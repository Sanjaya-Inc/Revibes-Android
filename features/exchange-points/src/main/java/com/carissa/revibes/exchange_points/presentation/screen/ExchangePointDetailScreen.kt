/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.exchange_points.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.Yellow900
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.core.presentation.components.components.SearchConfig
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.exchange_points.R
import com.carissa.revibes.exchange_points.domain.model.Voucher
import com.carissa.revibes.exchange_points.presentation.navigation.ExchangePointsGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ExchangePointsGraph>
@Composable
fun ExchangePointDetailScreen(
    voucher: Voucher,
    modifier: Modifier = Modifier,
    viewModel: ExchangePointDetailScreenViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )
    var showBottomSheet by remember { mutableStateOf(false) }
    var quantity by remember { mutableIntStateOf(1) }
    val context = LocalContext.current

    viewModel.collectSideEffect {
        when (it) {
            is ExchangePointDetailScreenUiEvent.ShowToast -> {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    ExchangePointDetailScreenContent(
        voucher = voucher,
        modifier = modifier,
        onBuyNowClicked = {
            showBottomSheet = true
        }
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = bottomSheetState,
            dragHandle = { },
            containerColor = RevibesTheme.colors.primary,
        ) {
            CouponConfirmationBottomSheet(
                voucher = voucher,
                quantity = quantity,
                onDismiss = { showBottomSheet = false },
                onQuantityChanged = { quantity = it },
                onConfirm = {
                    viewModel.onEvent(
                        ExchangePointDetailScreenUiEvent.PurchaseVoucher(
                            id = voucher.id,
                            qty = quantity
                        )
                    )
                },
                isConfirmEnabled = voucher.point * quantity <= state.userPoints,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
            )
        }
    }
}

@Composable
private fun ExchangePointDetailScreenContent(
    voucher: Voucher,
    modifier: Modifier = Modifier,
    onBuyNowClicked: () -> Unit
) {
    val navigator = RevibesTheme.navigator
    Scaffold(
        topBar = {
            CommonHeader(
                title = stringResource(R.string.exchange_point_detail_title),
                searchConfig = SearchConfig.None,
                backgroundDrawRes = R.drawable.bg_exchange_points,
                onBackClicked = navigator::navigateUp,
            )
        },
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AsyncImage(
                    model = voucher.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    contentScale = ContentScale.FillWidth
                )

                Spacer(modifier = Modifier.height(16.dp))
                TimeToShopSection(
                    validUntil = voucher.validUntil,
                    description = voucher.description
                )
                Spacer(modifier = Modifier.height(24.dp))
                TermsAndConditionsSection(terms = voucher.terms.toImmutableList())
                Spacer(modifier = Modifier.height(32.dp))
                Button(
                    text = stringResource(R.string.buy_it_now),
                    variant = ButtonVariant.Primary,
                    onClick = onBuyNowClicked,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                )
            }
        }
    }
}

@Composable
private fun TimeToShopSection(
    validUntil: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.time_to_shop),
            style = RevibesTheme.typography.h3.copy(
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            ),
            color = RevibesTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = validUntil,
            style = RevibesTheme.typography.body1.copy(
                fontSize = 16.sp,
            ),
            color = RevibesTheme.colors.primary
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = stringResource(R.string.details),
            style = RevibesTheme.typography.h4.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            color = RevibesTheme.colors.background
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = RevibesTheme.typography.body2.copy(
                fontSize = 14.sp
            ),
            color = RevibesTheme.colors.primary.copy(alpha = 0.7f)
        )
    }
}

