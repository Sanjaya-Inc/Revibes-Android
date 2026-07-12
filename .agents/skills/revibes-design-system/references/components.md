# Reusable UI Components & Specs

Reference sheet for Revibes custom Compose widgets, layouts, spacing tokens, and component design patterns.

<tokens>
### Spacing & Shape Tokens

All custom components must align with the standard layout values configured in `DESIGN.md`:

#### Spacing Scale (8dp Grid Rhythm)
- **Unit**: `8.dp` (All margins, paddings, gaps, and offsets must be multiples of 8dp, e.g. 8.dp, 16.dp, 24.dp, 32.dp).
- **Mobile Margins**: standard edge margins are `16.dp` (`container-padding`).
- **Card spacing**: gap between elements is `16.dp` (`card-gap`).
- **Touch Target**: minimum interactive touch area is `48.dp` (`touch-target`).

#### Shape Corner Rounding
- **sm**: `4.dp` (for tiny chip elements)
- **DEFAULT**: `8.dp` (for standard cards, TextFields / input fields container corners)
- **lg**: `12.dp` (for secondary container components)
- **xl**: `16.dp` (for global headers, bottom sheets top corners)
- **full**: `CircleShape` (for buttons, capsules)
</tokens>

<index>
### Reusable Components Index

All reusable core components are defined in package `com.carissa.revibes.core.presentation.compose.components`.

#### A. Text Component (`Text.kt`)
A custom wrapper around `BasicText` that incorporates Revibes text styles and selection color mappings.
- **Rules**: Prefer this over standard Material `Text`.
- **Usage**:
  ```kotlin
  Text(
      text = "Task Completed",
      style = RevibesTheme.typography.h2,
      color = RevibesTheme.colors.text
  )
  ```

#### B. Button Component (`Button.kt`)
Capsule-style button satisfying the standard `44.dp` minimum height.
- **Variants (`ButtonVariant`)**: Primary, PrimaryOutlined, PrimaryElevated, PrimaryGhost, Secondary, SecondaryOutlined, SecondaryElevated, SecondaryGhost, Destructive, DestructiveOutlined, DestructiveElevated, DestructiveGhost, Ghost.
- **Rules**: Always define text or a content block. Contains built-in progress indicator loading states.
- **Usage**:
  ```kotlin
  Button(
      text = "Submit Eco-Task",
      variant = ButtonVariant.Primary,
      loading = isSubmitting,
      onClick = { onEvent(SubmitEvent) }
  )
  ```

#### C. Text Inputs (`textfield/`)
Includes `OutlinedTextField`, `TextField`, and `UnderlinedTextField` components under the `textfield` package.
- **Rules**:
  - OutlinedTextField uses `DEFAULT` corner rounding shape (`8.dp` corners).
  - Uses `TextFieldValue` to track form values (retains selection/cursor state securely).
  - Outlined border shifts color dynamically depending on the state: default grey/translucent background, focused sky-blue outline (`RevibesTheme.colors.secondary`), and error red outline (`RevibesTheme.colors.error`).
- **Usage**:
  ```kotlin
  OutlinedTextField(
      value = uiState.fullNameInput, // TextFieldValue format
      onValueChange = { onEvent(FullNameChanged(it)) },
      label = { Text("Full Name") },
      isError = uiState.isFullNameInvalid
  )
  ```

#### D. Surfaces (`Surface.kt`)
Custom surface wrapper supporting custom elevation, shape, border, and ripple indication with four variants (static, clickable, selectable, and toggleable).
- **Rules**: Replaces standard Material `Surface`.
- **Variants**:
  - Static surface: `Surface(modifier, shape, color, contentColor, shadowElevation, border, content)`
  - Clickable surface: `Surface(onClick, modifier, enabled, shape, color, contentColor, shadowElevation, border, interactionSource, content)`
  - Selectable surface: `Surface(selected, onClick, modifier, enabled, shape, color, contentColor, shadowElevation, border, interactionSource, content)`
  - Toggleable surface: `Surface(checked, onCheckedChange, modifier, enabled, shape, color, contentColor, shadowElevation, border, interactionSource, content)`
- **Usage**:
  ```kotlin
  Surface(
      onClick = { /* action */ },
      shape = RoundedCornerShape(12.dp),
      color = RevibesTheme.colors.surface
  ) {
      // Content
  }
  ```

#### E. State & Transition Switchers

