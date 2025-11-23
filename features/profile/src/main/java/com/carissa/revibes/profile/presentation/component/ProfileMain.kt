/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.profile.presentation.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Surface
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.profile.R

@Composable
fun ProfileMain(
    userData: UserData,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier.fillMaxWidth(),
        color = RevibesTheme.colors.onPrimary,
        contentColor = RevibesTheme.colors.primary,
        shape = RoundedCornerShape(32.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(userData.name, style = RevibesTheme.typography.h1)
                    if (userData.verified) {
                        Text("✅", style = RevibesTheme.typography.body1)
                    }
                }
                Text(userData.email, style = RevibesTheme.typography.body1)
                Text(userData.phoneNumber, style = RevibesTheme.typography.body1)
                Spacer(modifier = Modifier.height(32.dp))
                Row {
                    Image(
                        painter = painterResource(R.drawable.ic_coin),
                        contentDescription = "Coins",
                        modifier = Modifier.size(24.dp)
                    )
                    Text(userData.coins.toString(), style = RevibesTheme.typography.h2)
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            val profilePictModifier = Modifier
                .size(120.dp)
                .clip(CircleShape)
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(R.drawable.ic_profile),
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(RevibesTheme.colors.primary),
                    modifier = profilePictModifier
                )
            } else {
                AsyncImage(
                    userData.profilePictureUrl,
                    contentDescription = "Profile Picture",
                    modifier = profilePictModifier
                )
            }
        }
    }
}

@Composable
@Preview
private fun ProfileMainPreview() {
    RevibesTheme {
        ProfileMain(UserData.dummy())
    }
}
