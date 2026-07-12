# Typography Design Specs & Scale

Reference sheet for Revibes typography, font scales, and text style implementations.

<scale>
All styles map to specific text rules using standard system font scales:

| Token Name | Weight | Font Size (sp) | Line Height (sp) | Letter Spacing (sp) | Typical Usage |
| :--- | :--- | :--- | :--- | :--- | :--- |
| `h1` | Bold (W700) | 24 | 32 | 0 | Bold header titles, status screen titles |
| `h2` | Bold (W700) | 20 | 28 | 0 | Section titles, header summaries |
| `h3` | Bold (W700) | 16 | 24 | 0 | Sub-headers, list header sections |
| `h4` | SemiBold (W600) | 16 | 24 | 0 | Card section details |
| `body1` | Regular (W400) | 16 | 24 | 0 | Primary page descriptions, lists body text |
| `body2` | Regular (W400) | 14 | 20 | 0.15 | Secondary body content, card subtexts |
| `body3` | Regular (W400) | 12 | 16 | 0.15 | Small metadata descriptors, fine print |
| `label1` | Medium (W500) | 14 | 20 | 0.1 | Standard interactive form labels |
| `label2` | Medium (W500) | 12 | 16 | 0.5 | Secondary badges, chips, auxiliary text |
| `label3` | Medium (W500) | 10 | 12 | 0.5 | Tiny button chips, info dots |
| `button` | Medium (W500) | 14 | 20 | 1 | Capsule buttons text style |
| `input` | Regular (W400) | 16 | 24 | 0 | TextField user inputs text style |
</scale>

<implementation>
Defined in `core/src/main/java/com/carissa/revibes/core/presentation/compose/Typography.kt`:

### Typography Model Container
```kotlin
data class Typography(
    val h1: TextStyle,
    val h2: TextStyle,
    val h3: TextStyle,
    val h4: TextStyle,
    val body1: TextStyle,
    val body2: TextStyle,
    val body3: TextStyle,
    val label1: TextStyle,
    val label2: TextStyle,
    val label3: TextStyle,
    val button: TextStyle,
    val input: TextStyle,
)
```

### Typography Providers
```kotlin
@Composable
fun provideTypography(): Typography {
    val fontFamily = fontFamily() // Resolves to FontFamily.Default
    return defaultTypography.copy(
        h1 = defaultTypography.h1.copy(fontFamily = fontFamily),
        // ... copy other styles with proper fontFamily configuration
    )
}

val LocalTypography = staticCompositionLocalOf { defaultTypography }
val LocalTextStyle = compositionLocalOf(structuralEqualityPolicy()) { TextStyle.Default }
```

Access styles via:
```kotlin
val typography = RevibesTheme.typography
Text(text = "Hello", style = typography.body1)
```
</implementation>

<constraints>
- **DON'T** hardcode font configurations or font sizes inside screen layout files. Always utilize `RevibesTheme.typography.<style>` to retain system-wide theme mapping.
- **DO** leverage the custom `Text` component wrapper (built on `BasicText` under `com.carissa.revibes.core.presentation.compose.components.Text`) instead of `androidx.compose.material3.Text` to align automatically with the design rules and the customized text selection/interaction colors.
- **DO** respect system-wide font scale sizes (Android Auto-scaling / accessibility configurations). Avoid specifying fixed heights on containers containing text to prevent text cut-off.
</constraints>
