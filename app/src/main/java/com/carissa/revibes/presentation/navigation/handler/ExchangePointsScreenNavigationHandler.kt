package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointsScreenUiEvent
import com.ramcosta.composedestinations.generated.exchangepoints.destinations.ExchangePointDetailScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.reflect.KClass

class ExchangePointsScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<KClass<out NavigationEvent>> = setOf(
        ExchangePointsScreenUiEvent.NavigateToProfile::class,
        ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint::class
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ExchangePointsScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint -> {
                navigator.navigate(ExchangePointDetailScreenDestination)
            }
        }
    }
}
