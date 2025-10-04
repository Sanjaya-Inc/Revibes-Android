package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.Route

abstract class NavigationEventHandler {
    abstract fun canHandle(event: NavigationEvent): Boolean

    abstract fun navigate(navigator: DestinationsNavigator, event: NavigationEvent)

    fun goToHome(navigator: DestinationsNavigator, popUpTo: Route = HomeScreenDestination) {
        navigator.navigate(HomeScreenDestination) {
            launchSingleTop = true
            restoreState = true
            popUpTo(popUpTo) {
                inclusive = true
            }
        }
    }
}
