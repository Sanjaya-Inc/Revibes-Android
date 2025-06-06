/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.onboarding.presentation.screen.register_or_login

import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class RegisterOrLoginScreenUiState(val isLoading: Boolean = false)

sealed interface RegisterOrLoginScreenUiEvent

@KoinViewModel
class RegisterOrLoginScreenViewModel :
    BaseViewModel<RegisterOrLoginScreenUiState, RegisterOrLoginScreenUiEvent>(
        RegisterOrLoginScreenUiState()
    )
