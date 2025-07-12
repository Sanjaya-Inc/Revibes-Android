/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.admin_menu.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
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
class AdminMenuScreenViewModel(
    private val navigationEventBus: NavigationEventBus,
) :
    BaseViewModel<AdminMenuScreenUiState, AdminMenuScreenUiEvent>(AdminMenuScreenUiState()) {
    override fun onEvent(event: AdminMenuScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is NavigationEvent -> navigationEventBus.post(event)
            else -> Unit
        }
    }
}
