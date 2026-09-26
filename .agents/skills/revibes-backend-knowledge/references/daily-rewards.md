# Daily reward settings, banner, flat amount

Verified against `functions/src` (deployed 2026-09-26). Prefer this over `api-endpoints.md` for daily-reward concerns.

<model>
- `AppSetting.dailyReward`: `{ days, initialPoint, multiplier, bannerText? }`.
- `dailyRewardAmounts()` in `models/dailyRewardSchedule.ts`: flat `[initialPoint × days]`. `multiplier` is ignored — legacy field, kept for schema compat.
- `UserDailyReward.applySettingAmount(amount)`: overrides stored doc amount for **unclaimed** rows only. Claimed history rows keep the amount actually awarded.
- `getDailyRewards` sets `bannerText` + applies setting amount on every read. Legacy docs (5/10/15… or 1/2/3… eras) never leak to UI or claims.
- Claim credits `txAddPoint` with `sourceType: "daily-reward"`, amount = setting `initialPoint`.
- Claiming the last row wipes the cycle and regenerates from current setting.
</model>

<http>
| Method | Path | Auth | Notes |
|---|---|---|---|
| `GET` | `/me/daily-rewards` | user | Items carry `amount` (setting-driven) + `bannerText` |
| `PUT` | `/setting/app` | admin | Zod requires **both** `point` and `dailyReward`. Send full `point` object when editing only `dailyReward` — fetch current first. |
</http>

<admin-ui>
Android admin: `ManageDailyCheckInScreen` (`:features:home-admin`) edits `bannerText` + `initialPoint`. It re-fetches the full setting before `PUT` so `point` is preserved.
</admin-ui>
<android-user>
- `:features:point` `DailyRewardData` DTO: `bannerText: String? = null` (server always sends it; null-tolerant for old payloads).
- `DailyRewardMapper.toDailyPoint()` passes `amount` through verbatim — backend guarantees setting-driven amount; do NOT hardcode in the mapper.
- `PointScreen` renders `dailyRewards.firstOrNull()?.bannerText`; blank → hidden with `Spacer(8.dp)` fallback.
- Screen layout: single scrollable `LazyColumn`; see `/revibes-design-system` `components.md` → Screen Layouts.
</android-user>

<gotchas>
- `bannerText` empty/blank → Android hides the banner row (spacer keeps spacing).
- Wipe script `functions/scripts/wipe-unclaimed-daily-rewards.js` is legacy from the flat-1 migration; reads now self-correct, script optional.
</gotchas>

Must not hardcode daily amounts client-side; backend reads are the single source of truth.
