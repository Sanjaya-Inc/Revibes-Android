package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.drop_off.presentation.screen.DropOffConfirmationScreenUiEvent
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class DropOffConfirmationScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is DropOffConfirmationScreenUiEvent.NavigateToHome
    }

    override fun navigate(
        navigator: DestinationsNavigator,
        event: NavigationEvent
    ) {
        when (event) {
            is DropOffConfirmationScreenUiEvent.NavigateToHome -> goToHome(navigator)
        }
    }
}
