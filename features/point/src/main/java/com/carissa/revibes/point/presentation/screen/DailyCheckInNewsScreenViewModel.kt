package com.carissa.revibes.point.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.model.UserPointFlow
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.point.data.PointRepository
import kotlinx.coroutines.delay
import org.koin.core.annotation.KoinViewModel

data class DailyCheckInNewsScreenUiState(
    val isLoading: Boolean = true,
    val title: String = "Daily News",
    val content: String = "No news today",
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
        intent {
            for (sec in 5 downTo 1) {
                reduce { state.copy(timerSeconds = sec) }
                delay(1000L)
            }
            reduce { state.copy(timerSeconds = 0, isTimerFinished = true) }
        }
    }

    private fun loadDailyNews() {
        startCountdownTimer()
        intent {
            reduce { state.copy(isLoading = true) }
            runCatching {
                pointRepository.getDailyNews()
            }.onSuccess { news ->
                reduce {
                    state.copy(
                        isLoading = false,
                        title = news?.title?.ifBlank { "Eskalasi Konflik AS-Iran" } ?: "Eskalasi Konflik AS-Iran",
                        content = news?.content?.ifBlank { "Eskalasi Konflik AS-Iran membuka risiko volatilitas minyak lanjutan. Harga Minyak kembali Naik +9,6% ke ~US$83/barrel" }
                            ?: "Eskalasi Konflik AS-Iran membuka risiko volatilitas minyak lanjutan. Harga Minyak kembali Naik +9,6% ke ~US$83/barrel"
                    )
                }
            }.onFailure {
                reduce {
                    state.copy(
                        isLoading = false,
                        title = "Eskalasi Konflik AS-Iran",
                        content = "Eskalasi Konflik AS-Iran membuka risiko volatilitas minyak lanjutan. Harga Minyak kembali Naik +9,6% ke ~US$83/barrel"
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
