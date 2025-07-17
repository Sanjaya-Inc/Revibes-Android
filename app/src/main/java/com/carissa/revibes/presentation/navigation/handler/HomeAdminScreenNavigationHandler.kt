package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home_admin.presentation.screen.HomeAdminScreenUiEvent
import com.ramcosta.composedestinations.generated.manageusers.destinations.ManageUsersScreenDestination
import com.ramcosta.composedestinations.generated.managevoucher.destinations.ManageVoucherScreenDestination
import com.ramcosta.composedestinations.generated.managetransaction.destinations.ManageTransactionScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class HomeAdminScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is HomeAdminScreenUiEvent.NavigateToManageUsers ||
            event is HomeAdminScreenUiEvent.NavigateToManageVouchers ||
            event is HomeAdminScreenUiEvent.NavigateToManageTransactions ||
            event is HomeAdminScreenUiEvent.NavigateToProfile
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is HomeAdminScreenUiEvent.NavigateToManageUsers -> navigator.navigate(
                ManageUsersScreenDestination
            )
            is HomeAdminScreenUiEvent.NavigateToManageVouchers -> navigator.navigate(
                ManageVoucherScreenDestination
            )
            is HomeAdminScreenUiEvent.NavigateToManageTransactions -> navigator.navigate(
                ManageTransactionScreenDestination
            )
            is HomeAdminScreenUiEvent.NavigateToProfile -> navigator.navigate(
                ProfileScreenDestination
            )
        }
    }
}
