package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.drop_off.presentation.screen.DropOffConfirmationScreenArguments
import com.carissa.revibes.drop_off.presentation.screen.DropOffScreenUiEvent
import com.ramcosta.composedestinations.generated.dropoff.destinations.DropOffConfirmationScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class DropOffScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is DropOffScreenUiEvent.NavigateToProfile ||
            event is DropOffScreenUiEvent.NavigateToConfirmOrder
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is DropOffScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is DropOffScreenUiEvent.NavigateToConfirmOrder -> navigator.navigate(
                direction = DropOffConfirmationScreenDestination(
                    arguments = DropOffConfirmationScreenArguments(
                        orderId = event.arguments.orderId,
                        type = event.arguments.type,
                        name = event.arguments.name,
                        store = event.arguments.store,
                        totalPoints = event.arguments.totalPoints,
                        items = event.arguments.items
                    )
                )
            )
        }
    }
}
