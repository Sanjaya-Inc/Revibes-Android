package com.carissa.revibes.point.presentation.screen

import androidx.lifecycle.viewModelScope
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.model.UserPointFlow
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.point.data.PointRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

data class DailyCheckInNewsScreenUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val title: String = "",
    val content: String = "",
    val timerSeconds: Int = 5,
    val isTimerFinished: Boolean = false,
    val isClaimingReward: Boolean = false,
    val isRewardClaimed: Boolean = false,
    val errorMessage: String? = null
)

sealed interface DailyCheckInNewsScreenUiEvent {
    data object NavigateBack : DailyCheckInNewsScreenUiEvent, NavigationEvent
    data object LoadDailyNews : DailyCheckInNewsScreenUiEvent
    data object ClaimReward : DailyCheckInNewsScreenUiEvent
    data object ClearError : DailyCheckInNewsScreenUiEvent
}

@KoinViewModel
class DailyCheckInNewsScreenViewModel(
    private val pointRepository: PointRepository,
    private val userPointFlow: UserPointFlow
) : BaseViewModel<DailyCheckInNewsScreenUiState, DailyCheckInNewsScreenUiEvent>(
    initialState = DailyCheckInNewsScreenUiState(),
    onCreate = {
        onEvent(DailyCheckInNewsScreenUiEvent.LoadDailyNews)
    }
) {

    override fun onEvent(event: DailyCheckInNewsScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            DailyCheckInNewsScreenUiEvent.NavigateBack -> intent {
                postSideEffect(event)
            }
            DailyCheckInNewsScreenUiEvent.LoadDailyNews -> loadDailyNews()
            DailyCheckInNewsScreenUiEvent.ClaimReward -> claimReward()
            DailyCheckInNewsScreenUiEvent.ClearError -> clearError()
        }
    }

    private fun startCountdownTimer() {
        viewModelScope.launch {
            for (sec in 5 downTo 1) {
                intent { reduce { state.copy(timerSeconds = sec) } }
                delay(1000L)
            }
            intent {
                reduce { state.copy(timerSeconds = 0, isTimerFinished = true) }
            }
        }
    }

    private fun loadDailyNews() {
        intent {
            reduce { state.copy(isLoading = true, error = null) }
            runCatching {
                pointRepository.getDailyNews()
            }.onSuccess { news ->
                if (news != null && news.title.isNotBlank() && news.content.isNotBlank()) {
                    startCountdownTimer()
                    reduce {
                        state.copy(
                            isLoading = false,
                            error = null,
                            title = news.title,
                            content = news.content
                        )
                    }
                } else {
                    reduce {
                        state.copy(
                            isLoading = false,
                            error = "No daily news available today."
                        )
                    }
                }
            }.onFailure { ex ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = ex.message ?: "Failed to load daily news."
                    )
                }
            }
        }
    }

    private fun claimReward() {
        intent {
            if (!state.isTimerFinished || state.isClaimingReward || state.isRewardClaimed) return@intent
            reduce { state.copy(isClaimingReward = true, errorMessage = null) }
            runCatching {
                pointRepository.claimDailyReward()
                userPointFlow.update()
            }.onSuccess {
                reduce {
                    state.copy(
                        isClaimingReward = false,
                        isRewardClaimed = true
                    )
                }
                postSideEffect(DailyCheckInNewsScreenUiEvent.NavigateBack)
            }.onFailure { ex ->
                reduce {
                    state.copy(
                        isClaimingReward = false,
                        errorMessage = ex.message ?: "Failed to claim check-in reward"
                    )
                }
            }
        }
    }

    private fun clearError() {
        intent {
            reduce { state.copy(errorMessage = null) }
        }
    }
}
