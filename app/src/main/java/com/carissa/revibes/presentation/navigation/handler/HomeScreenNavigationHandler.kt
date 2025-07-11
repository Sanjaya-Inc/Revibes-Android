package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home.presentation.screen.HomeScreenUiEvent
import com.carissa.revibes.profile.presentation.screen.ProfileScreenUiEvent
import com.ramcosta.composedestinations.generated.app.destinations.SplashScreenDestination
import com.ramcosta.composedestinations.generated.dropoff.destinations.DropOffScreenDestination
import com.ramcosta.composedestinations.generated.exchangepoints.destinations.ExchangePointsScreenDestination
import com.ramcosta.composedestinations.generated.home.destinations.AboutScreenDestination
import com.ramcosta.composedestinations.generated.home.destinations.HomeScreenDestination
import com.ramcosta.composedestinations.generated.profile.destinations.ProfileScreenDestination
import com.ramcosta.composedestinations.generated.shop.destinations.ShopScreenDestination
import com.ramcosta.composedestinations.generated.transactionhistory.destinations.TransactionHistoryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlin.reflect.KClass

class HomeScreenNavigationHandler(
    private val navigator: DestinationsNavigator
) : NavigationEventHandler() {

    override val supportedEvents: Set<KClass<out NavigationEvent>> = setOf(
        HomeScreenUiEvent.NavigateToProfile::class,
        ProfileScreenUiEvent.LogoutClicked::class,
        HomeScreenUiEvent.NavigateToShop::class,
        HomeScreenUiEvent.NavigateToExchangePoints::class,
        HomeScreenUiEvent.NavigateToTransactionHistory::class,
        HomeScreenUiEvent.NavigateToAboutUs::class,
        HomeScreenUiEvent.NavigateToDropOff::class,
        HomeScreenUiEvent.NavigateToLogin::class,
    )

    override fun navigate(event: NavigationEvent) {
        when (event) {
            is HomeScreenUiEvent.NavigateToProfile -> navigator.navigate(ProfileScreenDestination)
            is ProfileScreenUiEvent.LogoutClicked -> navigator.navigate(SplashScreenDestination) {
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
            is HomeScreenUiEvent.NavigateToLogin -> navigator.navigate(SplashScreenDestination) {
                popUpTo(HomeScreenDestination) { inclusive = true }
            }
        }
    }
}
