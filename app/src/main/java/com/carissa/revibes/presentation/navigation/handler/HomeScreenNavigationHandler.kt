package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home.presentation.screen.HomeScreenUiEvent
import com.carissa.revibes.profile.presentation.screen.ProfileScreenUiEvent
import com.ramcosta.composedestinations.generated.adminmenu.destinations.AdminMenuScreenDestination
import com.ramcosta.composedestinations.generated.app.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.generated.dropoff.destinations.DropOffScreenDestination
import com.ramcosta.composedestinations.generated.exchangepoints.destinations.ExchangePointsScreenDestination
import com.ramcosta.composedestinations.generated.helpcenter.destinations.HelpCenterScreenDestination
import com.ramcosta.composedestinations.generated.home.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.pickup.destinations.PickUpScreenDestination
import com.ramcosta.composedestinations.generated.point.destinations.PointScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.generated.shop.destinations.ShopScreenDestination
import com.ramcosta.composedestinations.generated.transactionhistory.destinations.TransactionHistoryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class HomeScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is HomeScreenUiEvent.NavigateToProfile ||
            event is ProfileScreenUiEvent.NavigateToLogin ||
            event is HomeScreenUiEvent.NavigateToShop ||
            event is HomeScreenUiEvent.NavigateToExchangePoints ||
            event is HomeScreenUiEvent.NavigateToTransactionHistory ||
            event is HomeScreenUiEvent.NavigateToAboutUs ||
            event is HomeScreenUiEvent.NavigateToDropOff ||
            event is HomeScreenUiEvent.NavigateToPickUp ||
            event is HomeScreenUiEvent.NavigateToPoint ||
            event is HomeScreenUiEvent.NavigateToHelpCenter ||
            event is HomeScreenUiEvent.NavigateToAdminMenu ||
            event is HomeScreenUiEvent.NavigateToLogin
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is HomeScreenUiEvent.NavigateToAdminMenu -> navigator.navigate(
                AdminMenuScreenDestination
            )

            is HomeScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is ProfileScreenUiEvent.NavigateToLogin -> navigator.navigate(SplashScreenDestination) {
                popUpTo(HomeScreenDestination) { inclusive = true }
            }

            is HomeScreenUiEvent.NavigateToShop -> navigator.navigate(ShopScreenDestination)
            is HomeScreenUiEvent.NavigateToExchangePoints -> navigator.navigate(
                ExchangePointsScreenDestination
            )

            is HomeScreenUiEvent.NavigateToTransactionHistory -> navigator.navigate(
                TransactionHistoryScreenDestination
            )

            is HomeScreenUiEvent.NavigateToAboutUs -> navigator.navigate(AboutScreenDestination)
            is HomeScreenUiEvent.NavigateToDropOff -> navigator.navigate(DropOffScreenDestination)
            is HomeScreenUiEvent.NavigateToPickUp -> navigator.navigate(PickUpScreenDestination)
            is HomeScreenUiEvent.NavigateToPoint -> navigator.navigate(PointScreenDestination)
            is HomeScreenUiEvent.NavigateToHelpCenter -> navigator.navigate(
                HelpCenterScreenDestination
            )

            is HomeScreenUiEvent.NavigateToLogin -> navigator.navigate(SplashScreenDestination) {
                popUpTo(HomeScreenDestination) { inclusive = true }
            }
        }
    }
}
