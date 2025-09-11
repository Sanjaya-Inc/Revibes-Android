/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.ComingSoon
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.TabButton
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.core.presentation.components.components.GeneralError
import com.carissa.revibes.core.presentation.components.components.RevibesLoading
import com.carissa.revibes.core.presentation.components.components.SearchConfig
import com.carissa.revibes.transaction_history.R
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData
import com.carissa.revibes.transaction_history.presentation.component.TransactionHistoryItem
import com.carissa.revibes.transaction_history.presentation.navigation.TransactionHistoryGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<TransactionHistoryGraph>(start = true)
@Composable
fun TransactionHistoryScreen(
    modifier: Modifier = Modifier,
    viewModel: TransactionHistoryScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val navigator = RevibesTheme.navigator

    viewModel.collectSideEffect { event ->
        when (event) {
            is TransactionHistoryScreenUiEvent.NavigateBack -> {
                navigator.navigateUp()
            }

            is TransactionHistoryScreenUiEvent.OnLoadTransactionHistoryFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }
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
            searchConfig = SearchConfig.Enabled(
                value = uiState.searchValue,
                onValueChange = { searchValue ->
                    eventReceiver.onEvent(TransactionHistoryScreenUiEvent.SearchValueChanged(searchValue))
                }
            )
        )
    }) { contentPadding ->
        AnimatedContent(uiState) { state ->
            when {
                state.isMaintenance -> {
                    ComingSoon(
                        featureName = "Transcation History",
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(32.dp),
                        onClick = {
                            eventReceiver.onEvent(TransactionHistoryScreenUiEvent.NavigateBack)
                        }
                    )
                }

                else -> {
                    Column(
                        modifier = Modifier
                            .padding(contentPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        val pagerState = rememberPagerState { 2 }
                        val scope = rememberCoroutineScope()

                        LaunchedEffect(pagerState.currentPage) {
                            eventReceiver.onEvent(
                                TransactionHistoryScreenUiEvent.TabChanged(
                                    pagerState.currentPage
                                )
                            )
                        }

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

                        when {
                            uiState.isLoading && uiState.transactions.isEmpty() -> {
                                RevibesLoading()
                            }

                            uiState.error != null && uiState.transactions.isEmpty() -> {
                                Box(
                                    modifier = Modifier.fillMaxSize(),
                                    contentAlignment = Alignment.Center
                                ) {
                                    GeneralError(
                                        message = uiState.error,
                                        onRetry = {
                                            eventReceiver.onEvent(TransactionHistoryScreenUiEvent.Refresh)
                                        }
                                    )
                                }
                            }

                            else -> {
                                HorizontalPager(
                                    pagerState,
                                    modifier = Modifier.weight(1f)
                                ) { page ->
                                    if (uiState.filteredTransactions.isEmpty()) {
                                        TransactionEmptyState()
                                    } else {
                                        TransactionList(
                                            transactions = uiState.filteredTransactions,
                                            isLoadingMore = uiState.isLoadingMore,
                                            hasMoreData = uiState.pagination?.hasMoreNext ?: false,
                                            onLoadMore = {
                                                eventReceiver.onEvent(
                                                    TransactionHistoryScreenUiEvent.LoadMore
                                                )
                                            },
                                            onTransactionClick = { transaction ->
                                                eventReceiver.onEvent(
                                                    TransactionHistoryScreenUiEvent.NavigateToTransactionDetail(
                                                        transactionId = transaction.id
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
            }
        }
    }
}

@Composable
private fun TransactionList(
    transactions: ImmutableList<TransactionHistoryData>,
    isLoadingMore: Boolean,
    hasMoreData: Boolean,
    onLoadMore: () -> Unit,
    onTransactionClick: (TransactionHistoryData) -> Unit
) {
    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - 3) && hasMoreData && !isLoadingMore
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value) {
            onLoadMore()
        }
    }

    LazyColumn(
        state = listState,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize(),
    ) {
        items(transactions) { transaction ->
            TransactionHistoryItem(
                data = transaction,
                onClick = { onTransactionClick(transaction) }
            )
        }

        if (isLoadingMore) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }
    }
}

@Composable
private fun TransactionEmptyState() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "No Transactions Found",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "You don't have any transactions yet.\nStart using Revibes to see your " +
                    "transaction history here.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
private fun TransactionHistoryScreenPreview() {
    RevibesTheme {
        TransactionHistoryScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = TransactionHistoryScreenUiState(
                transactions = persistentListOf(TransactionHistoryData.dummy()),
                filteredTransactions = persistentListOf(TransactionHistoryData.dummy())
            ),
            eventReceiver = EventReceiver { }
        )
    }
}