##### 1. Content State Switcher (`ContentStateSwitcher.kt`)
Orchestrates loading, error, and content UI states with smooth transitions.
- **Parameters**: `isLoading: Boolean`, `modifier: Modifier`, `error: String?`, `actionButton: Pair<String, () -> Unit>?`, `onContent: @Composable () -> Unit`
- **Usage**:
  ```kotlin
  ContentStateSwitcher(
      isLoading = uiState.isLoading,
      error = uiState.errorMessage,
      actionButton = "Retry" to { onEvent(RetryEvent) }
  ) {
      // Main screen content
  }
  ```

##### 2. State Switcher Animator (`StateSwitcherAnimator.kt`)
Low-level animator that handles switching states with slide/scale/fade animations based on type (Error, Loading, Content, Default).
- **Parameters**: `targetState: T`, `modifier: Modifier`, `transitionType: AnimationTransitionType`, `content: @Composable (T) -> Unit`
- **Usage**:
  ```kotlin
  StateSwitcherAnimator(
      targetState = currentTab,
      transitionType = AnimationTransitionType.Content
  ) { tab ->
      when(tab) {
          Tab.First -> FirstTabContent()
          Tab.Second -> SecondTabContent()
      }
  }
  ```

#### F. Header & Navigation Components

##### 1. Common Header (`CommonHeader.kt`)
Global header component featuring title, subtitle, optional search configuration, and standard toolbar navigation actions.
- **Parameters**: `title: String`, `backgroundDrawRes: Int`, `modifier: Modifier`, `viewModel: CommonHeaderViewModel`, `eventReceiver: EventReceiver<ToolbarEvent>`, `subtitle: String?`, `searchConfig: SearchConfig`, `onBackClicked: () -> Unit`, `onProfileClicked: () -> Unit`
- **Usage**:
  ```kotlin
  CommonHeader(
      title = "Dashboard",
      backgroundDrawRes = R.drawable.ic_header_bg,
      subtitle = "Eco-Activities",
      searchConfig = SearchConfig.Enabled(
          value = searchQuery,
          onValueChange = { onSearchChanged(it) }
      )
  )
  ```

##### 2. Tab Button (`TabButton.kt`)
Pill-shaped toggleable button using `ButtonVariant.Primary` when selected, and `ButtonVariant.PrimaryOutlined` when not.
- **Parameters**: `text: String`, `isSelected: Boolean`, `modifier: Modifier`, `onClick: () -> Unit`
- **Usage**:
  ```kotlin
  TabButton(
      text = "In Progress",
      isSelected = currentTab == Tab.Progress,
      onClick = { onTabSelected(Tab.Progress) }
  )
  ```

#### G. Status & State Placeholders

##### 1. Coming Soon (`ComingSoon.kt`)
Illustration and placeholder screen for features that are under development.
- **Parameters**: `featureName: String`, `modifier: Modifier`, `onClick: () -> Unit`
- **Usage**:
  ```kotlin
  ComingSoon(
      featureName = "Eco-Shop",
      onClick = { navigateBack() }
  )
  ```

##### 2. Revibes Empty State (`RevibesEmptyState.kt`)
A clean message panel for displaying empty states (e.g. empty lists).
- **Parameters**: `title: String`, `modifier: Modifier`, `message: String`
- **Usage**:
  ```kotlin
  RevibesEmptyState(
      title = "No Tasks Available",
      message = "Check back later for new eco-friendly activities."
  )
  ```

##### 3. Revibes Error State (`RevibesErrorState.kt`)
Simple screen wrapper for presenting an error state with a retry option.
- **Parameters**: `modifier: Modifier`, `message: String`, `onRetry: () -> Unit`
- **Usage**:
  ```kotlin
  RevibesErrorState(
      message = "Connection lost. Retry.",
      onRetry = { retryFetch() }
  )
  ```

##### 4. General Error (`GeneralError.kt`)
Detailed error illustration template that supports custom retry action buttons and is scrollable.
- **Parameters**: `error: String`, `modifier: Modifier`, `actionButton: Pair<String, () -> Unit>?`
- **Usage**:
  ```kotlin
  GeneralError(
      error = "Failed to load data",
      actionButton = "Retry" to { reload() }
  )
  ```

##### 5. Revibes Loading (`Loading.kt`)
A centered `CircularProgressIndicator` overlay spanning the screen/container.
- **Parameters**: `modifier: Modifier`, `color: Color`
- **Usage**:
  ```kotlin
  RevibesLoading()
  ```

