---
version: alpha
name: Revibes
colors:
  primary: "#43665F"              # SmokePine
  on-primary: "#FFFFFF"
  secondary: "#74C0E8"            # Spritzig
  on-secondary: "#FFFFFF"
  tertiary: "#E5F2E6"             # Honeydew
  on-tertiary: "#43665F"
  background: "#FFFFFF"           # Light mode default
  on-background: "#000000"
  surface: "#E2E2E2"              # Gray200
  on-surface: "#000000"
  outline: "#DFDFDF"              # Gray300
  error: "#DE1135"                # Red600
  on-error: "#FFFFFF"
  success: "#1DAF61"              # Green600
  on-success: "#FFFFFF"
typography:
  headline-lg:
    fontFamily: System
    fontSize: 24px
    fontWeight: "700"
    lineHeight: 32px
  headline-md:
    fontFamily: System
    fontSize: 20px
    fontWeight: "700"
    lineHeight: 28px
  body-md:
    fontFamily: System
    fontSize: 16px
    fontWeight: "400"
    lineHeight: 24px
  label-sm:
    fontFamily: System
    fontSize: 12px
    fontWeight: "500"
    lineHeight: 16px
rounded:
  sm: 4px
  DEFAULT: 8px                    # Cards / TextFields
  lg: 12px
  xl: 16px                        # Header bottom corners
  full: 9999px                    # Buttons (CircleShape)
spacing:
  unit: 8px                       # Base 8dp grid unit
  container-padding: 16px
  card-gap: 16px
  safe-area-bottom: 34px          # Standard system navigation bar safe zone
  touch-target: 48px              # Minimum recommended interactive dimension
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "{colors.on-primary}"
    typography: "{typography.label-sm}"
    rounded: "{rounded.full}"
    height: 44px                  # Matches the 44.dp MinHeight in ButtonDefaults
    padding: 8px 16px
  input-field:
    backgroundColor: "{colors.surface}"
    textColor: "{colors.on-surface}"
    typography: "{typography.body-md}"
    rounded: "{rounded.DEFAULT}"
    padding: 12px
    height: 48px
---

## Overview

Revibes is a mobile-first application focused on sustainability, eco-friendly habits, and community-driven actions. The design personality is **approachable, clean, and organic**, conveying a sense of renewal and environmental responsibility. 

- **Atmosphere:** Natural, serene, and modern. Spacings are kept clean with soft container boundaries and deep breathing room.
- **Multi-platform Philosophy:** The system uses fluid, adaptive components to transition smoothly between mobile views (phones, tablets) and potential web extensions. Surfaces utilize soft containers and tactile elevations rather than harsh separators.

---

## Colors

The Revibes color palette is grounded in earthy, organic tones paired with vibrant, energetic accents.

- **Primary (`#43665F` - SmokePine):** An organic, dark pine-green representing sustainability, stability, and structure. Used for core brand actions, filled buttons, primary headers, and active navigation nodes.
- **Secondary (`#74C0E8` - Spritzig):** A lively sky-blue reflecting water and clean energy. Used for secondary highlights, links, focus borders, and interactive accents.
- **Tertiary (`#E5F2E6` - Honeydew):** A soft, pale mint green acting as a natural contrast to SmokePine. Used as a background fill for cards, modal dialogs, and success containers.
- **Neutral (Gray Scale):** Ranges from pure `White` (`#FFFFFF`) to dark charcoal `Gray900` (`#282828`). Forms the structural background, borders, and body text.
- **System States:**
  - **Success (`#1DAF61` - Green600):** Signifies completed eco-tasks and validations.
  - **Error (`#DE1135` - Red600):** Highlight critical errors, destructive states, and alerts.

### Light & Dark Theme Mapping

The system supports dark mode by reversing luminance steps, mapping surfaces to dark shades while maintaining readability:

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
| `on-background`| `#000000` (Black) | `#FFFFFF` (White) |

---

## Typography

Revibes typography prioritizes high legibility and clean hierarchy across screen scales. The default system font scale maps directly to the following text styles:

- **Headlines:** Clean, bold weights optimized for short titles and status screens.
  - **h1:** Bold, 24sp size, 32sp line height, 0sp letter spacing.
  - **h2:** Bold, 20sp size, 28sp line height, 0sp letter spacing.
  - **h3:** Bold, 16sp size, 24sp line height, 0sp letter spacing.
  - **h4:** SemiBold, 16sp size, 24sp line height, 0sp letter spacing.
