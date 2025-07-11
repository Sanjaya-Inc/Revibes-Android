package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointDetailScreenUiEvent
import com.ramcosta.composedestinations.generated.exchangepoints.destinations.ExchangePointConfirmationScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.reflect.KClass

class ExchangePointDetailScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<KClass<out NavigationEvent>> = setOf(
        ExchangePointDetailScreenUiEvent.NavigateToProfile::class,
        ExchangePointDetailScreenUiEvent.ConfirmPurchase::class
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ExchangePointDetailScreenUiEvent.NavigateToProfile -> navigator.navigate(
                ProfileScreenDestination
            )

            is ExchangePointDetailScreenUiEvent.ConfirmPurchase -> {
                println("ketai: ExchangePointDetailScreenNavigationHandler")
                navigator.navigate(ExchangePointConfirmationScreenDestination)
            }
        }
    }
}
