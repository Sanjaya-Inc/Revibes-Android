# Presentation & MVI VM Patterns

Revibes standardizes presentation logic around the **MVI (Model-View-Intent)** architecture using the **Orbit MVI** library, integrating it with custom transitions and text-input validation behaviors.

---

## 1. Orbit MVI ViewModels

All ViewModels in the application must extend **`BaseViewModel`**:
- Implements `ContainerHost<State, Event>` and `EventReceiver<Event>`.
- Automatically catches unhandled coroutine exceptions via `CoroutineExceptionHandler` passed to the constructor.
- Automatically routes `NavigationEvent` types in `onEvent(event)` to the global `NavigationEventBus`.

```kotlin
@KoinViewModel
class LoginScreenViewModel(
    private val loginSubmitHandler: LoginSubmitHandler,
    private val loginExceptionHandler: LoginExceptionHandler
) : BaseViewModel<LoginScreenUiState, LoginScreenUiEvent>(
    initialState = LoginScreenUiState(),
    exceptionHandler = { syntax, exception ->
        loginExceptionHandler.onLoginError(syntax, exception)
    }
) {
    override fun onEvent(event: LoginScreenUiEvent) {
        super.onEvent(event) // Routes NavigationEvents to the NavigationEventBus
        intent {
            when (event) {
                is LoginScreenUiEvent.EmailChanged -> reduce { state.copy(userName = event.email) }
                LoginScreenUiEvent.SubmitLogin -> loginSubmitHandler.doLogin(state.loginType, this@LoginScreenViewModel, this)
                else -> postSideEffect(event)
            }
        }
    }
}
```

---

## 2. Form Input Rule

Always represent user text inputs via Compose's **`TextFieldValue`** in the screen state instead of raw `String`s. This preserves cursor offsets and selection focus states during state updates.

---

## 3. Cinematic Navigation Transitions

Screen entry and exit animations are managed by **`RevibesHostNavigationStyle`** which overrides `NavHostAnimatedDestinationStyle()` to provide a cinematic parallax sliding entry and drift-out exit animation:
- **Enter transition**: Subtle parallax slide (half width) combined with a slow fade-in and a scale-in snap (`EaseOutBack`).
- **Exit transition**: Slow drift-out (quarter width) with a fade-out.

---

## 4. Decoupled Navigation Pattern Reference

For the full detailed specification on the decoupled navigation architecture, how to implement event-driven routing, the `NavigationEventBus`, and how to register handlers, refer to:
- `/revibes-design-system`'s [navigation.md](.agents/skills/revibes-design-system/references/navigation.md)

---

## 5. Orbit MVI Best Practices & Anti-Patterns

- **No Long Delays inside `intent` blocks**: Never execute loops with `delay()` directly inside Orbit MVI `intent { ... }` blocks. Orbit processes `intent` blocks sequentially in a queue; holding an `intent` block locks the container queue and blocks subsequent state reductions. Launch timers in `viewModelScope.launch` and dispatch quick, non-blocking `intent { reduce { ... } }` blocks per tick.
- **NavigationEvent Dispatching**: `postSideEffect` in Orbit MVI posts to the side-effect channel and does not invoke `BaseViewModel.onEvent(event)`. For `NavigationEvent` types, call `onEvent(event)` so `BaseViewModel` routes them to `NavigationEventBus`.
- **Prevent Double Navigation**: Do not trigger both `onEvent(event)` (which routes to `NavigationEventBus`) AND manual `navigator.navigate(...)` in `collectSideEffect`.
- **State Sync Across Backstack**: Parent ViewModels should collect global flows (e.g., `userPointFlow`) inside `init { viewModelScope.launch { ... } }` to refresh screen state when returning from sub-screens.
- **No Fake Fallback Text**: Never display fake placeholder text when remote APIs return null or fail. Pass `error` to `ContentStateSwitcher` to show standard `GeneralError` with retry actions.
