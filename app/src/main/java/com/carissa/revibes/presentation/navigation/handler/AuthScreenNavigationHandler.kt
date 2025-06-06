package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.onboarding.presentation.screen.onboarding.component.page.OnboardingPageUiEvent
import com.ramcosta.composedestinations.generated.auth.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.auth.destinations.RegisterScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class AuthScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<NavigationEvent> = setOf(
        OnboardingPageUiEvent.NavigateToLogin,
        OnboardingPageUiEvent.NavigateToRegister,
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is OnboardingPageUiEvent.NavigateToLogin -> navigator.navigate(LoginScreenDestination)
            is OnboardingPageUiEvent.NavigateToRegister -> navigator.navigate(RegisterScreenDestination)
        }
    }
}
