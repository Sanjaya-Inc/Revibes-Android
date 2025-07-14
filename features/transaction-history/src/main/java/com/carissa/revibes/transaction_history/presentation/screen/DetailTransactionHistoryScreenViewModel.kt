/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.transaction_history.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.transaction_history.data.TransactionHistoryRepository
import com.carissa.revibes.transaction_history.domain.model.TransactionDetailDomain
import com.carissa.revibes.transaction_history.presentation.handler.DetailTransactionHistoryExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class DetailTransactionHistoryScreenUiState(
    val isLoading: Boolean = false,
    val transactionDetail: TransactionDetailDomain? = null,
    val error: String? = null
)

sealed interface DetailTransactionHistoryScreenUiEvent {
    data class LoadTransactionDetail(val transactionId: String) :
        DetailTransactionHistoryScreenUiEvent
}

@KoinViewModel
class DetailTransactionHistoryScreenViewModel(
    private val repository: TransactionHistoryRepository,
    private val exceptionHandler: DetailTransactionHistoryExceptionHandler
) : BaseViewModel<DetailTransactionHistoryScreenUiState, DetailTransactionHistoryScreenUiEvent>(
    initialState = DetailTransactionHistoryScreenUiState(),
    exceptionHandler = { syntax, exception ->
        exceptionHandler.onDetailTransactionHistoryError(syntax, exception)
    }
) {
    override fun onEvent(event: DetailTransactionHistoryScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is DetailTransactionHistoryScreenUiEvent.LoadTransactionDetail -> {
                loadTransactionDetail(event.transactionId)
            }
        }
    }

    private fun loadTransactionDetail(transactionId: String) {
        intent {
            reduce { state.copy(isLoading = true, error = null) }
            val detail = repository.getTransactionDetail(transactionId)
            reduce {
                state.copy(
                    isLoading = false,
                    transactionDetail = detail,
                    error = null
                )
            }
        }
    }
}
