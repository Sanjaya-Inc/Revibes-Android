package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home_admin.data.AppSettingRepository
import com.carissa.revibes.home_admin.data.model.AppSettingDailyRewardData
import com.carissa.revibes.home_admin.data.model.AppSettingData
import com.carissa.revibes.home_admin.data.model.AppSettingPointData
import com.carissa.revibes.home_admin.domain.parseDropOffPointValues
import org.koin.core.annotation.KoinViewModel

data class ManageDropOffConversionScreenUiState(
    val organicInput: TextFieldValue = TextFieldValue(),
    val nonOrganicInput: TextFieldValue = TextFieldValue(),
    val b3Input: TextFieldValue = TextFieldValue(),
    val dailyReward: AppSettingDailyRewardData = AppSettingDailyRewardData(),
    val isLoading: Boolean = false,
    val isSubmitting: Boolean = false,
    val hasLoaded: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

sealed interface ManageDropOffConversionScreenUiEvent {
    data object NavigateBack : ManageDropOffConversionScreenUiEvent, NavigationEvent
    data class OnOrganicChange(val value: TextFieldValue) : ManageDropOffConversionScreenUiEvent
    data class OnNonOrganicChange(val value: TextFieldValue) : ManageDropOffConversionScreenUiEvent
    data class OnB3Change(val value: TextFieldValue) : ManageDropOffConversionScreenUiEvent
    data object LoadSettings : ManageDropOffConversionScreenUiEvent
    data object SaveSettings : ManageDropOffConversionScreenUiEvent
    data object ClearMessage : ManageDropOffConversionScreenUiEvent
}

@KoinViewModel
class ManageDropOffConversionScreenViewModel(
    private val appSettingRepository: AppSettingRepository
) : BaseViewModel<ManageDropOffConversionScreenUiState, ManageDropOffConversionScreenUiEvent>(
    initialState = ManageDropOffConversionScreenUiState(isLoading = true),
    onCreate = {
        onEvent(ManageDropOffConversionScreenUiEvent.LoadSettings)
    }
) {
    override fun onEvent(event: ManageDropOffConversionScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ManageDropOffConversionScreenUiEvent.NavigateBack -> intent { postSideEffect(event) }
            is ManageDropOffConversionScreenUiEvent.OnOrganicChange -> intent {
                if (event.value.text.all(Char::isDigit)) {
                    reduce { state.copy(organicInput = event.value) }
                }
            }
            is ManageDropOffConversionScreenUiEvent.OnNonOrganicChange -> intent {
                if (event.value.text.all(Char::isDigit)) {
                    reduce { state.copy(nonOrganicInput = event.value) }
                }
            }
            is ManageDropOffConversionScreenUiEvent.OnB3Change -> intent {
                if (event.value.text.all(Char::isDigit)) {
                    reduce { state.copy(b3Input = event.value) }
                }
            }
            ManageDropOffConversionScreenUiEvent.LoadSettings -> loadSettings()
            ManageDropOffConversionScreenUiEvent.SaveSettings -> saveSettings()
            ManageDropOffConversionScreenUiEvent.ClearMessage -> intent {
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
                            organicInput = TextFieldValue(setting.point.organic.toString()),
                            nonOrganicInput = TextFieldValue(setting.point.nonOrganic.toString()),
                            b3Input = TextFieldValue(setting.point.b3.toString()),
                            dailyReward = setting.dailyReward
                        )
                    }
                }
                .onFailure { error ->
                    reduce {
                        state.copy(
                            isLoading = false,
                            errorMessage = error.message ?: "Failed to load drop-off points"
                        )
                    }
                }
        }
    }

    private fun saveSettings() {
        intent {
            val points = parseDropOffPointValues(
                state.organicInput.text,
                state.nonOrganicInput.text,
                state.b3Input.text
            )
            if (points == null) {
                reduce { state.copy(errorMessage = "Enter a point value for every waste type") }
                return@intent
            }

            reduce { state.copy(isSubmitting = true, errorMessage = null, successMessage = null) }
            runCatching {
                appSettingRepository.updateAppSetting(
                    AppSettingData(
                        point = AppSettingPointData(
                            organic = points.organic,
                            nonOrganic = points.nonOrganic,
                            b3 = points.b3
                        ),
                        dailyReward = state.dailyReward
                    )
                )
            }.onSuccess {
                reduce {
                    state.copy(
                        isSubmitting = false,
                        successMessage = "Drop-off points saved"
                    )
                }
            }.onFailure { error ->
                reduce {
                    state.copy(
                        isSubmitting = false,
                        errorMessage = error.message ?: "Failed to save drop-off points"
                    )
                }
            }
        }
    }
}
