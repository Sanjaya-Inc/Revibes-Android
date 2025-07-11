package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointConfirmationScreenUiEvent
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.reflect.KClass

class ExchangePointConfirmationScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<KClass<out NavigationEvent>> = setOf(
        ExchangePointConfirmationScreenUiEvent.NavigateToHome::class,
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ExchangePointConfirmationScreenUiEvent.NavigateToHome -> navigator.navigate(
                HomeScreenDestination
            )
        }
    }
}
