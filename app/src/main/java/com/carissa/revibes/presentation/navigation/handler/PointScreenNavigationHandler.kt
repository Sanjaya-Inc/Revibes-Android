package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.point.presentation.screen.PointScreenUiEvent
import com.ramcosta.composedestinations.generated.point.destinations.DailyCheckInNewsScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class PointScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is PointScreenUiEvent.NavigateToProfile ||
            event is PointScreenUiEvent.NavigateToDailyCheckInNews
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is PointScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is PointScreenUiEvent.NavigateToDailyCheckInNews -> navigator.navigate(DailyCheckInNewsScreenDestination)
        }
    }
}
