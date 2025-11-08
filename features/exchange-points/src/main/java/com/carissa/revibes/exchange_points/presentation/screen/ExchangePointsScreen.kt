/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.exchange_points.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.carissa.revibes.core.data.main.model.FeatureName
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.CommonHeader
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.core.presentation.compose.components.MaintenanceChecker
import com.carissa.revibes.core.presentation.compose.components.SearchConfig
import com.carissa.revibes.core.presentation.compose.components.TabButton
import com.carissa.revibes.exchange_points.R
import com.carissa.revibes.exchange_points.domain.model.UserVoucher
import com.carissa.revibes.exchange_points.domain.model.Voucher
import com.carissa.revibes.exchange_points.presentation.component.UserVoucherItem
import com.carissa.revibes.exchange_points.presentation.navigation.ExchangePointsGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<ExchangePointsGraph>(start = true)
@Composable
fun ExchangePointsScreen(
    modifier: Modifier = Modifier,
    viewModel: ExchangePointsScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val coins by viewModel.coins.collectAsStateWithLifecycle()
    val navigator = RevibesTheme.navigator
    viewModel.collectSideEffect { event ->
        when (event) {
            is ExchangePointsScreenUiEvent.NavigateBack -> navigator.navigateUp()
            is ExchangePointsScreenUiEvent.NavigateToUserVoucherDetail -> {
                // Navigation will be handled by Compose Destinations
            }
            else -> Unit
        }
    }
    ExchangePointsScreenContent(
        coins = coins,
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@Composable
private fun ExchangePointsScreenContent(
    coins: Long,
    uiState: ExchangePointsScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ExchangePointsScreenUiEvent> = EventReceiver { }
) {
    val pagerState = rememberPagerState { 2 }
    val scope = rememberCoroutineScope()

    LaunchedEffect(pagerState.currentPage) {
        eventReceiver.onEvent(ExchangePointsScreenUiEvent.TabChanged(pagerState.currentPage))
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            CommonHeader(
                title = stringResource(R.string.exchange_points_title),
                searchConfig = SearchConfig.None,
                subtitle = stringResource(R.string.exchange_points_subtitle, coins),
                backgroundDrawRes = R.drawable.bg_exchange_points,
            )
        }
    ) { paddingValues ->
        MaintenanceChecker(FeatureName.EXCHANGES, onBackAction = {
            eventReceiver.onEvent(ExchangePointsScreenUiEvent.NavigateBack)
        }, onFeatureEnabled = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.exchange_points_description),
                    modifier = Modifier.padding(top = 24.dp, bottom = 16.dp),
                    style = RevibesTheme.typography.h3,
                    textAlign = TextAlign.Center,
                    color = RevibesTheme.colors.primary,
                )

                // Tab buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    TabButton(
                        text = "Available Vouchers",
                        isSelected = pagerState.currentPage == 0,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }
                    )
                    TabButton(
                        text = "My Vouchers",
                        isSelected = pagerState.currentPage == 1,
                        modifier = Modifier.weight(1f),
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }
                    )
                }

                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.weight(1f)
                ) { page ->
                    when (page) {
                        0 -> ContentStateSwitcher(
                            uiState.isLoading,
                            error = uiState.error,
                            modifier = Modifier.padding(16.dp),
                            actionButton = "Refresh" to {
                                eventReceiver.onEvent(ExchangePointsScreenUiEvent.Refresh)
                            }
                        ) {
                            AvailableVouchersTab(
                                vouchers = uiState.vouchers,
                                onVoucherClick = { voucher ->
                                    eventReceiver.onEvent(
                                        ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint(voucher)
                                    )
                                }
                            )
                        }
                        1 -> ContentStateSwitcher(
                            uiState.isLoadingUserVouchers,
                            error = null, // User vouchers have separate error handling if needed
                            modifier = Modifier.padding(16.dp),
                            actionButton = "Retry" to {
                                eventReceiver.onEvent(ExchangePointsScreenUiEvent.TabChanged(1))
                            }
                        ) {
                            MyVouchersTab(
                                userVouchers = uiState.userVouchers,
                                onVoucherClick = { userVoucher ->
                                    eventReceiver.onEvent(
                                        ExchangePointsScreenUiEvent.NavigateToUserVoucherDetail(userVoucher)
                                    )
                                }
                            )
                        }
                    }
                }
            }
        })
    }
}

@Composable
private fun AvailableVouchersTab(
    vouchers: ImmutableList<Voucher>,
    onVoucherClick: (Voucher) -> Unit
) {
    AnimatedContent(vouchers.isEmpty()) { isEmpty ->
        if (isEmpty) {
            VoucherEmptyState("No vouchers available at the moment")
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(vouchers) { voucher ->
                    AsyncImage(
                        model = voucher.imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 32.dp, vertical = 4.dp)
                            .clickable { onVoucherClick(voucher) },
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }
    }
}

@Composable
private fun MyVouchersTab(
    userVouchers: ImmutableList<UserVoucher>,
    onVoucherClick: (UserVoucher) -> Unit
) {
    AnimatedContent(userVouchers.isEmpty()) { isEmpty ->
        if (isEmpty) {
            VoucherEmptyState("You don't have any vouchers yet.\nExchange points to get vouchers!")
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(userVouchers) { userVoucher ->
                    UserVoucherItem(
                        userVoucher = userVoucher,
                        onClick = { onVoucherClick(userVoucher) }
                    )
                }
            }
        }
    }
}

@Composable
private fun VoucherEmptyState(message: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "No Vouchers Found",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
@Preview
private fun ExchangePointsScreenPreview() {
    RevibesTheme {
        ExchangePointsScreenContent(
            modifier = Modifier.background(Color.White),
            coins = 0,
            uiState = ExchangePointsScreenUiState(
                vouchers = persistentListOf(
                    Voucher(
                        id = "1",
                        name = "Voucher 1",
                        description = "Description 1",
                        imageUri = "",
                        point = 100,
                        quota = 10
                    )
                ),
                userVouchers = persistentListOf(
                    UserVoucher(
                        id = "1",
                        voucherId = "voucher1",
                        status = "available",
                        claimedAt = null,
                        expiredAt = null,
                        createdAt = "2025-10-05T13:07:29.226Z",
                        updatedAt = "2025-10-05T13:07:29.226Z",
                        name = "Economical Grocery Package",
                        description = "Affordable Grocery Sembako Package",
                        imageUri = "",
                        code = "RVB-ESP-65481",
                        guides = emptyList(),
                        termConditions = emptyList()
                    )
                )
            )
        )
    }
}
