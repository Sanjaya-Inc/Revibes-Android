# Dependency Injection Guidelines (Koin DI)

Revibes uses **Koin** as its Dependency Injection framework, leveraging Koin compile-time annotations (`koin-annotations` Ksp compiler plugin) for compile-time safety and automatic registration.

---

## 1. DI Annotations Reference

Always use compile-time annotations instead of manual DSL declarations (`get()`, `single {}`) whenever possible.

### `@Module` & `@ComponentScan`
Declares a Koin module that automatically scans classes within the designated packages:
```kotlin
@Module
@ComponentScan("com.carissa.revibes.auth")
object AuthModule
```
*Tip: Place annotations on an `object` singleton.*

### `@Single`
Registers a class as a singleton (persisted throughout the application's lifecycle):
```kotlin
@Single
internal class AuthTokenDataSourceImpl(
    private val localDataSource: LocalDataSource
) : AuthTokenDataSource
```
- By default, Koin binds to the primary interface.
- If explicit interface bindings are needed, use `@Single(binds = [SomeInterface::class])`.

### `@Factory`
Registers a class to be recreated every time it is injected:
```kotlin
@Factory
class NavigationEventHandlerRegistry(
    private val handlers: List<NavigationEventHandler>
)
```

### `@KoinViewModel`
Registers a ViewModel in Koin:
```kotlin
@KoinViewModel
class LoginScreenViewModel(
    private val loginSubmitHandler: LoginSubmitHandler,
    private val loginExceptionHandler: LoginExceptionHandler
) : BaseViewModel<LoginScreenUiState, LoginScreenUiEvent>(...)
```

---

## 2. Startup & Multimodule Initialization

To prevent blocking the UI thread and ensure correct initialization order, Revibes uses the **Jetpack App Startup** library.

### Core Initialization (`KoinInitializer`)
In `:core`, Koin starts synchronously before anything else:
```kotlin
class KoinInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        LocalDataSource.maybeInitMMKV(context)
        startKoin {
            androidLogger()
            androidContext(context)
            modules(listOf(CoreModule.module()))
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> = emptyList()
}
```

### Feature Module Loading (`FeatureModuleInitializer`)
Each feature module lazily loads its own dependencies *after* the core Koin initialisation is complete:
```kotlin
class AuthModuleInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        loadKoinModules(AuthModule.module())
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return listOf(KoinInitializer::class.java)
    }
}
```
*Note: Make sure to register the initializers in the respective module's `AndroidManifest.xml`.*
