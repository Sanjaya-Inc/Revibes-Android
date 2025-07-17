package com.carissa.revibes.manage_transaction.presentation.handler

import android.util.Log
import com.carissa.revibes.manage_transaction.presentation.screen.ManageTransactionScreenUiEvent
import com.carissa.revibes.manage_transaction.presentation.screen.ManageTransactionScreenUiState
import com.carissa.revibes.manage_transaction.presentation.screen.TransactionDetailScreenUiEvent
import com.carissa.revibes.manage_transaction.presentation.screen.TransactionDetailScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class ManageTransactionExceptionHandler {
    suspend fun onGetTransactionsError(
        syntax: Syntax<ManageTransactionScreenUiState, ManageTransactionScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }
        Log.e(TAG, "onGetTransactionsError: ${throwable.message}", throwable)
    }

    suspend fun onGetTransactionDetailError(
        syntax: Syntax<TransactionDetailScreenUiState, TransactionDetailScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }
        Log.e(TAG, "onGetTransactionDetailError: ${throwable.message}", throwable)
    }

    suspend fun onTransactionActionError(
        syntax: Syntax<TransactionDetailScreenUiState, TransactionDetailScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isProcessing = false) }
        postSideEffect(
            TransactionDetailScreenUiEvent.OnTransactionActionFailed(
                throwable.message.orEmpty()
            )
        )
        Log.e(TAG, "onTransactionActionError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "ManageTransactionExceptionHandler"
    }
}
