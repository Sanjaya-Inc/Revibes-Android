package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

abstract class NavigationEventHandler {
    abstract fun canHandle(event: NavigationEvent): Boolean

    abstract fun navigate(navigator: DestinationsNavigator, event: NavigationEvent)
}