#### H. Container & Structural Components

##### 1. Dashed Border Container (`DashedBorderContainer.kt`)
A custom container widget that renders a double-bordered (dashed lines) border with inner padding.
- **Parameters**: `modifier: Modifier`, `borderColor: Color`, `strokeWidth: Dp`, `dashLength: Dp`, `gapLength: Dp`, `cornerRadius: Dp`, `innerPadding: Dp`, `content: @Composable () -> Unit`
- **Usage**:
  ```kotlin
  DashedBorderContainer {
      Text("Double dashed border container content")
  }
  ```

##### 2. Main Background (`MainBackground.kt`)
Global background decoration containing the system crop-fitted background illustration.
- **Parameters**: `modifier: Modifier`
- **Usage**:
  ```kotlin
  MainBackground()
  ```

##### 3. Maintenance Checker (`MaintenanceChecker.kt`)
Feature toggling gatekeeper that wraps content and switches between enabled screen content and a `ComingSoon` screen based on config.
- **Parameters**: `featureName: FeatureName`, `modifier: Modifier`, `configRepository: ConfigRepository`, `onFeatureEnabled: @Composable () -> Unit`, `onBackAction: () -> Unit`
- **Usage**:
  ```kotlin
  MaintenanceChecker(
      featureName = FeatureName.EcoShop,
      onFeatureEnabled = { EcoShopScreen() },
      onBackAction = { goBack() }
  )
  ```

#### I. Data Presentation Components

##### 1. Transaction Details Content (`TransactionDetails.kt`)
A detailed structured transaction invoice sheet template displaying customer info, address, dates, list of transaction items (each with name, type, weight, and photos), estimated or final points, and disclaimer warning.
- **Parameters**: `customerName: String`, `locationAddress: String`, `dateLabel: String`, `date: String`, `itemDetailsTitle: String`, `items: ImmutableList<TransactionItem>`, `calculatingPointsText: String`, `totalPointsFormat: String`, `itemPointsFormat: String`, `nameLabel: String`, `locationLabel: String`, `modifier: Modifier`, `status: String?`, `pointsDisclaimer: String`, `isEstimatingPoints: Boolean`, `totalPoints: Int`, `itemPoints: ImmutableMap<String, Int>`, `actionButton: @Composable (() -> Unit)?`
- **Usage**:
  ```kotlin
  TransactionDetailsContent(
      customerName = "Jane Doe",
      locationAddress = "Green Village 12",
      dateLabel = "Date",
      date = "12/07/2026",
      itemDetailsTitle = "Receipt Details",
      items = uiState.items,
      calculatingPointsText = "Estimating points...",
      totalPointsFormat = "Total Points: %d",
      itemPointsFormat = "Item %d: %d pts",
      nameLabel = "Name",
      locationLabel = "Location",
      totalPoints = 120,
      itemPoints = uiState.itemPoints
  )
  ```

##### 2. Pager Indicator (`PagerIndicator.kt`)
A paginated dot indicator list with custom animations (dot growth, scaling, border wrapping) indicating active state.
- **Parameters**: `currentPage: Int`, `totalPage: Int`, `modifier: Modifier`
- **Usage**:
  ```kotlin
  PagerIndicator(
      currentPage = pagerState.currentPage,
      totalPage = pagerState.pageCount
  )
  ```
</index>

<conventions>
### Custom Component API Design Conventions

Follow these 2026 Jetpack Compose design rules when adding/modifying reusable widgets:

1. **Stateless UI Pattern**: Expose stateless slot APIs containing layout variables. Do not store mutable states internally in reusable presentation widgets.
2. **Modifier Parameter Order**: The first optional parameter of any custom composable MUST be `modifier: Modifier = Modifier`. This allows the caller to apply custom sizes/paddings from the parent layout scope.
3. **Trailing Lambda**: The last parameter should always be the trailing layout lambda (e.g. `content: @Composable () -> Unit` or similar slots).
4. **Stable Parameters**: Wrap lists or unstable collections in `@Immutable` objects to prevent unnecessary recompositions. Rely on strong skipping mode.
5. **Primitive State**: Use primitive state holders (e.g. `mutableIntStateOf()`) to bypass Java autoboxing memory overhead.
6. **No Composed Modifiers**: Use `Modifier.Node` elements instead of `composed { ... }` blocks for custom modifiers to avoid performance issues during layout passes.
</conventions>
