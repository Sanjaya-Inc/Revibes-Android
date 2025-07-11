package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.shop.presentation.screen.ShopScreenUiEvent
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

class ShopScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is ShopScreenUiEvent.NavigateToProfile
    }

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is ShopScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
        }
    }
}
