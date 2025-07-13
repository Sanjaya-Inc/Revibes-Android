package com.carissa.revibes.transaction_history.presentation.handler

import android.util.Log
import com.carissa.revibes.transaction_history.presentation.screen.TransactionHistoryScreenUiEvent
import com.carissa.revibes.transaction_history.presentation.screen.TransactionHistoryScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class TransactionHistoryExceptionHandler {
    suspend fun onTransactionHistoryError(
        syntax: Syntax<TransactionHistoryScreenUiState, TransactionHistoryScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                isLoadingMore = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }

        Log.e(TAG, "onTransactionHistoryError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "TransactionHistoryExceptionHandler"
    }
}
