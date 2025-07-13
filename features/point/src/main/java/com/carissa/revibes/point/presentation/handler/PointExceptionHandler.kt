package com.carissa.revibes.point.presentation.handler

import android.util.Log
import com.carissa.revibes.point.presentation.screen.PointScreenUiEvent
import com.carissa.revibes.point.presentation.screen.PointScreenUiState
import io.ktor.client.plugins.ClientRequestException
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class PointExceptionHandler {
    suspend fun onPointError(
        syntax: Syntax<PointScreenUiState, PointScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        val isClaimingReward = state.isClaimingReward
        reduce {
            state.copy(
                isLoading = false,
                isClaimingReward = false
            )
        }

        val errorEvent = if (isClaimingReward) {
            if (throwable is ClientRequestException && throwable.response.status.value == 403) {
                reduce { state.copy(allowedToClaimReward = false) }
                PointScreenUiEvent.OnClaimDailyRewardFailed("You have already claimed today's reward.")
            } else {
                PointScreenUiEvent.OnClaimDailyRewardFailed(throwable.message.orEmpty())
            }
        } else {
            PointScreenUiEvent.OnLoadDailyRewardsFailed(throwable.message.orEmpty())
        }

        postSideEffect(errorEvent)
        Log.e(TAG, "onPointError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "PointExceptionHandler"
    }
}
