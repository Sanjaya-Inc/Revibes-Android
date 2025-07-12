/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.exchange_points.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class ExchangePointConfirmationScreenUiState(
    val isLoading: Boolean = false,
    val paymentDate: String = "26 Maret 2025", // Will be populated from string resources or API
    val paymentStatus: String = "Success", // Will be populated from string resources or API
    // Will be populated from string resources or API
    val couponImage: String = "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
    val couponName: String = "Shopee coupon 70% off", // Will be populated from string resources or API
    val couponValidUntil: String = "Valid until March 2026", // Will be populated from string resources or API
    val couponPrice: Int = 10,
    val couponQuantity: Int = 2,
    val totalAmount: Int = 20
)

sealed interface ExchangePointConfirmationScreenUiEvent {
    data object NavigateToHome : ExchangePointConfirmationScreenUiEvent, NavigationEvent
}

@KoinViewModel
class ExchangePointConfirmationScreenViewModel :
    BaseViewModel<ExchangePointConfirmationScreenUiState, ExchangePointConfirmationScreenUiEvent>(
        ExchangePointConfirmationScreenUiState()
    )
