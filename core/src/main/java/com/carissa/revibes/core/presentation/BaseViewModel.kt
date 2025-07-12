package com.carissa.revibes.core.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import kotlinx.coroutines.CoroutineExceptionHandler
import org.koin.java.KoinJavaComponent
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.Syntax
import org.orbitmvi.orbit.viewmodel.container

@Stable
fun interface EventReceiver<Event : Any> {
    fun onEvent(event: Event)
}

abstract class BaseViewModel<State : Any, Event : Any>(
    initialState: State,
    private val navigationEventBus: NavigationEventBus = KoinJavaComponent.get(NavigationEventBus::class.java),
    exceptionHandler: (suspend EventReceiver<Event>.(Syntax<State, Event>, Throwable) -> Unit)? = null,
    onCreate: (suspend EventReceiver<Event>.(Syntax<State, Event>) -> Unit)? = null
) : ContainerHost<State, Event>, EventReceiver<Event>, ViewModel() {

    override val container: Container<State, Event> =
        container(
            initialState = initialState,
            buildSettings = {
                if (exceptionHandler != null) {
                    this.exceptionHandler = CoroutineExceptionHandler { _, exception ->
                        intent { exceptionHandler(this@BaseViewModel, this, exception) }
                    }
                }
            },
            onCreate = {
                intent { onCreate?.invoke(this@BaseViewModel, this) }
            }
        )

    override fun onEvent(event: Event) {
        if (event is NavigationEvent) navigationEventBus.post(event)
    }
}
