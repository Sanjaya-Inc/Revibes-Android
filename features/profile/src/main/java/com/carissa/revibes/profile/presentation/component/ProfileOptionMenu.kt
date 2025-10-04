/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.profile.presentation.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Surface
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.profile.R

@Composable
fun ProfileOptionMenu(
    @DrawableRes icon: Int,
    menuName: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = RevibesTheme.colors.onPrimary,
        contentColor = RevibesTheme.colors.primary,
        shape = RoundedCornerShape(16.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                colorFilter = ColorFilter.tint(RevibesTheme.colors.primary),
                modifier = Modifier
                    .padding(start = 16.dp).size(32.dp)
            )
            Text(
                text = menuName,
                modifier = Modifier.weight(1f),
                style = RevibesTheme.typography.body1,
                color = RevibesTheme.colors.primary
            )
            Text(
                text = ">",
                style = RevibesTheme.typography.h1,
                color = RevibesTheme.colors.primary,
                modifier = Modifier.padding(end = 8.dp)
            )
        }
    }
}

@Composable
@Preview
private fun ProfileOptionMenuPreview() {
    RevibesTheme {
        ProfileOptionMenu(
            R.drawable.ic_profile,
            "Your Profile"
        )
    }
}
