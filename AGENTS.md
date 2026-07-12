# AGENTS.md

> **Project**: Revibes Android application (Kotlin + Compose)
> **Goal**: Maintain minimal, structure-decoupled rules. Let skills do the heavy lifting.

<rules>
## 1. Shell & Tooling Rules

### Shell Commands
Prefix all command line operations with `rtk`:
- `rtk git status`, `rtk git log`, `rtk git diff`
- `rtk ls`, `rtk grep`, `rtk find`
- `rtk read <file>` (displays full file contents)
- `rtk smart <file>` (displays 2-line summaries of files)
- `rtk read <file> -l aggressive` (extracts signatures/interfaces only)

### Code Discovery
1. **First Option**: Use codebase-memory-mcp graph tools (`search_graph`, `trace_path`, `get_code_snippet`).
2. **Second Option**: Use Serena (`find_symbol`, `find_declaration`, `find_implementations`, `find_referencing_symbols`, `get_symbols_overview`).
3. **Fallback**: Use grep/glob only for config values, string literals, or when graph tools return insufficient results.

### Code Editing & Diagnostics
- Use Serena to perform symbol edits: `replace_symbol_body`, `insert_after_symbol`, `insert_before_symbol`, `replace_content`.
- **Diagnostics**: Call Serena `get_diagnostics_for_file` immediately after modifying any source file.
- **Decisions Memory**: Log changes and design decisions using `write_memory` / `read_memory`.
</rules>

<design>
## 2. Core Code Design & Architecture

- **Clean Architecture & MVI**: Orbit MVI is used.
  - Every screen VM must extend `BaseViewModel<ScreenUiState, ScreenUiEvent>`.
  - Use `@KoinViewModel` annotation on ViewModels to register them with Koin DI.
- **Forms**: Represent text inputs via `TextFieldValue` to preserve cursor and selection states rather than raw Strings.
- **Dependencies**: Inject via constructor parameters. Use Version Catalog (`libs.versions.toml`) to refer to dependencies and plugins.
- **Self-Documenting Code**: No comments explaining *what* — only *why* when non-obvious. Do not include docstrings or backwards-compat shims.
</design>

<skills>
## 3. Specialized Skills Index (Heavy Lifting)

Consult these custom skills for specific tasks:
- `revibes-architecture` — Guidelines and architectural specifications for the Revibes Android application (modularity system, networking, local storage, DI, Orbit MVI, and utilities).
- `revibes-design-system` — Source of truth for styling, colors/typography mappings, custom components (Button, Text, TextFields), and NavigationEventBus patterns.
- `compose-component-expert` — Guidelines for custom modifiers, slot APIs, Modifier.Node, and stability optimization.
- `compose-m3-theme-expert` — Material 3 custom theme overrides, wallpaper Monet schemes, and CompositionLocal setups.
- `koin-expert` — Koin compile-time DI, compiler annotation settings.
- `ktor-expert` — Client/server communication, client engines, authentication tokens.
- `kotlin-coroutine-expert` — Flows, structured concurrency, tests (Turbine / runTest).
- `offline-first-expert` — Caching, database models (Room / SQLite Bundled), repository sync logic.
- `solid-expert` — Clean architecture patterns, composition over inheritance.
- `testing-expert` — Unit/integration tests, MockK, flows verification.
</skills>
