package com.carissa.revibes.point.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.point.data.PointRepository
import com.carissa.revibes.point.domain.model.Mission
import com.carissa.revibes.point.presentation.handler.PointExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class PointScreenUiState(
    val isLoading: Boolean = false,
    val isClaimingReward: Boolean = false,
    val allowedToClaimReward: Boolean = true,
    val dailyRewards: List<DailyReward> = emptyList(),
    val missions: List<Mission> = emptyList(),
    val isMissionsLoading: Boolean = false,
    val claimingMissionId: String? = null,
    val missionError: String? = null,
    val missionSuccess: String? = null,
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
    data object GetMissions : PointScreenUiEvent
    data class ClaimMission(val missionId: String) : PointScreenUiEvent
    data object ClearMissionMessage : PointScreenUiEvent
    data class OnLoadDailyRewardsFailed(val message: String) : PointScreenUiEvent
    data class OnClaimDailyRewardFailed(val message: String) : PointScreenUiEvent
}

@KoinViewModel
class PointScreenViewModel(
    private val pointRepository: PointRepository,
    private val pointExceptionHandler: PointExceptionHandler
) : BaseViewModel<PointScreenUiState, PointScreenUiEvent>(
    initialState = PointScreenUiState(isLoading = true),
    onCreate = {
        onEvent(PointScreenUiEvent.Initialize)
        onEvent(PointScreenUiEvent.GetMissions)
    },
    exceptionHandler = { syntax, exception ->
        pointExceptionHandler.onPointError(syntax, exception)
    },
) {
    override fun onEvent(event: PointScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            PointScreenUiEvent.Initialize -> loadDailyRewards()
            PointScreenUiEvent.ClaimDailyReward -> claimDailyReward()
            PointScreenUiEvent.GetMissions -> getMissions()
            is PointScreenUiEvent.ClaimMission -> claimMission(event.missionId)
            PointScreenUiEvent.ClearMissionMessage -> clearMissionMessage()
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

    private fun getMissions() {
        intent {
            reduce { state.copy(isMissionsLoading = true, missionError = null) }
            val missions = pointRepository.getMissions()
            reduce {
                state.copy(
                    isMissionsLoading = false,
                    missions = missions
                )
            }
        }
    }

    private fun claimMission(missionId: String) {
        intent {
            reduce {
                state.copy(
                    claimingMissionId = missionId,
                    missionError = null,
                    missionSuccess = null
                )
            }
            runCatching {
                pointRepository.claimMission(missionId)
            }.onSuccess {
                reduce {
                    state.copy(
                        claimingMissionId = null,
                        missionSuccess = "Mission claimed successfully!"
                    )
                }
                onEvent(PointScreenUiEvent.GetMissions)
            }.onFailure {
                reduce {
                    state.copy(
                        claimingMissionId = null,
                        missionError = "Failed to claim mission."
                    )
                }
            }
        }
    }

    private fun clearMissionMessage() {
        intent {
            reduce {
                state.copy(
                    missionError = null,
                    missionSuccess = null
                )
            }
        }
    }
}
