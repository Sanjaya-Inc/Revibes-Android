---
name: revibes-backend-knowledge
description: "Revibes Cloud Functions API, Firestore models, JWT, Android DTO mapping. Use when changing endpoints, schemas, Ktorfit clients, or Android↔backend contracts."
---

# Revibes Backend Knowledge

<instructions>
Match routes, param names, and JSON fields exactly. Prefer `logistic-points.md` over stale `/settings` rows in `api-endpoints.md`.
</instructions>

<rules>
- Kotlin DTO names and `@SerialName` must match backend JSON.
- Do not hit production for smoke. Do not delete `v1(us-central1)`.
- Points are per waste type. `customTotalPoint: 0` is a real award.
</rules>

<references>
- **Logistic points**: [logistic-points.md](references/logistic-points.md) — type rates, `customTotalPoint` including 0, `/setting/app`, smoke/deploy.
- **API Endpoints**: [api-endpoints.md](references/api-endpoints.md) — route tables. Prefer `logistic-points.md` for drop-off/settings.
- **Firestore**: [firestore-models.md](references/firestore-models.md) — collections. Overlay in `logistic-points.md` for order/points fields.
- **Architecture**: [architecture.md](references/architecture.md) — Express `v1`, JWT, error JSON, Ktorfit mapping.
</references>

<related>
For related architecture and design skills in Revibes-Android:
- `/revibes-architecture` — Android modularity system, Ktorfit networking configuration, DI, Orbit MVI, and storage.
- `/ktor-expert` — Ktor client engine, authentication tokens, and serialization setup.
- `/offline-first-expert` — Room caching and offline repository synchronization.
</related>

<constraints>
- Android shell must use `rtk`. Backend `functions/` uses `npm` / `npx firebase-tools` only.
- Graph discovery: codebase-memory-mcp. Backend project id may differ from Android.
</constraints>
