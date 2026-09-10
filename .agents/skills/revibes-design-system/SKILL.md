---
name: revibes-design-system
description: "Revibes Android design system: tokens, Compose wrappers, NavigationEventBus. Use when changing theme, components, or navigation flows."
---

# Revibes Design System

<instructions>
Read the matching reference. Use tokens and layout constraints from those files.
</instructions>

<rules>
- Colors/type from `references/colors.md` and `references/typography.md`.
- Cross-feature routing: `NavigationEventBus` + `:app` handlers only.
</rules>

<references>
- **Colors**: [colors.md](references/colors.md) — SmokePine, light/dark semantics.
- **Typography**: [typography.md](references/typography.md) — text scales.
- **Components**: [components.md](references/components.md) — Text, Button, TextField wrappers.
- **Navigation**: [navigation.md](references/navigation.md) — Orbit MVI, NavigationEventBus, Destinations.
</references>

<related>
For generic Compose rules and performance optimization, refer to:
- `/compose-component-expert` — Component statelessness, slot APIs, Modifier.Node, and stability optimization.
- `/compose-m3-theme-expert` — Material 3 custom theme overrides, Monet wallpaper schemes, and CompositionLocal setups.
</related>

<constraints>
Use tokens and NavigationEventBus only. Do not invent new palette names.
</constraints>
