---
name: revibes-architecture
description: "Revibes Android architecture: modules, Ktorfit, MMKV, Koin, Orbit MVI. Use when adding modules, storage, Ktorfit, Koin, ViewModels, or navigation transitions."
---

# Revibes Architecture

<instructions>
Read the matching reference. Do not invent module edges, HTTP clients, or drop-off scoring.
</instructions>

<rules>
- VM extends `BaseViewModel<UiState, UiEvent>`; register with `@KoinViewModel`.
- Text inputs: `TextFieldValue`, not raw `String`.
- Inject via constructor. Dependencies from Version Catalog (`libs.versions.toml` / `sjy`).
- Comments: *why* only. No docstrings or compat shims.
- `:features:*` depend on `:core` only. Cross-feature DTOs are copied, not imported.
</rules>

<references>
- **Modularity**: [modularity.md](references/modularity.md) — plugins, `sjy` catalog, new feature module steps.
- **DI**: [di.md](references/di.md) — Koin compile-time, startup, VM injection.
- **Networking**: [networking.md](references/networking.md) — Ktorfit, OkHttp, auth, `ApiException`.
- **Storage**: [storage.md](references/storage.md) — LocalDataSource, tokens, user data.
- **Presentation**: [presentation.md](references/presentation.md) — Orbit MVI, NavigationEventBus.
- **Drop-off points**: [drop-off-points.md](references/drop-off-points.md) — pcs vs kg, conversion UI, accept points, GCS PUT.
</references>

<related>
For advanced or deeper domain knowledge, refer to these expert skills:
- `/revibes-design-system` — Reusable custom UI components and theme configurations.
- `/koin-expert` — Deep Koin DI compile-time annotation guidelines.
- `/ktor-expert` — Advanced client engines and custom plugins.
- `/kotlin-coroutine-expert` — Coroutine Flows, structured concurrency, and testing.
- `/offline-first-expert` — Caching, database models (Room / SQLite Bundled), repository sync logic.
- `/testing-expert` — Unit/integration tests, MockK, flows verification.
</related>

<constraints>
- Developers **must** prefix all shell command operations with `rtk` **only**.
- Always **require** using codebase-memory-mcp graph tools for all code discovery.
- **Should** use Serena MCP for symbol edits and call get_diagnostics_for_file immediately after.
</constraints>
