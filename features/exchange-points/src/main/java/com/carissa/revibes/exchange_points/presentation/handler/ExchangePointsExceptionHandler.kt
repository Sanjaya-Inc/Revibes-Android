package com.carissa.revibes.exchange_points.presentation.handler

import android.util.Log
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointDetailScreenUiEvent
import com.carissa.revibes.exchange_points.presentation.screen.ExchangePointDetailScreenUiState
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

    suspend fun onExchangePointsDetailError(
        syntax: Syntax<ExchangePointDetailScreenUiState, ExchangePointDetailScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }

        val message = when {
            throwable is ApiException && throwable.statusCode == 400 -> {
                "Oops! Looks like you've already claimed your share of this awesome voucher! 🎉 "
            }
            else -> throwable.message ?: "An error occurred"
        }

        postSideEffect(ExchangePointDetailScreenUiEvent.ShowToast(message))
        Log.e(TAG, "onExchangePointsDetailError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "ExchangePointsExceptionHandler"
    }
}
