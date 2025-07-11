/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.transaction_history.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class TransactionHistoryScreenUiState(
    val isLoading: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue()
)

sealed interface TransactionHistoryScreenUiEvent

@KoinViewModel
class TransactionHistoryScreenViewModel :
    BaseViewModel<TransactionHistoryScreenUiState, TransactionHistoryScreenUiEvent>(
        TransactionHistoryScreenUiState()
    )
