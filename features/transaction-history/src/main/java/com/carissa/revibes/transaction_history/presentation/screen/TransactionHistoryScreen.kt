/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.transaction_history.R
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData
import com.carissa.revibes.transaction_history.presentation.component.TransactionHistoryItem
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
    Scaffold(modifier, topBar = {
        CommonHeader(
            "MY TRANSACTION HISTORY",
            backgroundDrawRes = R.drawable.bg_transaction_history,
            searchTextFieldValue = uiState.searchValue
        )
    }) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(10) { index ->
                TransactionHistoryItem(TransactionHistoryData.dummy())
            }
        }
    }
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
