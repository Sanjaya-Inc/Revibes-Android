package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import kotlin.reflect.KClass

abstract class NavigationEventHandler {
    protected abstract val supportedEvents: Set<KClass<out NavigationEvent>>

    open fun canHandle(event: NavigationEvent): Boolean {
        return supportedEvents.contains(event::class)
    }

    abstract fun navigate(event: NavigationEvent)
}
