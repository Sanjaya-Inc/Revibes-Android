/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.profile.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class ProfileScreenUiState(
    val isLoading: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue()
)

sealed interface ProfileScreenUiEvent

@KoinViewModel
class ProfileScreenViewModel :
    BaseViewModel<ProfileScreenUiState, ProfileScreenUiEvent>(ProfileScreenUiState())
