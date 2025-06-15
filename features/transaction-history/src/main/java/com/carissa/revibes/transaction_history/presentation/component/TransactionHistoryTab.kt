/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.transaction_history.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant

@Composable
fun TransactionHistoryTab(
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val variant = remember(isSelected) {
        if (isSelected) {
            ButtonVariant.Primary
        } else {
            ButtonVariant.PrimaryOutlined
        }
    }
    Button(onClick = onClick, variant = variant, text = text, modifier = modifier)
}

@Composable
@Preview
private fun TransactionHistoryTabPreview() {
    RevibesTheme {
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            TransactionHistoryTab("Process", false, modifier = Modifier.weight(1f))
            TransactionHistoryTab("Transaction Complete", true, modifier = Modifier.weight(1f))
        }
    }
}
