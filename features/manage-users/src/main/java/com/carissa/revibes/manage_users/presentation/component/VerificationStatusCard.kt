package com.carissa.revibes.manage_users.presentation.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.manage_users.domain.model.UserDomain
@Composable
fun VerificationStatusCard(
    user: UserDomain,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    onVerificationChange: (Boolean) -> Unit = {}
) {
    val colors = RevibesTheme.colors
    val typography = RevibesTheme.typography

    val verified = user.verified
    var showDialog by remember { mutableStateOf(false) }

    val iconTint by animateColorAsState(
        targetValue = if (verified) colors.success else colors.textSecondary,
        animationSpec = spring()
    )
    val scale by animateFloatAsState(
        targetValue = if (verified) 1.1f else 1.0f,
        animationSpec = spring()
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(colors.surface),
        color = colors.surface,
        tonalElevation = 1.dp,
        shadowElevation = if (verified) 4.dp else 1.dp
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .scale(scale)
                        .clip(CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (verified) Icons.Filled.Check else Icons.Filled.Close,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (verified) "Verified Account" else "Unverified Account",
                        style = typography.body2.copy(
                            color = if (verified) colors.success else colors.textSecondary,
                            fontSize = 13.sp
                        )
                    )
                }
            }

            Button(
                onClick = { if (!isLoading) showDialog = true },
                shape = RoundedCornerShape(12.dp),
                enabled = !isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (verified) colors.error else colors.primary,
                    contentColor = colors.onPrimary,
                    disabledContainerColor = if (verified) {
                        colors.error.copy(
                            alpha = 0.6f
                        )
                    } else {
                        colors.primary.copy(alpha = 0.6f)
                    },
                    disabledContentColor = colors.onPrimary.copy(alpha = 0.7f)
                ),
                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = colors.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = if (verified) "Unverify" else "Verify",
                        style = typography.button.copy(fontWeight = FontWeight.SemiBold)
                    )
                }
            }
        }
    }

    if (showDialog) {
        VerifyConfirmationDialog(
            verified = verified,
            onDismiss = { showDialog = false },
            onConfirm = {
                showDialog = false
                onVerificationChange(!verified)
            }
        )
    }
}

@Composable
private fun VerifyConfirmationDialog(
    verified: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colors = RevibesTheme.colors
    val typography = RevibesTheme.typography

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = if (verified) "Unverify Account?" else "Verify Account?",
                style = typography.h4.copy(
                    color = colors.text,
                    fontWeight = FontWeight.Bold
                )
            )
        },
        text = {
            Text(
                text = if (verified) {
                    "Are you sure you want to mark this account as unverified?"
                } else {
                    "Confirm verifying this account. The user will gain verified privileges."
                },
                style = typography.body2.copy(color = colors.textSecondary)
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = if (verified) "Unverify" else "Verify",
                    color = if (verified) colors.error else colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(text = "Cancel", color = colors.textSecondary)
            }
        },
        containerColor = colors.background,
        tonalElevation = 2.dp
    )
}
