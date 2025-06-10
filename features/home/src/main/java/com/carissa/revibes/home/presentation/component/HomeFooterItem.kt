package com.carissa.revibes.home.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.home.R

@Composable
fun HomeFooterItem(
    data: FooterItem,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(data.icon),
            contentDescription = null,
            modifier = Modifier.size(32.dp),
            tint = data.color ?: RevibesTheme.colors.onPrimary
        )
        Text(
            data.value,
            color = RevibesTheme.colors.onPrimary,
            modifier = Modifier.weight(1f),
            style = RevibesTheme.typography.body1
        )
    }
}

@Composable
@Preview
private fun HomeFooterItemPreview() {
    RevibesTheme {
        Box(modifier = Modifier.background(RevibesTheme.colors.primary)) {
            HomeFooterItem(FooterItem(R.drawable.ic_profile, "Test"))
        }
    }
}
