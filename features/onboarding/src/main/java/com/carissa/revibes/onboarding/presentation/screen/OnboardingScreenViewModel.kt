/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.onboarding.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class OnboardingScreenUiState(val isLoading: Boolean = false)

sealed interface OnboardingScreenUiEvent

@KoinViewModel
class OnboardingScreenViewModel :
    BaseViewModel<OnboardingScreenUiState, OnboardingScreenUiEvent>(OnboardingScreenUiState())
