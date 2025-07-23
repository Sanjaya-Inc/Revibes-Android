package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointDetailScreenUiEvent
import com.ramcosta.composedestinations.generated.exchangepoints.destinations.ExchangePointConfirmationScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class ExchangePointDetailScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is ExchangePointDetailScreenUiEvent.NavigateToProfile ||
            event is ExchangePointDetailScreenUiEvent.NavigateToConfirmation
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is ExchangePointDetailScreenUiEvent.NavigateToProfile -> navigator.navigate(
                ProfileScreenDestination
            )

            is ExchangePointDetailScreenUiEvent.NavigateToConfirmation -> {
                navigator.navigate(ExchangePointConfirmationScreenDestination)
            }
        }
    }
}
