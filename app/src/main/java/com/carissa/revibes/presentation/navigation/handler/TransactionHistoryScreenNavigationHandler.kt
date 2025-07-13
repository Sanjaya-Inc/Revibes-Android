package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.transaction_history.presentation.screen.TransactionHistoryScreenUiEvent
import com.ramcosta.composedestinations.generated.transactionhistory.destinations.DetailTransactionHistoryScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class TransactionHistoryScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is TransactionHistoryScreenUiEvent.NavigateToTransactionDetail
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is TransactionHistoryScreenUiEvent.NavigateToTransactionDetail -> navigator.navigate(
                direction = DetailTransactionHistoryScreenDestination(
                    transactionId = event.transactionId
                )
            )
        }
    }
}
