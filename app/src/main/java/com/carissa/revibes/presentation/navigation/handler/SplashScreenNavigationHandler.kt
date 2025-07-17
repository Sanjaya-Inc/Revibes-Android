package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.presentation.screen.splash.SplashScreenUiEvent
import com.ramcosta.composedestinations.generated.app.navgraphs.RevibesGraph
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.homeadmin.destinations.HomeAdminScreenDestination
import com.ramcosta.composedestinations.generated.onboarding.destinations.OnboardingScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class SplashScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is SplashScreenUiEvent.NavigateToOnboarding ||
            event is SplashScreenUiEvent.NavigateToHome ||
            event is SplashScreenUiEvent.NavigateToAdminHome
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is SplashScreenUiEvent.NavigateToOnboarding -> navigator.navigate(
                OnboardingScreenDestination
            ) {
                popUpTo(RevibesGraph.startRoute) {
                    inclusive = true
                }
            }

            is SplashScreenUiEvent.NavigateToHome -> navigator.navigate(HomeScreenDestination) {
                popUpTo(RevibesGraph.startRoute) {
                    inclusive = true
                }
            }

            is SplashScreenUiEvent.NavigateToAdminHome -> navigator.navigate(HomeAdminScreenDestination) {
                popUpTo(RevibesGraph.startRoute) {
                    inclusive = true
                }
            }
        }
    }
}
