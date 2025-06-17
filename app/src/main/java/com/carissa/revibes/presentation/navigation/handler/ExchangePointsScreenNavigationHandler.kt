package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointsScreenUiEvent
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class ExchangePointsScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<NavigationEvent> = setOf(
        ExchangePointsScreenUiEvent.NavigateToProfile,
        ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ExchangePointsScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint -> {
                // TODO: Navigate to detail exchange point screen when implemented
            }
        }
    }
}
