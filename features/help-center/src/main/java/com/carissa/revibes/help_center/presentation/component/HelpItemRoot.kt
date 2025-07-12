/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.help_center.presentation.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.help_center.presentation.model.HelpCenterChildData
import com.carissa.revibes.help_center.presentation.model.HelpCenterRootData

@Composable
fun HelpItemRoot(
    data: HelpCenterRootData,
    modifier: Modifier = Modifier,
    onChildClicked: (HelpCenterChildData) -> Unit = {}
) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = data.icon,
                colorFilter = ColorFilter.tint(RevibesTheme.colors.primary),
                contentDescription = null,
                modifier = Modifier.size(32.dp)
            )
            Text(
                data.title,
                style = RevibesTheme.typography.h2,
                color = RevibesTheme.colors.primary
            )
        }
        data.children.forEachIndexed { index, data ->
            HelpItemChild(
                index + 1,
                data,
                modifier = Modifier
                    .padding(start = 36.dp)
                    .clickable {
                        onChildClicked(data)
                    }
            )
        }
    }
}

@Composable
@Preview
private fun HelpItemRootPreview() {
    RevibesTheme {
        HelpItemRoot(HelpCenterRootData())
    }
}
