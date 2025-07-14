/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.DashedBorderContainer
import com.carissa.revibes.core.presentation.components.components.GeneralError
import com.carissa.revibes.core.presentation.components.components.RevibesLoading
import com.carissa.revibes.core.presentation.components.components.TransactionDetailsContent
import com.carissa.revibes.core.presentation.util.openSupportWhatsApp
import com.carissa.revibes.transaction_history.presentation.navigation.TransactionHistoryGraph
import com.carissa.revibes.transaction_history.presentation.screen.DetailTransactionHistoryScreenUiEvent.LoadTransactionDetail
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<TransactionHistoryGraph>()
@Composable
fun DetailTransactionHistoryScreen(
    transactionId: String,
    modifier: Modifier = Modifier,
    viewModel: DetailTransactionHistoryScreenViewModel = koinViewModel()
) {
    val navigator = RevibesTheme.navigator
    val state = viewModel.collectAsState().value

    LaunchedEffect(transactionId) {
        viewModel.onEvent(LoadTransactionDetail(transactionId))
    }

    DetailTransactionHistoryWrapperScreen(
        transactionId = transactionId,
        onBackClick = navigator::navigateUp,
        uiState = state,
        modifier = modifier
    )
}

@Composable
private fun DetailTransactionHistoryWrapperScreen(
    transactionId: String,
    uiState: DetailTransactionHistoryScreenUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    Scaffold(
        modifier = modifier.statusBarsPadding(),
        containerColor = RevibesTheme.colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.back_cta),
                        modifier = Modifier.size(86.dp),
                        tint = Color.Unspecified,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Transaction Details",
                    style = RevibesTheme.typography.h2,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f, fill = true),
                )
            }
        }
    ) { paddingValues ->
        DashedBorderContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            DetailTransactionHistoryScreenContent(
                uiState = uiState
            )
        }
    }
}

@Composable
private fun DetailTransactionHistoryScreenContent(
    uiState: DetailTransactionHistoryScreenUiState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    when {
        uiState.isLoading -> {
            RevibesLoading(modifier = modifier.fillMaxSize())
        }

        uiState.error != null -> {
            GeneralError(
                message = uiState.error,
                modifier = modifier.fillMaxSize()
            )
        }

        uiState.transactionDetail != null -> {
            val detail = uiState.transactionDetail

            TransactionDetailsContent(
                customerName = detail.name,
                locationAddress = detail.locationAddress,
                dateLabel = "Transaction Date",
                date = detail.formattedDate,
                itemDetailsTitle = "TRANSACTION DETAILS",
                status = detail.formattedStatus,
                items = detail.items,
                isEstimatingPoints = false,
                totalPoints = detail.totalPoint,
                itemPoints = detail.itemPoints,
                calculatingPointsText = "Calculating points...",
                totalPointsFormat = "Total Points: %d Points",
                itemPointsFormat = "Item %d: %d Points",
                nameLabel = "Customer Name",
                locationLabel = "Location",
                modifier = modifier,
                actionButton = {
                    Button(
                        text = "Chat Revibes Team",
                        onClick = { context.openSupportWhatsApp() },
                        enabled = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                    )
                }
            )
        }
    }
}

@Composable
@Preview
private fun DetailTransactionHistoryScreenPreview() {
    RevibesTheme {
        DetailTransactionHistoryWrapperScreen(
            transactionId = "sample-123",
            modifier = Modifier.background(Color.White),
            onBackClick = {},
            uiState = DetailTransactionHistoryScreenUiState()
        )
    }
}
