package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.compose.components.ToolbarEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class ToolbarNavigationHandler : NavigationEventHandler() {
    override fun canHandle(event: NavigationEvent): Boolean {
        return event is ToolbarEvent
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is ToolbarEvent.NavigateBack -> navigator.navigateUp()
            is ToolbarEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
        }
    }
}
