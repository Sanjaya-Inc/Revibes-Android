/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.shop.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class ShopScreenUiState(val isLoading: Boolean = false)

sealed interface ShopScreenUiEvent : NavigationEvent {
    data object NavigateToProfile : ShopScreenUiEvent, NavigationEvent
}

@KoinViewModel
class ShopScreenViewModel : BaseViewModel<ShopScreenUiState, ShopScreenUiEvent>(
    ShopScreenUiState()
)
