package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent

abstract class NavigationEventHandler {
    protected abstract val supportedEvents: Set<NavigationEvent>

    open fun canHandle(event: NavigationEvent): Boolean {
        return supportedEvents.contains(event)
    }

    abstract fun navigate(event: NavigationEvent)
}
