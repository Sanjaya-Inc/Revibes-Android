/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.exchange_points.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import org.koin.android.annotation.KoinViewModel

data class ExchangePointsScreenUiState(
    val isLoading: Boolean = false,
    val points: Int = 0,
    val images: List<String> = DUMMY_IMAGES // emptyList()
)

sealed interface ExchangePointsScreenUiEvent : NavigationEvent {
    data object NavigateToProfile : ExchangePointsScreenUiEvent
    data object NavigateToDetailExchangePoint : ExchangePointsScreenUiEvent
}

@KoinViewModel
class ExchangePointsScreenViewModel(
    private val navigationEventBus: NavigationEventBus
) : BaseViewModel<ExchangePointsScreenUiState, ExchangePointsScreenUiEvent>(
    ExchangePointsScreenUiState()
) {
    override fun onEvent(event: ExchangePointsScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ExchangePointsScreenUiEvent.NavigateToProfile -> navigationEventBus.post(event)
            ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint -> navigationEventBus.post(event)
        }
    }
}

private val DUMMY_IMAGES = listOf(
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
)
