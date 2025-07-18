package com.carissa.revibes.manage_transaction.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.manage_transaction.data.ManageTransactionRepository
import com.carissa.revibes.manage_transaction.domain.model.TransactionDetailDomain
import com.carissa.revibes.manage_transaction.presentation.handler.ManageTransactionExceptionHandler
import org.koin.android.annotation.KoinViewModel

sealed interface TransactionDetailScreenUiEvent {
    data class LoadTransactionDetail(val transactionId: String) : TransactionDetailScreenUiEvent
    data class RejectTransaction(val reason: String) : TransactionDetailScreenUiEvent
    data object CompleteTransaction : TransactionDetailScreenUiEvent
    data class OnTransactionActionFailed(val message: String) : TransactionDetailScreenUiEvent
}

data class TransactionDetailScreenUiState(
    val transactionDetail: TransactionDetailDomain? = null,
    val isLoading: Boolean = false,
    val isProcessing: Boolean = false,
    val isRejecting: Boolean = false,
    val actionCompleted: Boolean = false,
    val actionMessage: String? = null,
    val error: String? = null
)

@KoinViewModel
class TransactionDetailScreenViewModel internal constructor(
    private val repository: ManageTransactionRepository,
    private val exceptionHandler: ManageTransactionExceptionHandler
) : BaseViewModel<TransactionDetailScreenUiState, TransactionDetailScreenUiEvent>(
    initialState = TransactionDetailScreenUiState(),
    exceptionHandler = { syntax, throwable ->
        exceptionHandler.onGetTransactionDetailError(syntax, throwable)
    }
) {
    override fun onEvent(event: TransactionDetailScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is TransactionDetailScreenUiEvent.LoadTransactionDetail -> {
                loadTransactionDetail(event.transactionId)
            }

            is TransactionDetailScreenUiEvent.RejectTransaction -> rejectTransaction(event.reason)
            is TransactionDetailScreenUiEvent.CompleteTransaction -> completeTransaction()
            else -> Unit
        }
    }

    private fun rejectTransaction(reason: String) = intent {
        reduce { state.copy(isRejecting = true) }

        repository.rejectTransaction(state.transactionDetail!!.id, reason)

        reduce {
            state.copy(
                isRejecting = false,
                actionCompleted = true,
                actionMessage = "Transaction rejected successfully"
            )
        }
    }

    private fun completeTransaction() = intent {
        reduce { state.copy(isProcessing = true) }

        repository.completeTransaction(state.transactionDetail!!.id)

        reduce {
            state.copy(
                isProcessing = false,
                actionCompleted = true,
                actionMessage = "Transaction completed successfully"
            )
        }
    }

    private fun loadTransactionDetail(transactionId: String) = intent {
        reduce { state.copy(isLoading = true) }

        val transactionDetail = repository.getTransactionDetail(transactionId)

        reduce {
            state.copy(
                transactionDetail = transactionDetail,
                isLoading = false
            )
        }
    }
}
