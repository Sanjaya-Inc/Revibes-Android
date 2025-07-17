package com.carissa.revibes.home_admin.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Surface
import com.carissa.revibes.core.presentation.components.components.Text

@Composable
fun AdminMenuCard(
    title: String,
    description: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = RevibesTheme.typography.h3,
                    color = RevibesTheme.colors.primary
                )
                Text(
                    text = description,
                    style = RevibesTheme.typography.body2,
                    color = RevibesTheme.colors.onSurface
                )
            }

            Icon(
                imageVector = Icons.Default.ArrowForward,
                contentDescription = "Navigate",
                tint = RevibesTheme.colors.primary
            )
        }
    }
}

@Composable
@Preview
private fun AdminMenuCardPreview() {
    RevibesTheme {
        AdminMenuCard(
            title = "Manage Users",
            description = "View and manage user accounts"
        )
    }
}
