package com.carissa.revibes.presentation.navigation.handler

import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_transaction.presentation.screen.ManageTransactionScreenUiEvent
import com.ramcosta.composedestinations.generated.managetransaction.destinations.TransactionDetailScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.core.annotation.Factory

@Factory
class ManageTransactionScreenNavigationHandler : NavigationEventHandler() {

    override fun canHandle(event: NavigationEvent): Boolean {
        return event is ManageTransactionScreenUiEvent.NavigateToDetail
    }

    override fun navigate(navigator: DestinationsNavigator, event: NavigationEvent) {
        when (event) {
            is ManageTransactionScreenUiEvent.NavigateToDetail -> navigator.navigate(
                TransactionDetailScreenDestination(event.transactionId)
            )
        }
    }
}
