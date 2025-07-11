package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent

abstract class NavigationEventHandler {
    abstract fun canHandle(event: NavigationEvent): Boolean

    abstract fun navigate(event: NavigationEvent)
}
