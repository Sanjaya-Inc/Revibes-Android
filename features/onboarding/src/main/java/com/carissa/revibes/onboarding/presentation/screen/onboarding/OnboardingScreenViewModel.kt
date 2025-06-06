/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.onboarding.presentation.screen.onboarding

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.onboarding.data.local.IsOnboardingFinishedDataSource
import org.koin.android.annotation.KoinViewModel

data class OnboardingScreenUiState(val initialPage: Int = 0, val pageCount: Int = 3)

sealed interface OnboardingScreenUiEvent {
    data object OnboardingFinished : OnboardingScreenUiEvent
}

@KoinViewModel
class OnboardingScreenViewModel(
    private val isOnboardingFinishedDataSource: IsOnboardingFinishedDataSource
) : BaseViewModel<OnboardingScreenUiState, OnboardingScreenUiEvent>(
    OnboardingScreenUiState(
        initialPage = if (isOnboardingFinishedDataSource.getIsOnboardingFinishedValue()) 2 else 0
    )
) {
    override fun onEvent(event: OnboardingScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            OnboardingScreenUiEvent.OnboardingFinished -> {
                if (isOnboardingFinishedDataSource.getIsOnboardingFinishedValue()) return
                isOnboardingFinishedDataSource.setIsOnboardingFinishedValue(true)
            }
        }
    }
}
