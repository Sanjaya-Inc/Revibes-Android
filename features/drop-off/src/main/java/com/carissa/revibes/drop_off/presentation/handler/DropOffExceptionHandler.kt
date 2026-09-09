package com.carissa.revibes.drop_off.presentation.handler

import android.util.Log
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.domain.utils.GeneralErrorMapper
import com.carissa.revibes.drop_off.presentation.screen.DropOffConfirmationScreenUiEvent
import com.carissa.revibes.drop_off.presentation.screen.DropOffConfirmationScreenUiState
import com.carissa.revibes.drop_off.presentation.screen.DropOffScreenUiEvent
import com.carissa.revibes.drop_off.presentation.screen.DropOffScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class DropOffExceptionHandler(
    private val generalErrorMapper: GeneralErrorMapper
) {
    suspend fun onDropOffError(
        syntax: Syntax<DropOffConfirmationScreenUiState, DropOffConfirmationScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false, isEstimatingPoints = false) }
        postSideEffect(DropOffConfirmationScreenUiEvent.OnMakeOrderFailed(resolveMessage(throwable)))
        Log.e(TAG, "onDropOffError: ${throwable.message}", throwable)
    }

    suspend fun onDropOffFormError(
        syntax: Syntax<DropOffScreenUiState, DropOffScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        val message = resolveMessage(throwable)
        val isInitialLoad = state.currentOrderId == null
        reduce {
            state.copy(
                isLoading = false,
                isAddingItem = false,
                uploadingItemIndex = null,
                error = if (isInitialLoad) message else state.error
            )
        }
        if (!isInitialLoad) {
            postSideEffect(DropOffScreenUiEvent.OnError(message))
        }
        Log.e(TAG, "onDropOffFormError: ${throwable.message}", throwable)
    }

    private fun resolveMessage(throwable: Throwable): String {
        return (throwable as? ApiException)?.let(generalErrorMapper::mapError)?.message
            ?: throwable.message
            ?: ""
    }

    companion object {
        private const val TAG = "DropOffExceptionHandler"
    }
}
