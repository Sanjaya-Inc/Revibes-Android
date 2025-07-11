package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.drop_off.presentation.screen.DropOffScreenUiEvent
import com.ramcosta.composedestinations.generated.app.navgraphs.RevibesGraph
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class DropOffScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is DropOffScreenUiEvent.NavigateToProfile ||
            event is DropOffScreenUiEvent.NavigateToHome
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is DropOffScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is DropOffScreenUiEvent.NavigateToHome -> navigator.navigate(HomeScreenDestination) {
                popUpTo(RevibesGraph.startRoute) {
                    inclusive = true
                }
            }
        }
    }
}
