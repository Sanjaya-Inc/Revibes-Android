/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.transaction_history.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.transaction_history.data.TransactionHistoryRepository
import com.carissa.revibes.transaction_history.data.model.PaginationData
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData
import com.carissa.revibes.transaction_history.presentation.handler.TransactionHistoryExceptionHandler
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import org.koin.android.annotation.KoinViewModel

data class TransactionHistoryScreenUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val transactions: PersistentList<TransactionHistoryData> = persistentListOf(),
    val filteredTransactions: PersistentList<TransactionHistoryData> = persistentListOf(),
    val pagination: PaginationData? = null,
    val error: String? = null
)

sealed interface TransactionHistoryScreenUiEvent : NavigationEvent {
    data class NavigateToTransactionDetail(val transactionId: String) :
        TransactionHistoryScreenUiEvent

    data object Initialize : TransactionHistoryScreenUiEvent
    data object LoadMore : TransactionHistoryScreenUiEvent
    data object Refresh : TransactionHistoryScreenUiEvent
    data class OnLoadTransactionHistoryFailed(val message: String) : TransactionHistoryScreenUiEvent
    data class SearchValueChanged(val searchValue: TextFieldValue) : TransactionHistoryScreenUiEvent
}

@KoinViewModel
class TransactionHistoryScreenViewModel(
    private val repository: TransactionHistoryRepository,
    private val exceptionHandler: TransactionHistoryExceptionHandler
) : BaseViewModel<TransactionHistoryScreenUiState, TransactionHistoryScreenUiEvent>(
    initialState = TransactionHistoryScreenUiState(),
    onCreate = { onEvent(TransactionHistoryScreenUiEvent.Initialize) },
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onTransactionHistoryError(syntax, exception)
    }
) {

    override fun onEvent(event: TransactionHistoryScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            TransactionHistoryScreenUiEvent.Initialize -> loadTransactionHistory()
            TransactionHistoryScreenUiEvent.LoadMore -> loadMoreTransactions()
            TransactionHistoryScreenUiEvent.Refresh -> onRefresh()
            is TransactionHistoryScreenUiEvent.SearchValueChanged -> onSearchValueChanged(event.searchValue)
            else -> Unit
        }
    }

    private fun loadTransactionHistory(refresh: Boolean = false) = intent {
        if (refresh) {
            reduce { state.copy(isLoading = true, error = null) }
        } else if (state.transactions.isEmpty()) {
            reduce { state.copy(isLoading = true, error = null) }
        } else {
            reduce { state.copy(isLoadingMore = true, error = null) }
        }

        val lastDocId = if (refresh) null else state.pagination?.lastDocId
        val result = repository.getTransactionHistory(
            lastDocId = lastDocId,
            direction = "next"
        )

        val newTransactions = if (refresh) {
            result.items.toPersistentList()
        } else {
            (state.transactions + result.items).toPersistentList()
        }

        reduce {
            state.copy(
                isLoading = false,
                isLoadingMore = false,
                transactions = newTransactions,
                filteredTransactions = filterTransactions(newTransactions, state.searchValue.text),
                pagination = result.pagination,
                error = null
            )
        }
    }

    private fun loadMoreTransactions() = intent {
        if (state.pagination?.hasMoreNext == true && !state.isLoadingMore) {
            loadTransactionHistory(refresh = false)
        }
    }

    private fun onSearchValueChanged(searchValue: TextFieldValue) = intent {
        val filteredTransactions = filterTransactions(state.transactions, searchValue.text)
        reduce {
            state.copy(
                searchValue = searchValue,
                filteredTransactions = filteredTransactions
            )
        }
    }

    private fun onRefresh() {
        loadTransactionHistory(refresh = true)
    }

    private fun filterTransactions(
        transactions: PersistentList<TransactionHistoryData>,
        searchQuery: String
    ): PersistentList<TransactionHistoryData> {
        return if (searchQuery.isBlank()) {
            transactions
        } else {
            transactions.filter { transaction ->
                transaction.title.contains(searchQuery, ignoreCase = true) ||
                    transaction.validatorName.contains(searchQuery, ignoreCase = true) ||
                    transaction.location.contains(searchQuery, ignoreCase = true) ||
                    transaction.items.any { it.contains(searchQuery, ignoreCase = true) }
            }.toPersistentList()
        }
    }
}
