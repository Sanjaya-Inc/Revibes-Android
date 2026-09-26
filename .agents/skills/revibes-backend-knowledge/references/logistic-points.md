# Logistic points, settings, complete, smoke

Verified against `functions/src`. Prefer this over stale `/settings` rows in `api-endpoints.md`.

<scoring>
- Points are per waste type (`organic` | `non-organic` | `b3`), not `rate × pcs`. `LogisticItem.calculatePoint(setting)` ignores `weight`.
- Defaults in `AppSetting.point` are all `5`.
- `submitOrder` / `PATCH .../submit` does not credit points.
- `completeOrder` credits via `UserPointController.txAddPoint` with `sourceType: "logistic-order"`. Status must be `submitted`.
- `resolveOrderPoint(items, setting, customTotalPoint, customPoints)` in `models/logisticOrderPoints.ts`.
- Use `customTotalPoint != null`. `if (customTotalPoint)` skips `0`.
</scoring>

<http>
| Method | Path | Auth | Notes |
|---|---|---|---|
| `POST` | `/logistic-orders/estimate-point` | user | `{ items: [{ name, type, weight, unit: "kg"\|"pcs" }] }` → `{ items, total }` |
| `PATCH` | `/logistic-orders/:id/complete` | admin | `{ customTotalPoint?, customPoints? }` |
| `GET` | `/setting/app` | admin | `{ point, dailyReward }` |
| `PUT` | `/setting/app` | admin | Zod requires **both** `point` and `dailyReward` |

Presign: `POST /logistic-orders/:id/items/:itemId/media/presigned-url` `{ contentType }`. Signed URL must not include `x-goog-acl` (Firebase UBLA). HEIC → 400. Missing GCS object on submit: strip media, still `200`.
</http>

<deploy>
- Production function: `v1` in `us-central1`. Do not delete it. Do not switch to `asia-southeast2` until Android `BASE_URL` ships.
- Smoke: project `demo-revibes`. Emulators Firestore `8098`, Auth `9109`, Storage `9198`. Express `127.0.0.1:5012` requiring `lib/handlers/https/server.js`. Dummy SA + emulator hosts. Never boot Express on production `functions/.env` without emulator hosts.
- Unit: `npm test` in `functions`. `fileStorage.test.ts` may miss `lib/**/*.test.js` glob depth.
</deploy>

<constraints>
Must not hit production for smoke. Must not delete `v1(us-central1)` until Android `BASE_URL` ships.
</constraints>
