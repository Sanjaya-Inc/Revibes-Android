package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home_admin.presentation.screen.HomeAdminScreenUiEvent
import com.carissa.revibes.home_admin.presentation.screen.ManageDropOffPointsScreenUiEvent
import com.ramcosta.composedestinations.generated.homeadmin.destinations.AddDropOffPointScreenDestination
import com.ramcosta.composedestinations.generated.homeadmin.destinations.ManageDropOffPointsScreenDestination
import com.ramcosta.composedestinations.generated.homeadmin.destinations.ManageNewsScreenDestination
import com.ramcosta.composedestinations.generated.manageclaimedvouchers.destinations.ManageClaimedVouchersScreenDestination
import com.ramcosta.composedestinations.generated.managetransaction.destinations.ManageTransactionScreenDestination
import com.ramcosta.composedestinations.generated.manageusers.destinations.ManageUsersScreenDestination
import com.ramcosta.composedestinations.generated.managevoucher.destinations.ManageVoucherScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class HomeAdminScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is HomeAdminScreenUiEvent.NavigateToManageUsers ||
            event is HomeAdminScreenUiEvent.NavigateToManageVouchers ||
            event is HomeAdminScreenUiEvent.NavigateToManageTransactions ||
            event is HomeAdminScreenUiEvent.NavigateToClaimedVouchers ||
            event is HomeAdminScreenUiEvent.NavigateToManageNews ||
            event is HomeAdminScreenUiEvent.NavigateToManageDropOffPoints ||
            event is ManageDropOffPointsScreenUiEvent.NavigateToAddDropOffPoint ||
            event is ManageDropOffPointsScreenUiEvent.NavigateToEditDropOffPoint ||
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
            is HomeAdminScreenUiEvent.NavigateToClaimedVouchers -> navigator.navigate(
                ManageClaimedVouchersScreenDestination
            )
            is HomeAdminScreenUiEvent.NavigateToManageNews -> navigator.navigate(
                ManageNewsScreenDestination
            )
            is HomeAdminScreenUiEvent.NavigateToManageDropOffPoints -> navigator.navigate(
                ManageDropOffPointsScreenDestination
            )
            is ManageDropOffPointsScreenUiEvent.NavigateToAddDropOffPoint -> navigator.navigate(
                AddDropOffPointScreenDestination()
            )
            is ManageDropOffPointsScreenUiEvent.NavigateToEditDropOffPoint -> navigator.navigate(
                AddDropOffPointScreenDestination(store = event.store)
            )
            is HomeAdminScreenUiEvent.NavigateToProfile -> navigator.navigate(
                ProfileScreenDestination
            )
        }
    }
}
