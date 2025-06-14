package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home.presentation.screen.HomeScreenUiEvent
import com.carissa.revibes.profile.presentation.screen.ProfileScreenUiEvent
import com.ramcosta.composedestinations.generated.app.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class HomeScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<NavigationEvent> = setOf(
        HomeScreenUiEvent.NavigateToProfile,
        ProfileScreenUiEvent.LogoutClicked
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is HomeScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is ProfileScreenUiEvent.LogoutClicked -> navigator.navigate(SplashScreenDestination) {
                popUpTo(HomeScreenDestination) { inclusive = true }
            }
        }
    }
}
