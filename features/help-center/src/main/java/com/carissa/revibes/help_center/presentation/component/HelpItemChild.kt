/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.help_center.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.help_center.presentation.model.HelpCenterChildData

@Composable
fun HelpItemChild(
    number: Int,
    data: HelpCenterChildData,
    modifier: Modifier = Modifier
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .fillMaxWidth()
                .background(RevibesTheme.colors.tertiary)
        ) {
            Row(modifier = Modifier.padding(start = 16.dp, end = 8.dp, top = 4.dp, bottom = 4.dp)) {
                Text(
                    "$number. ${data.question}",
                    style = RevibesTheme.typography.body1,
                    color = RevibesTheme.colors.primary,
                    modifier = Modifier.weight(1f)
                )
                val rotation = remember(data.isExpanded) {
                    if (data.isExpanded) {
                        90f
                    } else {
                        0f
                    }
                }
                Text(">", modifier = Modifier.rotate(rotation), textAlign = TextAlign.Center)
            }
        }

        AnimatedVisibility(data.isExpanded) {
            Text(
                data.answer,
                style = RevibesTheme.typography.body2,
                color = RevibesTheme.colors.primary,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 24.dp)
            )
        }
    }
}

@Composable
@Preview
private fun HelpItemChildPreview() {
    RevibesTheme {
        HelpItemChild(1, HelpCenterChildData(isExpanded = true))
    }
}
