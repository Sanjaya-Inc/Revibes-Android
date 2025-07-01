package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointConfirmationScreenUiEvent
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class ExchangePointConfirmationScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<NavigationEvent> = setOf(
        ExchangePointConfirmationScreenUiEvent.NavigateToHome,
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ExchangePointConfirmationScreenUiEvent.NavigateToHome -> navigator.navigate(
                HomeScreenDestination
            )
        }
    }
}
