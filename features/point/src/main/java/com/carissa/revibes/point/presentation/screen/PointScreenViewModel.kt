package com.carissa.revibes.point.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.point.data.PointRepository
import com.carissa.revibes.point.presentation.handler.PointExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class PointScreenUiState(
    val isLoading: Boolean = false,
    val isClaimingReward: Boolean = false,
    val allowedToClaimReward: Boolean = true,
    val dailyRewards: List<DailyReward> = emptyList(),
    val missions: List<Triple<String, String, Int>> = emptyList(),
)

data class DailyReward(
    val id: String,
    val dayIndex: Int,
    val amount: Int,
    val claimedAt: String? = null,
)

sealed interface PointScreenUiEvent {
    data object NavigateToProfile : PointScreenUiEvent, NavigationEvent
    data object Initialize : PointScreenUiEvent
    data object ClaimDailyReward : PointScreenUiEvent
    data class OnLoadDailyRewardsFailed(val message: String) : PointScreenUiEvent
    data class OnClaimDailyRewardFailed(val message: String) : PointScreenUiEvent
}

@KoinViewModel
class PointScreenViewModel(
    private val pointRepository: PointRepository,
    private val pointExceptionHandler: PointExceptionHandler
) : BaseViewModel<PointScreenUiState, PointScreenUiEvent>(
    initialState = PointScreenUiState(isLoading = true, missions = DUMMY_MISSIONS),
    onCreate = { onEvent(PointScreenUiEvent.Initialize) },
    exceptionHandler = { syntax, exception ->
        pointExceptionHandler.onPointError(syntax, exception)
    },
) {
    override fun onEvent(event: PointScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            PointScreenUiEvent.Initialize -> loadDailyRewards()
            PointScreenUiEvent.ClaimDailyReward -> claimDailyReward()
            else -> Unit
        }
    }

    private fun loadDailyRewards() {
        intent {
            reduce { state.copy(isLoading = true) }
            val dailyRewards = pointRepository.getDailyRewards()
            reduce {
                state.copy(
                    isLoading = false,
                    dailyRewards = dailyRewards
                )
            }
        }
    }

    private fun claimDailyReward() {
        intent {
            reduce { state.copy(isClaimingReward = true) }
            pointRepository.claimDailyReward()
            val updatedDailyRewards = pointRepository.getDailyRewards()
            reduce {
                state.copy(
                    isClaimingReward = false,
                    dailyRewards = updatedDailyRewards
                )
            }
        }
    }
}

val DUMMY_MISSIONS = listOf(
    Triple("Complete your Revibe Account Details", "Earn Extra Points", 100),
    Triple("Change your Rubbish on Revibe", "Earn Extra Points", 150),
    Triple("Complete your Revibe Account Details", "Earn Extra Points", 100),
    Triple("Change your Rubbish on Revibe", "Earn Extra Points", 150),
    Triple("Complete your Revibe Account Details", "Earn Extra Points", 100),
    Triple("Change your Rubbish on Revibe", "Earn Extra Points", 150),
)
