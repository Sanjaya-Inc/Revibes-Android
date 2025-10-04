package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.onboarding.presentation.screen.onboarding.component.page.OnboardingPageUiEvent
import com.ramcosta.composedestinations.generated.auth.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.auth.destinations.RegisterScreenDestination
import com.ramcosta.composedestinations.generated.homeadmin.destinations.HomeAdminScreenDestination
import com.ramcosta.composedestinations.generated.onboarding.destinations.OnboardingScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class AuthScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is OnboardingPageUiEvent.NavigateToLogin ||
            event is OnboardingPageUiEvent.NavigateToRegister ||
            event is LoginScreenUiEvent.NavigateToHome ||
            event is LoginScreenUiEvent.NavigateToAdminHome
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is OnboardingPageUiEvent.NavigateToLogin -> navigator.navigate(LoginScreenDestination)
            is OnboardingPageUiEvent.NavigateToRegister -> navigator.navigate(
                RegisterScreenDestination
            )

            is LoginScreenUiEvent.NavigateToHome -> goToHome(navigator, OnboardingScreenDestination)
            is LoginScreenUiEvent.NavigateToAdminHome -> navigator.navigate(
                HomeAdminScreenDestination
            ) {
                popUpTo(OnboardingScreenDestination) { inclusive = true }
            }
        }
    }
}
