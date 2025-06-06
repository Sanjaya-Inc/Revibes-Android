package com.carissa.revibes.core.presentation

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineExceptionHandler
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
    exceptionHandler: (suspend Syntax<State, Event>.(Throwable) -> Unit)? = null,
    onCreate: (suspend Syntax<State, Event>.() -> Unit)? = null
) : ContainerHost<State, Event>, EventReceiver<Event>, ViewModel() {

    override val container: Container<State, Event> =
        container(
            initialState = initialState,
            buildSettings = {
                if (exceptionHandler != null) {
                    this.exceptionHandler = CoroutineExceptionHandler { _, exception ->
                        intent { exceptionHandler(this, exception) }
                    }
                }
            },
            onCreate = {
                intent { onCreate?.invoke(this) }
            }
        )

    override fun onEvent(event: Event) = Unit
}
