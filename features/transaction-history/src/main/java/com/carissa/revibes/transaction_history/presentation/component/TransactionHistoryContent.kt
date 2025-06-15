/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.transaction_history.R
import com.carissa.revibes.transaction_history.data.model.TransactionHistoryData

@Stable
class TransactionHistoryContentScope {
    @Composable
    fun WaterDaily(
        data: TransactionHistoryData,
        modifier: Modifier = Modifier
    ) {
        Column(modifier) {
            Text(data.date, style = RevibesTheme.typography.body3)
            Text(data.title, style = RevibesTheme.typography.body1)
            Text("Day-${data.waterDays}", style = RevibesTheme.typography.body3)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Image(
                    painterResource(R.drawable.ic_coin),
                    contentDescription = "Coin",
                    modifier = Modifier.size(20.dp)
                )
                Text(data.coinReceive.toString(), style = RevibesTheme.typography.body3)
            }
        }
    }

    @Composable
    fun Mission(
        data: TransactionHistoryData,
        modifier: Modifier = Modifier
    ) {
        Column(modifier) {
            Text(data.date, style = RevibesTheme.typography.body3)
            Text(data.title, style = RevibesTheme.typography.body1)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Image(
                    painterResource(R.drawable.ic_coin),
                    contentDescription = "Coin",
                    modifier = Modifier.size(20.dp)
                )
                Text(data.coinReceive.toString(), style = RevibesTheme.typography.body3)
            }
        }
    }

    @Composable
    fun Revibes(
        data: TransactionHistoryData,
        modifier: Modifier = Modifier
    ) {
        Column(modifier) {
            Text(data.date, style = RevibesTheme.typography.body3)
            Text(data.validatorName, style = RevibesTheme.typography.body1)
            Text(data.location, style = RevibesTheme.typography.body3)
            Text(
                "${data.items.count()} Item - ${data.items.joinToString(" ; ")}",
                style = RevibesTheme.typography.body3
            )
        }
    }

    @Composable
    fun Coupon(
        data: TransactionHistoryData,
        modifier: Modifier = Modifier
    ) {
        Column(modifier) {
            Text("${data.date} | ${data.time}", style = RevibesTheme.typography.body3)
            Text(data.title, style = RevibesTheme.typography.body1)
            Text("Valid until ${data.validUntil}", style = RevibesTheme.typography.body3)
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Image(
                    painterResource(R.drawable.ic_coin),
                    contentDescription = "Coin",
                    modifier = Modifier.size(20.dp)
                )
                Text(data.coinPrice.toString(), style = RevibesTheme.typography.body3)
            }
        }
    }
}

@Composable
@Preview
private fun TransactionHistoryContentPreview() {
    RevibesTheme {
        val scope = remember { TransactionHistoryContentScope() }
        scope.WaterDaily(
            TransactionHistoryData.dummy().copy(
                type = TransactionHistoryData.Type.WATER_DAILY
            )
        )
    }
}
