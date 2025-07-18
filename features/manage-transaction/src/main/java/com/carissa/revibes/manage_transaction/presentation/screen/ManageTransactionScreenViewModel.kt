package com.carissa.revibes.manage_transaction.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_transaction.data.ManageTransactionRepository
import com.carissa.revibes.manage_transaction.domain.model.ManageTransactionDomain
import com.carissa.revibes.manage_transaction.presentation.handler.ManageTransactionExceptionHandler
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.android.annotation.KoinViewModel

sealed interface ManageTransactionScreenUiEvent {
    data class OnSearchQueryChanged(val query: String) : ManageTransactionScreenUiEvent
    data object RefreshTransactions : ManageTransactionScreenUiEvent
    data object LoadTransactions : ManageTransactionScreenUiEvent
    data class NavigateToDetail(val transactionId: String) :
        NavigationEvent,
        ManageTransactionScreenUiEvent

    data class OnLoadTransactionsFailed(val message: String) : ManageTransactionScreenUiEvent
}

data class ManageTransactionScreenUiState(
    val transactions: ImmutableList<ManageTransactionDomain> = persistentListOf(),
    val filteredTransactions: ImmutableList<ManageTransactionDomain> = persistentListOf(),
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)

@KoinViewModel
class ManageTransactionScreenViewModel internal constructor(
    private val repository: ManageTransactionRepository,
    private val exceptionHandler: ManageTransactionExceptionHandler
) : BaseViewModel<ManageTransactionScreenUiState, ManageTransactionScreenUiEvent>(
    initialState = ManageTransactionScreenUiState(),
    onCreate = {
        onEvent(ManageTransactionScreenUiEvent.LoadTransactions)
    },
    exceptionHandler = { syntax, throwable ->
        exceptionHandler.onGetTransactionsError(syntax, throwable)
    }
) {
    override fun onEvent(event: ManageTransactionScreenUiEvent) {
        println("ketai: ManageTransactionScreenViewModel.onEvent $event")
        when (event) {
            is ManageTransactionScreenUiEvent.OnSearchQueryChanged -> onSearchQueryChanged(event.query)
            is ManageTransactionScreenUiEvent.RefreshTransactions,
            is ManageTransactionScreenUiEvent.LoadTransactions -> loadTransactions()

            else -> super.onEvent(event)
        }
    }

    private fun onSearchQueryChanged(query: String) = intent {
        reduce {
            state.copy(
                searchQuery = query,
                filteredTransactions = filterTransactions(state.transactions, query)
            )
        }
    }

    private fun loadTransactions() = intent {
        reduce { state.copy(isLoading = true) }

        val transactions = repository.getPendingTransactions()

        reduce {
            state.copy(
                transactions = transactions.toImmutableList(),
                filteredTransactions = filterTransactions(
                    transactions = transactions.toImmutableList(),
                    query = state.searchQuery
                ),
                isLoading = false
            )
        }
    }

    private fun filterTransactions(
        transactions: ImmutableList<ManageTransactionDomain>,
        query: String
    ): ImmutableList<ManageTransactionDomain> {
        if (query.isBlank()) return transactions

        return transactions.filter { transaction ->
            transaction.name.contains(query, ignoreCase = true) ||
                transaction.address.contains(query, ignoreCase = true) ||
                transaction.maker.contains(query, ignoreCase = true) ||
                transaction.id.contains(query, ignoreCase = true)
        }.toImmutableList()
    }
}
