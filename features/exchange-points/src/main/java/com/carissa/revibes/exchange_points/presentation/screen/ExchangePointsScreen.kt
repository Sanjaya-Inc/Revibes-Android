/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.exchange_points.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.carissa.revibes.exchange_points.R
import com.carissa.revibes.exchange_points.domain.model.Voucher
import com.carissa.revibes.exchange_points.presentation.navigation.ExchangePointsGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
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
    viewModel.collectSideEffect {
        if (it is ExchangePointsScreenUiEvent.NavigateBack) navigator.navigateUp()
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
    coins: Int,
    uiState: ExchangePointsScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ExchangePointsScreenUiEvent> = EventReceiver { }
) {
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
                ContentStateSwitcher(
                    uiState.isLoading,
                    error = uiState.error,
                    actionButton = "Refresh" to {
                        eventReceiver.onEvent(ExchangePointsScreenUiEvent.Refresh)
                    }
                ) {
                    LazyColumn {
                        items(uiState.vouchers) { voucher ->
                            AsyncImage(
                                model = voucher.imageUri,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 32.dp, vertical = 4.dp)
                                    .clickable {
                                        eventReceiver.onEvent(
                                            ExchangePointsScreenUiEvent.NavigateToDetailExchangePoint(
                                                voucher
                                            )
                                        )
                                    },
                                contentScale = ContentScale.FillWidth,
                            )
                        }
                    }
                }
            }
        })
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
                )
            )
        )
    }
}
