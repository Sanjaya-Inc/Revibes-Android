# Drop-off quantity, conversion, accept points

Android rules for logistic drop-off. Server scoring and routes: `/revibes-backend-knowledge` `logistic-points.md`.

<rules>
- Pcs = exact positive int (`parsePositiveCount`). Kg keeps four ranges. Do not restore 2–5 / 6–10 bucket chips or midpoint fakes (5 / 8).
- `:features` modules do not depend on each other. Duplicate a small `AppSetting` DTO in `:features:manage-transaction`; do not import `:features:home-admin`.
- Admin menu **Manage Drop-off Points** = store locations. Conversion rates = **Drop-off Point Values** → `ManageDropOffConversionScreen`. Daily check-in banner + point amount = **Daily Check-in Settings** → `ManageDailyCheckInScreen` (separate domain; do NOT add daily-reward fields to the conversion screen).
- `PUT /setting/app` body must include both `point` and `dailyReward`. GET first; keep the other object; PUT the edited one. Shared `Json` omits default values; PUT DTOs need `@EncodeDefault`.
- Pending transaction detail shows verified points above Accept/Reject. Prefill with `suggestedAcceptPoints`. If `GET setting/app` fails, omit type-rate fallback so Accept sends no `customTotalPoint`. Blank Accept still uses server type defaults.
- GCS PUT uses an isolated OkHttp client. Do not send `X-GOOG-ACL`. API JSON calls use `Content-Type: application/json`. Cache gallery URIs to a file before upload.
- `GeneralErrorMapper`: show `cause.message` unless timeout, unknown host, connect failure, or blank. HTTP 4xx/5xx uses server `message`.
- Do not commit `docs/perf-audit-*.md`.
</rules>

<files>
- `features/drop-off/.../DropOffMedia.kt` — `parsePositiveCount`, `normalizedContentType`
- `features/home-admin/.../ManageDropOffConversionScreen.kt` — GET/PUT `setting/app`
- `features/manage-transaction/.../TransactionDetailScreen.kt` — verified points field
- `:app` `HomeAdminScreenNavigationHandler` — `NavigateToManageDropOffConversion`, `NavigateToManageDailyCheckIn`
</files>

<constraints>
Do not restore pcs bucket chips. Features must not import other features.
</constraints>
