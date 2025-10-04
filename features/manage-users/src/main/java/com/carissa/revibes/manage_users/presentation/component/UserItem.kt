package com.carissa.revibes.manage_users.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.manage_users.R
import com.carissa.revibes.manage_users.domain.model.UserDomain

@Composable
internal fun UserItem(
    user: UserDomain,
    onUserClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onUserClick(user.id) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = RevibesTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(RevibesTheme.colors.primary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = user.name.take(2).uppercase(),
                    style = RevibesTheme.typography.h3,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = user.name,
                    style = RevibesTheme.typography.h3,
                    color = RevibesTheme.colors.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = user.email,
                    style = RevibesTheme.typography.body2,
                    color = RevibesTheme.colors.onSurface.copy(alpha = 0.7f)
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.role) + ": ${getRoleDisplayName(user.role)}",
                        style = RevibesTheme.typography.label3,
                        color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = "•",
                        style = RevibesTheme.typography.label3,
                        color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                    Text(
                        text = if (user.isActive) {
                            stringResource(
                                R.string.active
                            )
                        } else {
                            stringResource(R.string.inactive)
                        },
                        style = RevibesTheme.typography.label3,
                        color = if (user.isActive) RevibesTheme.colors.success else RevibesTheme.colors.error
                    )
                }
            }

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "${user.points}",
                    style = RevibesTheme.typography.h3,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = stringResource(R.string.points),
                    style = RevibesTheme.typography.label3,
                    color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}

@Composable
private fun getRoleDisplayName(role: UserDomain.UserRole): String {
    return when (role) {
        UserDomain.UserRole.ADMIN -> stringResource(R.string.role_admin)
        UserDomain.UserRole.USER -> stringResource(R.string.role_user)
    }
}

@Preview
@Composable
private fun UserItemPreview() {
    RevibesTheme {
        UserItem(
            user = UserDomain.dummy(),
            onUserClick = {},
            modifier = Modifier.padding(16.dp)
        )
    }
}
