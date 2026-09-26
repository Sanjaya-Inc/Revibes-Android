package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home_admin.data.AppSettingRepository
import com.carissa.revibes.home_admin.data.model.AppSettingDailyRewardData
import com.carissa.revibes.home_admin.data.model.AppSettingData
import org.koin.core.annotation.KoinViewModel

data class ManageDailyCheckInScreenUiState(
    val bannerTextInput: TextFieldValue = TextFieldValue(),
    val pointAmountInput: TextFieldValue = TextFieldValue(),
    val dailyReward: AppSettingDailyRewardData = AppSettingDailyRewardData(),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val hasLoaded: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

sealed interface ManageDailyCheckInScreenUiEvent {
    data object NavigateBack : ManageDailyCheckInScreenUiEvent, NavigationEvent
    data class OnBannerTextChange(val value: TextFieldValue) : ManageDailyCheckInScreenUiEvent
    data class OnPointAmountChange(val value: TextFieldValue) : ManageDailyCheckInScreenUiEvent
    data object LoadSettings : ManageDailyCheckInScreenUiEvent
    data object SaveSettings : ManageDailyCheckInScreenUiEvent
    data object ClearMessage : ManageDailyCheckInScreenUiEvent
}

@KoinViewModel
class ManageDailyCheckInScreenViewModel(
    private val appSettingRepository: AppSettingRepository
) : BaseViewModel<ManageDailyCheckInScreenUiState, ManageDailyCheckInScreenUiEvent>(
    initialState = ManageDailyCheckInScreenUiState(isLoading = true),
    onCreate = {
        onEvent(ManageDailyCheckInScreenUiEvent.LoadSettings)
    }
) {
    override fun onEvent(event: ManageDailyCheckInScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ManageDailyCheckInScreenUiEvent.NavigateBack -> intent { postSideEffect(event) }
            is ManageDailyCheckInScreenUiEvent.OnBannerTextChange -> intent {
                reduce { state.copy(bannerTextInput = event.value) }
            }
            is ManageDailyCheckInScreenUiEvent.OnPointAmountChange -> intent {
                if (event.value.text.all(Char::isDigit)) {
                    reduce { state.copy(pointAmountInput = event.value) }
                }
            }
            ManageDailyCheckInScreenUiEvent.LoadSettings -> loadSettings()
            ManageDailyCheckInScreenUiEvent.SaveSettings -> saveSettings()
            ManageDailyCheckInScreenUiEvent.ClearMessage -> intent {
                reduce { state.copy(successMessage = null, errorMessage = null) }
            }
        }
    }

    private fun loadSettings() {
        intent {
            reduce { state.copy(isLoading = true, errorMessage = null) }
            runCatching { appSettingRepository.getAppSetting() }
                .onSuccess { setting ->
                    reduce {
                        state.copy(
                            isLoading = false,
                            hasLoaded = true,
                            bannerTextInput = TextFieldValue(setting.dailyReward.bannerText),
                            pointAmountInput = TextFieldValue(setting.dailyReward.initialPoint.toString()),
                            dailyReward = setting.dailyReward
                        )
                    }
                }
                .onFailure { error ->
                    reduce {
                        state.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load daily check-in settings"
                        )
                    }
                }
        }
    }

    private fun saveSettings() {
        intent {
            val pointAmount = state.pointAmountInput.text.toIntOrNull()
            if (pointAmount == null || pointAmount <= 0) {
                reduce { state.copy(errorMessage = "Point amount is required") }
                return@intent
            }

            reduce { state.copy(isSubmitting = true, errorMessage = null, successMessage = null) }
            runCatching {
                val currentSetting = appSettingRepository.getAppSetting()
                appSettingRepository.updateAppSetting(
                    AppSettingData(
                        point = currentSetting.point,
                        dailyReward = state.dailyReward.copy(
                            initialPoint = pointAmount,
                            bannerText = state.bannerTextInput.text
                        )
                    )
                )
            }.onSuccess {
                reduce {
                    state.copy(
                        isSubmitting = false,
                        successMessage = "Daily check-in settings saved"
                    )
                }
            }.onFailure { error ->
                reduce {
                    state.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Failed to save daily check-in settings"
                    )
                }
            }
        }
    }
}
