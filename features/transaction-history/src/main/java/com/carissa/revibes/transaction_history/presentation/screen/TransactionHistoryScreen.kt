/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.transaction_history.presentation.navigation.TransactionHistoryGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<TransactionHistoryGraph>(start = true)
@Composable
fun TransactionHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionHistoryScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    TransactionHistoryScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun TransactionHistoryScreenContent(
    uiState: TransactionHistoryScreenUiState,
    modifier: Modifier = Modifier
) {
    TODO()
}

@Composable
@Preview
private fun TransactionHistoryScreenPreview() {
    RevibesTheme {
        TransactionHistoryScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = TransactionHistoryScreenUiState()
        )
    }
}
