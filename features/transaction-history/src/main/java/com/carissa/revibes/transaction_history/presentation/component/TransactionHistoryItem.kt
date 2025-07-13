/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Surface
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.transaction_history.R
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData
import kotlinx.collections.immutable.persistentListOf
import java.util.Locale

@Composable
fun TransactionHistoryItem(
    data: TransactionHistoryData,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val contentScope = remember { TransactionHistoryContentScope() }
    Surface(
        onClick = onClick,
        modifier = modifier,
        color = RevibesTheme.colors.tertiary,
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            val imageModifier = Modifier
                .size(100.dp, 75.dp)
                .clip(RoundedCornerShape(16.dp))
            if (LocalInspectionMode.current) {
                Image(
                    painterResource(R.drawable.ic_coin),
                    contentDescription = null,
                    modifier = imageModifier
                )
            } else {
                AsyncImage(data.imageUrl, contentDescription = null, modifier = imageModifier)
            }

            val contentModifier = Modifier.weight(1f)

            when (data.type) {
                TransactionHistoryData.Type.WATER_DAILY -> contentScope.WaterDaily(
                    data,
                    contentModifier
                )

                TransactionHistoryData.Type.MISSION -> contentScope.Mission(data, contentModifier)
                TransactionHistoryData.Type.REVIBES -> contentScope.Revibes(data, contentModifier)
                TransactionHistoryData.Type.COUPONS -> contentScope.Coupon(data, contentModifier)
            }

            val colors = RevibesTheme.colors
            val statusColor = remember(data.status) {
                when (data.status) {
                    TransactionHistoryData.Status.SUCCESS,
                    TransactionHistoryData.Status.COMPLETED -> colors.onTertiary

                    TransactionHistoryData.Status.PROCESS -> colors.secondary
                }
            }

            Text(
                data.status.toString().lowercase()
                    .replaceFirstChar {
                        if (it.isLowerCase()) {
                            it.titlecase(Locale.getDefault())
                        } else {
                            it.toString()
                        }
                    },
                style = RevibesTheme.typography.body2.copy(
                    color = statusColor
                )
            )
        }
    }
}

@Composable
@Preview(name = "Water Daily Transaction")
private fun TransactionHistoryItemWaterDailyPreview() {
    RevibesTheme {
        TransactionHistoryItem(
            TransactionHistoryData.dummy().copy(
                type = TransactionHistoryData.Type.WATER_DAILY,
                title = "Water Daily Challenge",
                waterDays = 5,
                coinReceive = 50,
                status = TransactionHistoryData.Status.SUCCESS
            )
        )
    }
}

@Composable
@Preview(name = "Mission Transaction")
private fun TransactionHistoryItemMissionPreview() {
    RevibesTheme {
        TransactionHistoryItem(
            TransactionHistoryData.dummy().copy(
                type = TransactionHistoryData.Type.MISSION,
                title = "Complete Daily Mission",
                coinReceive = 100,
                status = TransactionHistoryData.Status.COMPLETED
            )
        )
    }
}

@Composable
@Preview(name = "Revibes Transaction")
private fun TransactionHistoryItemRevibesPreview() {
    RevibesTheme {
        TransactionHistoryItem(
            TransactionHistoryData.dummy().copy(
                type = TransactionHistoryData.Type.REVIBES,
                title = "Waste Drop-off",
                validatorName = "Green Validator Center",
                location = "Jakarta Selatan",
                items = persistentListOf("Plastic Bottles", "Paper", "Cardboard"),
                status = TransactionHistoryData.Status.SUCCESS
            )
        )
    }
}

@Composable
@Preview(name = "Coupons Transaction")
private fun TransactionHistoryItemCouponsPreview() {
    RevibesTheme {
        TransactionHistoryItem(
            TransactionHistoryData.dummy().copy(
                type = TransactionHistoryData.Type.COUPONS,
                title = "Shopee Discount Voucher",
                validUntil = "2025-02-15",
                time = "14:30 PM",
                coinPrice = 250,
                status = TransactionHistoryData.Status.SUCCESS
            )
        )
    }
}

@Composable
@Preview(name = "Process Status Transaction")
private fun TransactionHistoryItemProcessPreview() {
    RevibesTheme {
        TransactionHistoryItem(
            TransactionHistoryData.dummy().copy(
                type = TransactionHistoryData.Type.REVIBES,
                title = "Waste Validation in Progress",
                validatorName = "Eco Validator Hub",
                location = "Bandung",
                items = persistentListOf("Glass Bottles", "Metal Cans"),
                status = TransactionHistoryData.Status.PROCESS
            )
        )
    }
}

@Composable
@Preview(name = "All Transaction Types", group = "Transaction Types")
private fun TransactionHistoryItemAllTypesPreview() {
    RevibesTheme {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            TransactionHistoryItem(
                TransactionHistoryData.dummy().copy(
                    type = TransactionHistoryData.Type.WATER_DAILY,
                    title = "Water Daily Challenge",
                    waterDays = 3,
                    coinReceive = 30
                )
            )

            TransactionHistoryItem(
                TransactionHistoryData.dummy().copy(
                    type = TransactionHistoryData.Type.MISSION,
                    title = "Weekly Mission Complete",
                    coinReceive = 150
                )
            )

            TransactionHistoryItem(
                TransactionHistoryData.dummy().copy(
                    type = TransactionHistoryData.Type.REVIBES,
                    title = "Waste Drop-off",
                    validatorName = "EcoCenter Jakarta",
                    location = "Jakarta Pusat",
                    items = persistentListOf("Plastic", "Paper")
                )
            )

            TransactionHistoryItem(
                TransactionHistoryData.dummy().copy(
                    type = TransactionHistoryData.Type.COUPONS,
                    title = "Food Discount Coupon",
                    validUntil = "2025-03-01",
                    coinPrice = 200
                )
            )
        }
    }
}
