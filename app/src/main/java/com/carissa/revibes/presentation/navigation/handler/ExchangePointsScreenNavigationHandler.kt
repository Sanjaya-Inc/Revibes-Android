package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointsScreenUiEvent
import com.ramcosta.composedestinations.generated.exchangepoints.destinations.ExchangePointDetailScreenDestination
import com.ramcosta.composedestinations.generated.exchangepoints.destinations.UserVoucherDetailScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class ExchangePointsScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is ExchangePointsScreenUiEvent.NavigateToProfile ||
            event is ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint ||
            event is ExchangePointsScreenUiEvent.NavigateToUserVoucherDetail
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is ExchangePointsScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint -> {
                navigator.navigate(ExchangePointDetailScreenDestination(event.voucher))
            }
            is ExchangePointsScreenUiEvent.NavigateToUserVoucherDetail -> {
                navigator.navigate(UserVoucherDetailScreenDestination(event.userVoucher))
            }
        }
    }
}
