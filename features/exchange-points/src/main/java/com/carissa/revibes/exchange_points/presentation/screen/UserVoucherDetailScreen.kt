/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.exchange_points.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.CommonHeader
import com.carissa.revibes.core.presentation.compose.components.SearchConfig
import com.carissa.revibes.exchange_points.R
import com.carissa.revibes.exchange_points.domain.model.UserVoucher
import com.carissa.revibes.exchange_points.presentation.navigation.ExchangePointsGraph
import com.ramcosta.composedestinations.annotation.Destination

@Destination<ExchangePointsGraph>
@Composable
fun UserVoucherDetailScreen(
    userVoucher: UserVoucher,
    modifier: Modifier = Modifier
) {
    UserVoucherDetailScreenContent(
        userVoucher = userVoucher,
        modifier = modifier,
    )
}

@Composable
private fun UserVoucherDetailScreenContent(
    userVoucher: UserVoucher,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White),
        topBar = {
            CommonHeader(
                title = "Voucher Detail",
                searchConfig = SearchConfig.None,
                backgroundDrawRes = R.drawable.bg_exchange_points,
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                // Voucher Image
                AsyncImage(
                    model = userVoucher.imageUri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            item {
                // Voucher Info Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = userVoucher.name,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = userVoucher.description,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Voucher Code:",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Text(
                                text = userVoucher.code,
                                style = MaterialTheme.typography.bodyLarge,
                                color = RevibesTheme.colors.primary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Status:",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Medium
                            )
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = when (userVoucher.status) {
                                            "available" -> Color.Green.copy(alpha = 0.1f)
                                            "claimed" -> Color.Yellow.copy(alpha = 0.1f)
                                            "expired" -> Color.Red.copy(alpha = 0.1f)
                                            else -> Color.Gray.copy(alpha = 0.1f)
                                        },
                                        shape = RoundedCornerShape(6.dp)
                                    )
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = userVoucher.status.uppercase(),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = when (userVoucher.status) {
                                        "available" -> Color.Green
                                        "claimed" -> Color.Yellow
                                        "expired" -> Color.Red
                                        else -> Color.Gray
                                    },
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (userVoucher.claimedAt != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Claimed At:",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = userVoucher.claimedAt,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        if (userVoucher.expiredAt != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Expires At:",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Medium
                                )
                                Text(
                                    text = userVoucher.expiredAt,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            if (userVoucher.guides.isNotEmpty()) {
                item {
                    // How to Use Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "How to Use",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                items(userVoucher.guides) { guide ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium,
                            color = RevibesTheme.colors.primary
                        )
                        Text(
                            text = guide,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            if (userVoucher.termConditions.isNotEmpty()) {
                item {
                    // Terms & Conditions Card
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "Terms & Conditions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                items(userVoucher.termConditions) { term ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = "•",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = term,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun UserVoucherDetailScreenPreview() {
    RevibesTheme {
        UserVoucherDetailScreenContent(
            userVoucher = UserVoucher(
                id = "1",
                voucherId = "voucher1",
                status = "available",
                claimedAt = null,
                expiredAt = "2025-11-05T13:07:29.226Z",
                createdAt = "2025-10-05T13:07:29.226Z",
                updatedAt = "2025-10-05T13:07:29.226Z",
                name = "Economical Grocery Package",
                description = "Affordable Grocery Sembako Package\n" +
                    "Example (while stock last)\n• Rice 1 kg\n" +
                    "• Sugar 500 gr\n• Salt 250 gr",
                imageUri = "",
                code = "RVB-ESP-65481",
                guides = listOf(
                    "Claim the voucher in the Revibes app",
                    "Show the voucher code / QR at Revibes merchants",
                    "Redeem for the affordable grocery package"
                ),
                termConditions = listOf(
                    "Valid for one-time redemption",
                    "Cannot be exchanged for cash or other items",
                    "Valid only at Revibes merchants/partners",
                    "Valid for 30 days from the date of claim",
                    "While supplies last"
                )
            )
        )
    }
}
