/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.home.R

@Composable
fun CtaYourPoint(
    yourPoint: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    androidx.compose.material3.Surface(
        modifier = modifier,
        color = Color.Transparent,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(width = 1.dp, color = RevibesTheme.colors.primary),
        onClick = onClick
    ) {
        Box {
            AsyncImage(
                R.drawable.bg_your_points,
                contentDescription = "Background Image",
                modifier = Modifier.matchParentSize(),
                contentScale = ContentScale.Crop
            )
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.cta_your_point),
                    style = RevibesTheme.typography.h2,
                    color = RevibesTheme.colors.primary
                )
                AsyncImage(
                    R.drawable.object_your_point,
                    contentDescription = "Coin Icon",
                    modifier = Modifier.width(100.dp).weight(1f)
                )
                Text(
                    yourPoint.toString(),
                    style = RevibesTheme.typography.h2,
                    color = RevibesTheme.colors.primary
                )
            }
        }
    }
}

@Composable
@Preview
private fun CtaYourPointPreview() {
    RevibesTheme {
        CtaYourPoint(1306)
    }
}
