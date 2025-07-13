/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
@Preview
private fun TransactionHistoryItemPreview() {
    RevibesTheme {
        TransactionHistoryItem(TransactionHistoryData.dummy())
    }
}
