/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.shop.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import org.koin.android.annotation.KoinViewModel

data class ShopScreenUiState(val isLoading: Boolean = false)

sealed interface ShopScreenUiEvent : NavigationEvent {
    data object NavigateToProfile : ShopScreenUiEvent
}

@KoinViewModel
class ShopScreenViewModel(
    private val navigationEventBus: NavigationEventBus
) : BaseViewModel<ShopScreenUiState, ShopScreenUiEvent>(ShopScreenUiState()) {

    override fun onEvent(event: ShopScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ShopScreenUiEvent.NavigateToProfile -> navigationEventBus.post(event)
        }
    }
}
