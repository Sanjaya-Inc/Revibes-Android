package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.admin_menu.presentation.screen.AdminMenuScreenUiEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class AdminMenuScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is AdminMenuScreenUiEvent.NavigateToProfile
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is AdminMenuScreenUiEvent.NavigateToProfile -> navigator.navigate(
                ProfileScreenDestination
            )
        }
    }
}
