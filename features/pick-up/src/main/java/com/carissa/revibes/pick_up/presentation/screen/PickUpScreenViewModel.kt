/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.pick_up.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class PickUpScreenUiState(
    val isLoading: Boolean = false
)

sealed interface PickUpScreenUiEvent {
    data object NavigateToProfile : PickUpScreenUiEvent, NavigationEvent
    data object NavigateToHome : PickUpScreenUiEvent, NavigationEvent
}

@KoinViewModel
class PickUpScreenViewModel : BaseViewModel<PickUpScreenUiState, PickUpScreenUiEvent>(
    initialState = PickUpScreenUiState()
) {

    override fun onEvent(event: PickUpScreenUiEvent) {
        super.onEvent(event)
    }
}
