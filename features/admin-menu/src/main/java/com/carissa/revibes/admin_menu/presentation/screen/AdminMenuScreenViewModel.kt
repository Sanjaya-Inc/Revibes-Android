/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.admin_menu.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class AdminMenuScreenUiState(
    val isLoading: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
)

sealed interface AdminMenuScreenUiEvent {
    data class OnSearchChange(val query: TextFieldValue) : AdminMenuScreenUiEvent
    data object NavigateToProfile : AdminMenuScreenUiEvent, NavigationEvent
}

@KoinViewModel
class AdminMenuScreenViewModel : BaseViewModel<AdminMenuScreenUiState, AdminMenuScreenUiEvent>(
    AdminMenuScreenUiState()
)
