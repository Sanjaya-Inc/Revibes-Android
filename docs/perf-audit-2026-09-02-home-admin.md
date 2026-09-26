# Compose Performance Audit — 2026-09-02 — home-admin drop-off points

## Environment
- Compose UI: via `sjy` catalog / `buildlogic.compose` convention
- Compose Compiler: Kotlin Compose plugin (`kotlin-compose`)
- Kotlin: 2.4.10 (Koin compiler plugin warns it is newer than tested 2.4.0)
- AGP: via sjy convention (target SDK 37, min 24)
- Device available in this session: `emulator-5554` (`sdk_gphone16k_arm64`) only
- Macrobenchmark module: **absent**
- Baseline Profile: **absent**
- Compose Compiler Reports: opt-in via `-PcomposeCompilerReports=true` (writes to `<module>/build/compose_compiler`)

## Baseline (Phase 1)
- Cold startup median: **not measured**
- Scroll FrameTimingMetric (P50/P90/P99): **not measured**
- Baseline Profile present: **no**

Phase 1 could not be completed as specified. The audit skill requires a **release + R8** build and a **physical device**; this session only had an emulator, and the repo has no Macrobenchmark / Baseline Profile generator module. Emulator numbers would not be representative. No code-change claim below is a FrameTimingMetric delta.

Observed before the refetch fix (static, high confidence):
- First open: `onCreate` `LoadStores` **and** `Lifecycle.ON_RESUME` `LoadStores` → **two** `GET /stores` calls
- Every return to the screen (back from Add/Edit, Open Maps, or app resume) fired `ON_RESUME` again
- `loadStores()` always set `isLoading = true`, and `ContentStateSwitcher` swaps the list for `RevibesLoading()` — the list was unmounted and rebuilt on every visit
- The same `ON_RESUME` refetch existed on Manage Users and Manage Vouchers

## Diagnosis (Phase 2)
- Restartable-not-skippable composables: **unknown until** `./gradlew :features:home-admin:compileReleaseKotlin -PcomposeCompilerReports=true`
- Unstable classes: **unknown** until `classes.txt` is generated on a **release** build
- Top 5 recomposition / cost hotspots (ranked by frequency × cost on this surface):
  1. **`ON_RESUME` + loading switcher** — network + full-tree dispose/recompose of `LazyColumn` on every visit
  2. **`ContentStateSwitcher` loading branch** — while `isLoading`, `onContent()` is not composed; item state and scroll position are discarded
  3. **Item lambdas created inside `items { }`** — new callback instances each parent recomposition
  4. **`RoundedCornerShape(16.dp)` allocated inside each card**
  5. **Same `ON_RESUME` pattern** on `ManageUsersScreen` and `ManageVoucherScreen`
- Phase-misplaced reads: none identified on this surface (no per-frame animation/scroll-derived offset)

`StoreData` / `StorePositionData` are `val` data classes with stable primitives; list state already uses `ImmutableList`. `UserDomain` is now `@Immutable`. `VoucherDomain` stays `@Stable` because `termConditions` / `guides` remain `List<String>` for kotlinx.serialization.

## Fixes applied (Phase 3)
| Skill | Change | Files | Macrobench delta |
| ----- | ------ | ----- | ---------------- |
| using-efficient-effects | Removed `DisposableEffect` `ON_RESUME` reload on drop-off, users, and vouchers | `ManageDropOffPointsScreen.kt`, `ManageUsersScreen.kt`, `ManageVoucherScreen.kt` | not measured |
| lists / presentation (cache) | In-memory store cache; show cache immediately; refetch only after create/update via `StoreRepository.changes`; do not flip `isLoading` when the list already has rows | `StoreRepository.kt`, `ManageDropOffPointsScreenViewModel.kt` | not measured |
| lists / presentation (paginated) | Users/vouchers emit `changes` after mutations; list VMs silent-refresh (no full-screen loader). Pull-to-refresh still sets `isLoading`. No full-list cache (pagination). | `ManageUsersRepository.kt`, `ManageUsersScreenViewModel.kt`, `ManageVoucherRepository.kt`, `ManageVoucherScreenViewModel.kt` | not measured |
| optimizing-lazy-layouts | Hoist one `onEvent` into items; `contentType`; `Modifier.animateItem()`; hoist `DropOffCardShape` | `ManageDropOffPointsScreen.kt`, `ManageUsersScreen.kt`, `ManageVoucherScreen.kt`, `VoucherItem.kt` | not measured |
| stabilizing-compose-types | `@Immutable` on `StoreData`, `StorePositionData`, `UserDomain`, `VoucherValue`, `VoucherConditions` | `StoreModel.kt`, `UserDomain.kt`, `VoucherDomain.kt` | not measured |
| diagnosing-compose-stability | Compiler reports gated by `-PcomposeCompilerReports=true` in compose convention | `sjy-build-logic/.../compose.gradle.kts` | n/a |

Retry on the error state still calls `LoadStores` / `Refresh` (one shot, user-initiated).

## Verification (Phase 4)
- Cold startup median: **not re-measured**
- Scroll FrameTimingMetric (P50/P90/P99): **not re-measured**
- Baseline Profile regenerated: **no** (module does not exist)
- CI stability gate active: **no** (`stabilityDump` / `stabilityCheck` not present)

Compile check: `:features:home-admin:compileDebugKotlin`, `:features:manage-users:compileDebugKotlin`, `:features:manage-voucher:compileDebugKotlin`, and `:features:home-admin:testDebugUnitTest` succeeded after these fixes.

## Open items / follow-ups
- Add a Macrobenchmark module and run Phase 1 on a physical device (`StartupTimingMetric` + drop-off list `FrameTimingMetric`)
- Generate **release** compiler reports (`./gradlew :features:home-admin:compileReleaseKotlin -PcomposeCompilerReports=true`) and confirm `DropOffPointItem` is `restartable skippable` in `composables.txt`
- CI `stabilityDump` / `stabilityCheck` gate
- `VoucherDomain.termConditions` / `guides` stay `List<String>` for serialization; do not mark the type `@Immutable` until those fields are `ImmutableList` with a serializer
- Do not chase 100% skippability; next KPI is list scroll P90 on a real device after reports exist
