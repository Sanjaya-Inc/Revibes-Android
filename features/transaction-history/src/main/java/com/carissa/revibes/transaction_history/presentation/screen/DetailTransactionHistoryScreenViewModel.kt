/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.transaction_history.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class DetailTransactionHistoryScreenUiState(val isLoading: Boolean = false)

sealed interface DetailTransactionHistoryScreenUiEvent

@KoinViewModel
class DetailTransactionHistoryScreenViewModel :
    BaseViewModel<DetailTransactionHistoryScreenUiState, DetailTransactionHistoryScreenUiEvent>(
        DetailTransactionHistoryScreenUiState()
    )