- **Body:** Standard body layouts for lists, detail cards, and descriptions.
  - **body1 (Primary):** Regular, 16sp size, 24sp line height, 0sp letter spacing.
  - **body2 (Secondary):** Regular, 14sp size, 20sp line height, 0.15sp letter spacing.
  - **body3 (Small/Metadata):** Regular, 12sp size, 16sp line height, 0.15sp letter spacing.
- **Labels & Interactive:**
  - **label1:** Medium (W500), 14sp size, 20sp line height, 0.1sp letter spacing.
  - **label2:** Medium (W500), 12sp size, 16sp line height, 0.5sp letter spacing.
  - **label3:** Medium (W500), 10sp size, 12sp line height, 0.5sp letter spacing.
  - **button:** Medium (W500), 14sp size, 20sp line height, 1sp letter spacing.
  - **input:** Regular, 16sp size, 24sp line height, 0sp letter spacing.

### Platform Adaptations
- **Web Scaling:** Desktop displays increase header sizes (e.g., h1 -> 32sp) and apply fluid line heights.
- **Mobile Scaling:** Respects platform accessibility settings (Android Auto-scaling / iOS Dynamic Type) to allow text scaling without cutting off components.

---

## Layout

The Revibes layout grid system is built around a standard `8dp` spatial unit to maintain consistent spacing proportions.

- **Grid Rhythm:** Margins, padding, and spacers must be multiples of 8dp (e.g., 8dp, 16dp, 24dp, 32dp).
- **Mobile Margins:** Standard mobile screens use a 16dp margin (`container-padding`) from the screen edges, with 8dp or 16dp item spacing (`card-gap`).
- **Safe Area Insets:** Layouts must account for status bars, camera notches, and software home indicators. Screen content should not overlay these navigation Zones unless intentionally using a fully immersive layout (like maps).

---

## Elevation & Depth

Visual layers are structured to convey depth and focus, using background coloring and subtle elevations rather than heavy shadow constructs.

- **Layer 0 (Background):** Base app background (`#FFFFFF` in light mode, `#000000` in dark mode).
- **Layer 1 (Card Containers):** Elevated surface containers (using `#E2E2E2` or specific light background shades like Honeydew `#E5F2E6`).
- **Elevation Shadows:**
  - Standard cards and buttons use a default elevation of `2.dp` when resting.
  - Interactive components do not scale shadow heights heavily; they rely on overlay color shifts (e.g., ripple effects and state indicators).

---

## Shapes

Revibes relies on soft, rounded shape paths to reinforce its friendly and natural branding.

- **Buttons & Chips:** Use `full` corner rounding (`CircleShape` / `9999px`) to create organic capsule buttons.
- **TextFields & Input Fields:** Use a uniform corner radius of `8dp` (`rounded.DEFAULT`) for standard touch comfort.
- **Cards & Bottom Sheets:** Card containers (e.g., Transaction details) use `8dp` corners. Global header overlays or floating bottom sheets use `16dp` bottom/top corner radiuses (`rounded.xl`) to frame the views.

---

## Components

### Buttons
Buttons feature standard 44dp height to conform with mobile ergonomics.

- **Primary Filled Button:** Container uses `primary` background and `on-primary` text. Ripple uses transparent overlays.
- **Outlined Button:** Transparent container, `outline` borders, and `primary` text.
- **Ghost/Text Button:** Transparent container with `primary` or `secondary` text.
- **Touch Target:** Minimum height of 44dp (`ButtonDefaults.MinHeight`), centered vertically inside layout columns.

### Text Inputs
- **Container:** Rectangular structure with `8dp` rounded corners.
- **States:**
  - Default: Light grey/translucent background with outline.
  - Active/Focused: Clear outline highlight using `secondary` (Spritzig `#74C0E8`).
  - Error: Outline shifts to `error` (`Red600`).

---

## Do's and Don'ts

- **Do** map all component properties to semantic tokens (`primary`, `surface-container`) instead of direct color hex codes (`#43665F`).
- **Don't** use hover effects as the only way to reveal vital actions (ensure mobile users can access actions directly).
- **Do** respect system-wide notch and bottom safe area zones to prevent interactive components from clipping.
- **Don't** drop below 44dp for primary buttons or clickable areas.
- **Do** maintain a consistent 8dp spacing rhythm across all card offsets.
