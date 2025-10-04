package com.carissa.revibes.point.presentation.handler

import android.util.Log
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.domain.utils.GeneralErrorMapper
import com.carissa.revibes.point.presentation.screen.PointScreenUiEvent
import com.carissa.revibes.point.presentation.screen.PointScreenUiState
import io.ktor.client.plugins.ClientRequestException
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class PointExceptionHandler(
    private val generalErrorMapper: GeneralErrorMapper
) {
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
                PointScreenUiEvent.OnClaimDailyRewardFailed(getMessage(true, throwable))
            } else {
                PointScreenUiEvent.OnClaimDailyRewardFailed(getMessage(true, throwable))
            }
        } else {
            PointScreenUiEvent.OnClaimDailyRewardFailed(getMessage(false, throwable))
        }

        postSideEffect(errorEvent)
        Log.e(TAG, "onPointError: ${throwable.message}", throwable)
    }

    private fun getMessage(isClaiming: Boolean, e: Throwable): String {
        return if (e is ApiException) {
            if (e.statusCode == 403 && isClaiming) {
                "You have already claimed today's reward."
            } else {
                generalErrorMapper.mapError(e).message.orEmpty()
            }
        } else {
            e.message.orEmpty()
        }
    }

    companion object {
        private const val TAG = "PointExceptionHandler"
    }
}
