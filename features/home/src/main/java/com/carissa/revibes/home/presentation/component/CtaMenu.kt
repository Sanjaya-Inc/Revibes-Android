/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Surface
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.home.R

@Composable
fun CtaMenu(
    title: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            color = Color.Transparent,
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(width = 1.dp, color = RevibesTheme.colors.primary),
            onClick = onClick
        ) {
            AsyncImage(
                icon,
                contentDescription = "Menu Icon",
                modifier = Modifier
                    .size(100.dp)
                    .padding(16.dp)
            )
        }
        Text(
            text = title,
            style = RevibesTheme.typography.body1,
            color = RevibesTheme.colors.primary,
            textAlign = TextAlign.Center,
            maxLines = 1,
            modifier = Modifier.basicMarquee()
        )
    }
}

@Composable
@Preview
private fun CtaMenuPreview() {
    RevibesTheme {
        CtaMenu(title = "test", icon = R.drawable.ic_fax)
    }
}
