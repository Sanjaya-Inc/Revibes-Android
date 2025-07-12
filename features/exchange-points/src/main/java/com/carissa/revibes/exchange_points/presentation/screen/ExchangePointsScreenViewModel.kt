/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.exchange_points.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class ExchangePointsScreenUiState(
    val isLoading: Boolean = false,
    val points: Int = 0,
    val images: List<String> = DUMMY_IMAGES // emptyList()
)

sealed interface ExchangePointsScreenUiEvent {
    data object NavigateToProfile : ExchangePointsScreenUiEvent, NavigationEvent
    data object NavigateToDetailExchangePoint : ExchangePointsScreenUiEvent, NavigationEvent
}

@KoinViewModel
class ExchangePointsScreenViewModel :
    BaseViewModel<ExchangePointsScreenUiState, ExchangePointsScreenUiEvent>(
        ExchangePointsScreenUiState()
    )

private val DUMMY_IMAGES = listOf(
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
)
