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

<skills>
## 2. Skills

Load these; keep details in each skill's `references/`:
- `/revibes-architecture` — modules, Ktorfit, DI, Orbit MVI, drop-off points
- `/revibes-design-system` — tokens, components, NavigationEventBus
- `/revibes-backend-knowledge` — HTTP, DTO, Firestore

Also: `compose-component-expert`, `compose-m3-theme-expert`, `koin-expert`, `ktor-expert`, `kotlin-coroutine-expert`, `offline-first-expert`, `solid-expert`, `testing-expert`.
</skills>

<constraints>
AGENTS.md must stay rules + skill refs only. Domain facts go in skill `references/`.
</constraints>
