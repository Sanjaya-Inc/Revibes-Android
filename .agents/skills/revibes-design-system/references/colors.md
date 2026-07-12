# Colors Design & Theme Mappings

Reference sheet for Revibes colors palette, light/dark mode mappings, and implementation guidelines.

<palette>
The Revibes color palette is grounded in earthy, organic tones paired with vibrant accents:
- **SmokePine (`#43665F`)**: Core brand actions, filled buttons, primary headers, active navigation nodes.
- **Spritzig (`#74C0E8`)**: sky-blue accent for links, secondary highlights, active borders, and highlights.
- **Honeydew (`#E5F2E6`)**: pale mint green used for backgrounds of cards, modal dialogs, success containers.

### Hex to Code Mappings
Defined in `core/src/main/java/com/carissa/revibes/core/presentation/compose/Color.kt`:
```kotlin
val SmokePine = Color(0xFF43665F)
val Spritzig = Color(0xFF74C0E8)
val Honeydew = Color(0xFFE5F2E6)
```
</palette>

<mappings>
### Light & Dark Theme Mappings

| Semantic Token | Light Mode Value | Dark Mode Value |
| :--- | :--- | :--- |
| `primary` | `#43665F` (SmokePine) | `#FFFFFF` (White) |
| `on-primary` | `#FFFFFF` (White) | `#000000` (Black) |
| `secondary` | `#74C0E8` (Spritzig) | `#C7C7C7` (Gray400) |
| `on-secondary` | `#FFFFFF` (White) | `#FFFFFF` (White) |
| `tertiary` | `#E5F2E6` (Honeydew) | `#B7CEFA` (Blue300) |
| `on-tertiary` | `#43665F` (SmokePine) | `#000000` (Black) |
| `surface` | `#E2E2E2` (Gray200) | `#282828` (Gray900) |
| `on-surface` | `#000000` (Black) | `#FFFFFF` (White) |
| `background` | `#FFFFFF` (White) | `#000000` (Black) |
| `on-background` | `#000000` (Black) | `#FFFFFF` (White) |
| `outline` | `#DFDFDF` (Gray300) | `#4B4B4B` (Gray800) |
| `error` | `#DE1135` (Red600) | `#FC7F79` (Red400) |
| `on-error` | `#FFFFFF` (White) | `#000000` (Black) |
| `success` | `#1DAF61` (Green600) | `#1A7544` (Green800) |
| `on-success` | `#FFFFFF` (White) | `#000000` (Black) |
</mappings>

<implementation>
The design system implements a custom `Colors` container and uses a static composition local provider.

### Theme Colors Definition
```kotlin
@Immutable
data class Colors(
    val primary: Color,
    val onPrimary: Color,
    val secondary: Color,
    val onSecondary: Color,
    val tertiary: Color,
    val onTertiary: Color,
    val error: Color,
    val onError: Color,
    val success: Color,
    val onSuccess: Color,
    val disabled: Color,
    val onDisabled: Color,
    val surface: Color,
    val onSurface: Color,
    val background: Color,
    val onBackground: Color,
    val outline: Color,
    val transparent: Color = Color.Transparent,
    val white: Color = White,
    val black: Color = Black,
    val text: Color,
    val textSecondary: Color,
    val textDisabled: Color,
    val scrim: Color,
    val elevation: Color,
)
```

### Static Composition Local Provider
```kotlin
val LocalColors = staticCompositionLocalOf { LightColors }
```
Access colors in composables using `RevibesTheme.colors.primary` (which resolves to `LocalColors.current.primary`).
</implementation>

<constraints>
- **DON'T** hardcode raw Hex/Color constants directly inside presentation code (e.g. `Color(0xFF43665F)` or `SmokePine`). Use `RevibesTheme.colors.primary` or custom CompositionLocal tokens.
- **DON'T** use `compositionLocalOf` for colors configuration (causes heavy snapshot-read checks and unnecessary recomposition overhead). Always use `staticCompositionLocalOf`.
- **DON'T** read animated theme colors directly in layout phase (e.g., `Box(Modifier.background(animatedColor))`). Instead, defer color reads to the draw phase using `drawBehind { drawRect(animatedColor) }` to skip layout passes.
- **DO** always use the semantic `contentColorFor(color)` helper to automatically map matching contrast text colors based on background color choice.
</constraints>
