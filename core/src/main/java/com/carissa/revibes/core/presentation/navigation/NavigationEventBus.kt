package com.carissa.revibes.core.presentation.navigation

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalForInheritanceCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

interface NavigationEvent

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
interface NavigationEventBus : SharedFlow<NavigationEvent> {
    fun post(event: NavigationEvent): Job
}

@OptIn(ExperimentalForInheritanceCoroutinesApi::class)
@Single
internal class NavigationEventBusImpl(
    private val _flow: MutableSharedFlow<NavigationEvent> = MutableSharedFlow(),
    private val dispatcher: CoroutineDispatcher = Dispatchers.Main
) : NavigationEventBus,
    SharedFlow<NavigationEvent> by _flow,
    CoroutineScope by CoroutineScope(dispatcher) {
    override fun post(event: NavigationEvent) = launch {
        _flow.emit(event)
    }
}
