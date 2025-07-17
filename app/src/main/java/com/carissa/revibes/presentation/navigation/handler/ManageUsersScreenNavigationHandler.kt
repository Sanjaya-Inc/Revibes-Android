package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_users.presentation.screen.ManageUsersScreenUiEvent
import com.ramcosta.composedestinations.generated.manageusers.destinations.AddUserScreenDestination
import com.ramcosta.composedestinations.generated.manageusers.destinations.EditUserScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class ManageUsersScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is ManageUsersScreenUiEvent.NavigateToAddUser ||
            event is ManageUsersScreenUiEvent.NavigateToEditUser
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is ManageUsersScreenUiEvent.NavigateToAddUser -> navigator.navigate(
                AddUserScreenDestination
            )
            is ManageUsersScreenUiEvent.NavigateToEditUser -> navigator.navigate(
                EditUserScreenDestination(userId = event.userId)
            )
        }
    }
}
