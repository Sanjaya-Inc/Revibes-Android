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
