/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.transaction_history.R
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData
import com.carissa.revibes.transaction_history.presentation.component.TransactionHistoryItem
import com.carissa.revibes.core.presentation.components.TabButton
import com.carissa.revibes.transaction_history.presentation.navigation.TransactionHistoryGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<TransactionHistoryGraph>(start = true)
@Composable
fun TransactionHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionHistoryScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    TransactionHistoryScreenContent(
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@Composable
private fun TransactionHistoryScreenContent(
    uiState: TransactionHistoryScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<TransactionHistoryScreenUiEvent> = EventReceiver { }
) {
    Scaffold(modifier, topBar = {
        CommonHeader(
            "MY TRANSACTION HISTORY",
            backgroundDrawRes = R.drawable.bg_transaction_history,
            searchTextFieldValue = uiState.searchValue
        )
    }) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            val pagerState = rememberPagerState { 2 }
            val scope = rememberCoroutineScope()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TabButton(
                    "Process",
                    pagerState.currentPage == 0,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )
                TabButton(
                    "Transaction Complete",
                    pagerState.currentPage == 1,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
            }
            HorizontalPager(pagerState, modifier = Modifier.weight(1f)) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(10) { index ->
                        val dummyData = TransactionHistoryData.dummy()
                        TransactionHistoryItem(
                            data = dummyData,
                            onClick = {
                                eventReceiver.onEvent(
                                    TransactionHistoryScreenUiEvent.NavigateToTransactionDetail(
                                        transactionId = dummyData.id
                                    )
                                )
                            }
                        )
                    }
                }
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
            uiState = TransactionHistoryScreenUiState(),
            eventReceiver = EventReceiver { }
        )
    }
}
