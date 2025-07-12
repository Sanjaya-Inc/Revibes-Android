/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.exchange_points.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.Yellow900
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.exchange_points.R
import com.carissa.revibes.exchange_points.presentation.navigation.ExchangePointsGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ExchangePointsGraph>()
@Composable
fun ExchangePointConfirmationScreen(
    modifier: Modifier = Modifier,
    viewModel: ExchangePointConfirmationScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    ExchangePointConfirmationScreenContent(
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@Composable
private fun ExchangePointConfirmationScreenContent(
    uiState: ExchangePointConfirmationScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ExchangePointConfirmationScreenUiEvent> = EventReceiver {},
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = RevibesTheme.colors.primary,
                        shape = RoundedCornerShape(bottomStart = 12.dp, bottomEnd = 12.dp)
                    )
                    .padding(vertical = 36.dp, horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.successful_order_payment),
                        style = RevibesTheme.typography.h2,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold
                    )
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        modifier = Modifier.padding(top = 8.dp)
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_coin),
                            contentDescription = "Coin Icon",
                            modifier = Modifier.size(24.dp),
                            tint = Yellow900
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = uiState.totalAmount.toString(),
                            style = RevibesTheme.typography.h1.copy(fontSize = 28.sp),
                            color = Color.White,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .offset(y = (-30).dp)
                    .size(60.dp)
                    .background(
                        color = RevibesTheme.colors.primary,
                        shape = RoundedCornerShape(60.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_check),
                    contentDescription = "Check Icon",
                    modifier = Modifier.size(30.dp),
                    tint = Color.White
                )
            }
            PaymentDetailsSection(
                paymentDate = uiState.paymentDate,
                paymentStatus = uiState.paymentStatus
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = RevibesTheme.colors.primary
            )
            CouponInformationSection(
                couponImage = uiState.couponImage,
                couponName = uiState.couponName,
                couponValidUntil = uiState.couponValidUntil,
                couponPrice = uiState.couponPrice,
                couponQuantity = uiState.couponQuantity
            )
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                thickness = 1.dp,
                color = RevibesTheme.colors.primary
            )
            PaymentAmountSection(totalAmount = uiState.totalAmount)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 48.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Image(
                    painter = painterResource(R.drawable.girl_character),
                    contentDescription = "Girl Character",
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier.heightIn(max = 250.dp)
                )
            }
            Button(
                text = stringResource(R.string.home),
                variant = ButtonVariant.Primary,
                onClick = { eventReceiver.onEvent(ExchangePointConfirmationScreenUiEvent.NavigateToHome) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 4.dp)
            )
        }
    }
}

@Composable
private fun PaymentDetailsSection(
    paymentDate: String,
    paymentStatus: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.payment_paid_date),
            style = RevibesTheme.typography.body2,
            color = RevibesTheme.colors.primary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = paymentDate,
            style = RevibesTheme.typography.h3,
            color = RevibesTheme.colors.primary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(R.string.payment_status_colon, paymentStatus),
            style = RevibesTheme.typography.body2,
            color = RevibesTheme.colors.primary,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun CouponInformationSection(
    couponImage: String,
    couponName: String,
    couponValidUntil: String,
    couponPrice: Int,
    couponQuantity: Int
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = couponImage,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(25.dp)),
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp)
        ) {
            Text(
                text = couponName,
                style = RevibesTheme.typography.body1,
                color = RevibesTheme.colors.primary,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = couponValidUntil,
                style = RevibesTheme.typography.body3,
                color = RevibesTheme.colors.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.Bottom) {
                Icon(
                    painter = painterResource(R.drawable.ic_coin),
                    contentDescription = "Coin Icon",
                    modifier = Modifier.size(16.dp),
                    tint = Yellow900
                )
                Text(
                    modifier = Modifier.padding(start = 4.dp),
                    text = couponPrice.toString(),
                    style = RevibesTheme.typography.body1,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Light
                )
            }
        }
        Text(
            text = stringResource(R.string.quantity_format, couponQuantity),
            style = RevibesTheme.typography.h2,
            color = RevibesTheme.colors.primary,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun PaymentAmountSection(totalAmount: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Absolute.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.payment_amount),
            style = RevibesTheme.typography.h3,
            color = RevibesTheme.colors.primary,
            fontWeight = FontWeight.Bold
        )

        Row(verticalAlignment = Alignment.Bottom) {
            Icon(
                painter = painterResource(R.drawable.ic_coin),
                contentDescription = "Coin Icon",
                modifier = Modifier.size(20.dp),
                tint = Yellow900
            )
            Text(
                modifier = Modifier.padding(start = 4.dp),
                text = totalAmount.toString(),
                style = RevibesTheme.typography.h2,
                color = RevibesTheme.colors.primary,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
@Preview
private fun ExchangePointConfirmationScreenPreview() {
    RevibesTheme {
        ExchangePointConfirmationScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = ExchangePointConfirmationScreenUiState(
                paymentDate = "20 JULY 2024", // Preview default
                paymentStatus = "SUCCESS", // Preview default
                couponName = "Shopee Coupon 70% off", // Preview default
                couponValidUntil = "Valid until 31 December 2024" // Preview default
            )
        )
    }
}
