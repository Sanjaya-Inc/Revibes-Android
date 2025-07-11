package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.shop.presentation.screen.ShopScreenUiEvent
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.reflect.KClass

class ShopScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<KClass<out NavigationEvent>> = setOf(
        ShopScreenUiEvent.NavigateToProfile::class
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ShopScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
        }
    }
}
