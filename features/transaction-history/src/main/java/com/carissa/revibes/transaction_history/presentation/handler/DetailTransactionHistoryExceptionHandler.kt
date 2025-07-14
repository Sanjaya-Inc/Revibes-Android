package com.carissa.revibes.transaction_history.presentation.handler

import android.util.Log
import com.carissa.revibes.transaction_history.presentation.screen.DetailTransactionHistoryScreenUiEvent
import com.carissa.revibes.transaction_history.presentation.screen.DetailTransactionHistoryScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class DetailTransactionHistoryExceptionHandler {
    suspend fun onDetailTransactionHistoryError(
        syntax: Syntax<DetailTransactionHistoryScreenUiState, DetailTransactionHistoryScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }

        Log.e(TAG, "onDetailTransactionHistoryError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "DetailTransactionHistoryExceptionHandler"
    }
}
