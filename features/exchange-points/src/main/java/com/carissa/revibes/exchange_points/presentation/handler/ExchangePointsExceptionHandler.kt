package com.carissa.revibes.exchange_points.presentation.handler

import android.util.Log
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointsScreenUiEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointsScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class ExchangePointsExceptionHandler {
    suspend fun onExchangePointsError(
        syntax: Syntax<ExchangePointsScreenUiState, ExchangePointsScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                error = throwable.message
            )
        }
        Log.e(TAG, "onExchangePointsError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "ExchangePointsExceptionHandler"
    }
}
