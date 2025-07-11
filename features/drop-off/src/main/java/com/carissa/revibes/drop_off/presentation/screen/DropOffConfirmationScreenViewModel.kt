/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.drop_off.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class DropOffConfirmationScreenUiState(val isLoading: Boolean = false)

sealed interface DropOffConfirmationScreenUiEvent

@KoinViewModel
class DropOffConfirmationScreenViewModel :
    BaseViewModel<DropOffConfirmationScreenUiState, DropOffConfirmationScreenUiEvent>(
        DropOffConfirmationScreenUiState()
    )
