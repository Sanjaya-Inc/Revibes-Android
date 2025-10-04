package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.pick_up.presentation.screen.PickUpScreenUiEvent
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class PickUpScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is PickUpScreenUiEvent.NavigateToProfile ||
            event is PickUpScreenUiEvent.NavigateToHome
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is PickUpScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is PickUpScreenUiEvent.NavigateToHome -> goToHome(navigator)
        }
    }
}
