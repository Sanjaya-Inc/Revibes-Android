package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_voucher.presentation.screen.ManageVoucherScreenUiEvent
import com.ramcosta.composedestinations.generated.managevoucher.destinations.AddVoucherScreenDestination
import com.ramcosta.composedestinations.generated.managevoucher.destinations.EditVoucherScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class ManageVoucherScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is ManageVoucherScreenUiEvent.NavigateToAddVoucher ||
            event is ManageVoucherScreenUiEvent.NavigateToEditVoucher
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is ManageVoucherScreenUiEvent.NavigateToAddVoucher -> navigator.navigate(
                AddVoucherScreenDestination
            )
            is ManageVoucherScreenUiEvent.NavigateToEditVoucher -> navigator.navigate(
                EditVoucherScreenDestination(voucherId = event.voucherId)
            )
        }
    }
}
