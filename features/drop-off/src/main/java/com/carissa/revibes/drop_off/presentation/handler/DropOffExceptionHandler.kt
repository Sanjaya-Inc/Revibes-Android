package com.carissa.revibes.drop_off.presentation.handler

import android.util.Log
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.domain.utils.GeneralErrorMapper
import com.carissa.revibes.drop_off.presentation.screen.DropOffConfirmationScreenUiEvent
import com.carissa.revibes.drop_off.presentation.screen.DropOffConfirmationScreenUiState
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
        reduce { state.copy(isLoading = false) }
        val message = (throwable as? ApiException)?.let(generalErrorMapper::mapError)?.message
            ?: throwable.message
        postSideEffect(DropOffConfirmationScreenUiEvent.OnMakeOrderFailed(message.orEmpty()))
        Log.e(TAG, "onDropOffError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "DropOffExceptionHandler"
    }
}
