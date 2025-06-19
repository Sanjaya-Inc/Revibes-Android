package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointDetailScreenUiEvent
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class ExchangePointDetailScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<NavigationEvent> = setOf(
        ExchangePointDetailScreenUiEvent.NavigateToProfile,
        ExchangePointDetailScreenUiEvent.BuyCoupon
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ExchangePointDetailScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is ExchangePointDetailScreenUiEvent.BuyCoupon -> {
                // TODO: Open order confirmation screen after bottom sheet's main button is clicked
                navigator.navigateUp()
            }
        }
    }
}
