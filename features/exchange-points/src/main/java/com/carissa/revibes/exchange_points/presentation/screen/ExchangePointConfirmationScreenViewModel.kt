/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.exchange_points.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class ExchangePointConfirmationScreenUiState(
    val isLoading: Boolean = false,
    val paymentDate: String = java.text.SimpleDateFormat(
        "dd MMMM yyyy",
        java.util.Locale.getDefault()
    ).format(java.util.Date()),
    val paymentStatus: String = "Success",
    val couponImage: String = "",
    val couponName: String = "",
    val couponValidUntil: String = "",
    val couponPrice: Int = 0,
    val couponQuantity: Int = 0,
    val totalAmount: Int = 0
)

sealed interface ExchangePointConfirmationScreenUiEvent {
    data object NavigateToHome : ExchangePointConfirmationScreenUiEvent, NavigationEvent
}

@KoinViewModel
class ExchangePointConfirmationScreenViewModel :
    BaseViewModel<ExchangePointConfirmationScreenUiState, ExchangePointConfirmationScreenUiEvent>(
        ExchangePointConfirmationScreenUiState()
    )
