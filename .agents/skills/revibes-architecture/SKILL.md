---
name: revibes-architecture
description: "Guidelines and architectural specifications for the Revibes Android application (modularity system, networking, local storage, DI, Orbit MVI, and utilities). Make sure to use this skill whenever the user mentions adding new modules, writing database/preferences storage, implementing Ktorfit services, configuring Koin injection, creating MVI ViewModels, or implementing custom navigation transitions."
---

# Revibes Architecture Skill

Source of truth for the Revibes Android architecture, modular design, Ktorfit networking, MMKV/SharedPreferences local storage, Koin dependency injection, Orbit MVI presentation pattern, and core reusable utilities.

<instructions>
Refer to specific reference documents for detailed specs on modularity, dependency injection, networking, local storage, and presentation. Follow the architectural design patterns, testing standards, and module creation guides defined within.
</instructions>

<references>
- **Colors & Theme Mode**: [colors.md](.agents/skills/revibes-design-system/references/colors.md) — SmokePine palette, Light/Dark mode semantic mappings.
- **Modularity & New Modules**: [modularity.md](.agents/skills/revibes-architecture/references/modularity.md) — Precompiled script plugins, custom `sjy` catalog, feature module registration, and step-by-step module creation.
- **Dependency Injection**: [di.md](.agents/skills/revibes-architecture/references/di.md) — Koin compile-time annotations, startup initialization, and VM injection.
- **Networking**: [networking.md](.agents/skills/revibes-architecture/references/networking.md) — Ktorfit & Ktor, OkHttp client configuration, auth headers, and ApiException handler.
- **Local Storage**: [storage.md](.agents/skills/revibes-architecture/references/storage.md) — LocalDataSource (MMKV/SharedPref hybrid), interface delegation for auth tokens and user data.
- **Presentation & Navigation**: [presentation.md](.agents/skills/revibes-architecture/references/presentation.md) — Orbit MVI, BaseViewModel, decoupled NavigationEventBus, and custom host navigation transitions.
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
