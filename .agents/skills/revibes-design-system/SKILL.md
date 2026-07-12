---
name: revibes-design-system
description: "Guidelines and source of truth for the Revibes Android application design system, custom Compose components, theme configuration, and event-driven navigation pattern. Use this skill when the user mentions custom themes, layout structures, colors/typography mapping, adding new components, or modifying navigation flows in Revibes-Android."
---

# Revibes Design System Skill

Source of truth for Revibes Android styling, reusable Compose UI components, and the decoupled navigation architecture.

<instructions>
Refer to specific reference documents for detailed specs on colors, typography, components, and navigation. Use the design tokens and layout constraints defined within.
</instructions>

<references>
- **Colors & Theme Mode**: [colors.md](.agents/skills/revibes-design-system/references/colors.md) — SmokePine palette, Light/Dark mode semantic mappings.
- **Typography & Font Scales**: [typography.md](.agents/skills/revibes-design-system/references/typography.md) — Text style scales, default system font usage.
- **Reusable UI Components**: [components.md](.agents/skills/revibes-design-system/references/components.md) — Reusable Text, Button, and TextField wrappers and stateless design templates.
- **Decoupled Navigation**: [navigation.md](.agents/skills/revibes-design-system/references/navigation.md) — Orbit MVI, NavigationEventBus, and Compose Destinations integration guidelines.
</references>

<related>
For generic Compose rules and performance optimization, refer to:
- `/compose-component-expert` — Component statelessness, slot APIs, Modifier.Node, and stability optimization.
- `/compose-m3-theme-expert` — Material 3 custom theme overrides, Monet wallpaper schemes, and CompositionLocal setups.
</related>