@Composable
private fun TermsAndConditionsSection(
    terms: ImmutableList<String>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(R.string.terms_conditions),
            style = RevibesTheme.typography.h3.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            color = RevibesTheme.colors.primary
        )

        Spacer(modifier = Modifier.height(12.dp))

        terms.forEachIndexed { index, term ->
            Row(
                modifier = Modifier.padding(vertical = 4.dp),
                verticalAlignment = Alignment.Top
            ) {
                Text(
                    text = "${index + 1}.",
                    style = RevibesTheme.typography.body2.copy(
                        fontSize = 14.sp
                    ),
                    color = RevibesTheme.colors.primary.copy(alpha = 0.7f),
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = term,
                    style = RevibesTheme.typography.body2.copy(
                        fontSize = 14.sp
                    ),
                    color = RevibesTheme.colors.primary.copy(alpha = 0.7f),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun CouponConfirmationBottomSheet(
    voucher: Voucher,
    quantity: Int,
    onDismiss: () -> Unit,
    onQuantityChanged: (Int) -> Unit,
    onConfirm: () -> Unit,
    isConfirmEnabled: Boolean,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
            contentDescription = "Close",
            tint = RevibesTheme.colors.background,
            modifier = Modifier
                .padding(8.dp)
                .size(24.dp)
                .align(Alignment.TopEnd)
                .clickable { onDismiss() }
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp), // Add top padding to avoid overlap with close button
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom
            ) {
                AsyncImage(
                    model = voucher.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(16.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_coin),
                        contentDescription = "Coin Icon",
                        modifier = Modifier.size(24.dp),
                        tint = Yellow900
                    )
                    Text(
                        text = voucher.point.toString(),
                        style = RevibesTheme.typography.h4.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        ),
                        color = Yellow900
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                thickness = 1.dp,
                color = RevibesTheme.colors.background
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.quantity),
                    style = RevibesTheme.typography.body1.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Light
                    ),
                    color = RevibesTheme.colors.background
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = if (quantity > 1) {
                                    RevibesTheme.colors.background
                                } else {
                                    RevibesTheme.colors.background.copy(alpha = 0.3f)
                                },
                                shape = CircleShape
                            )
                            .clickable(enabled = quantity > 1) {
                                onQuantityChanged(quantity - 1)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "-",
                            style = RevibesTheme.typography.h4.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = RevibesTheme.colors.primary
                        )
                    }

                    Text(
                        text = quantity.toString(),
                        style = RevibesTheme.typography.h4.copy(
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = RevibesTheme.colors.background,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(40.dp)
                    )

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = RevibesTheme.colors.background,
                                shape = CircleShape
                            )
                            .clickable {
                                onQuantityChanged(quantity + 1)
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "+",
                            style = RevibesTheme.typography.h4.copy(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            ),
                            color = RevibesTheme.colors.primary
                        )
                    }
                }
            }

            Button(
                text = stringResource(R.string.confirmation),
                variant = ButtonVariant.PrimaryOutlined,
                onClick = onConfirm,
                enabled = isConfirmEnabled,
                modifier = Modifier
                    .padding(vertical = 16.dp)
                    .clip(RoundedCornerShape(56.dp))
                    .background(RevibesTheme.colors.background)
                    .fillMaxWidth()
                    .height(56.dp)
            )
        }
    }
}

@Composable
@Preview
private fun ExchangePointDetailScreenPreview() {
    RevibesTheme {
        ExchangePointDetailScreenContent(
            modifier = Modifier.background(Color.White),
            voucher = Voucher(
                id = "1",
                name = "Voucher Title",
                description = "Voucher Title",
                imageUri = "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
                point = 10,
                quota = 10,
                validUntil = "Valid until: 31 Dec 2025",
                terms = listOf("Term 1", "Term 2", "Term 3")
            ),
            onBuyNowClicked = {}
        )
    }
}

@Composable
@Preview(showBackground = true)
private fun CouponConfirmationBottomSheetPreview() {
    RevibesTheme {
        Box(modifier = Modifier.background(RevibesTheme.colors.primary)) {
            CouponConfirmationBottomSheet(
                voucher = Voucher(
                    id = "1",
                    name = "Voucher Title",
                    description = "Voucher Title",
                    imageUri = "https://gcdnb.pbrd.co/images/16vLvVICjqy3.webp",
                    point = 10,
                    quota = 10,
                    validUntil = "Valid until: 31 Dec 2025",
                    terms = listOf("Term 1", "Term 2", "Term 3")
                ),
                quantity = 1,
                onDismiss = {},
                onQuantityChanged = {},
                onConfirm = {},
                isConfirmEnabled = true
            )
        }
    }
}
