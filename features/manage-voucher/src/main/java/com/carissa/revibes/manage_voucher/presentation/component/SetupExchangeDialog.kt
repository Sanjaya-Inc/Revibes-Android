package com.carissa.revibes.manage_voucher.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain

@Composable
fun SetupExchangeDialog(
    voucher: VoucherDomain,
    isLoading: Boolean,
    onDismiss: () -> Unit,
    onConfirm: (amount: Int, description: String, quota: Int, endedAt: String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var amount by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var quota by remember { mutableStateOf("-1") }
    var endedAt by remember { mutableStateOf("") }
    var amountError by remember { mutableStateOf<String?>(null) }

    Dialog(
        onDismissRequest = { if (!isLoading) onDismiss() },
        properties = DialogProperties(
            dismissOnBackPress = !isLoading,
            dismissOnClickOutside = !isLoading,
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            modifier = modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = RevibesTheme.colors.background
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp)
            ) {
                // Header with emoji
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🔄",
                        style = MaterialTheme.typography.displaySmall
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Setup Exchange",
                        style = RevibesTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        color = RevibesTheme.colors.primary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Voucher info card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = RevibesTheme.colors.tertiary
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    ) {
                        Text(
                            text = "Voucher: ${voucher.name}",
                            style = RevibesTheme.typography.body1,
                            fontWeight = FontWeight.SemiBold,
                            color = RevibesTheme.colors.onTertiary
                        )
                        Text(
                            text = "Code: ${voucher.code}",
                            style = RevibesTheme.typography.body2,
                            color = RevibesTheme.colors.onTertiary.copy(alpha = 0.8f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Amount field (required)
                Text(
                    text = "Points Required *",
                    style = RevibesTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    color = RevibesTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = amount,
                    onValueChange = {
                        amount = it
                        amountError = if (it.isBlank()) {
                            "Amount is required"
                        } else if (it.toIntOrNull() == null || it.toInt() <= 0) {
                            "Amount must be a positive number"
                        } else {
                            null
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Enter points amount",
                            style = RevibesTheme.typography.body2
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "💎",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    isError = amountError != null,
                    supportingText = amountError?.let { { Text(it) } },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RevibesTheme.colors.primary,
                        unfocusedBorderColor = RevibesTheme.colors.outline,
                        errorBorderColor = RevibesTheme.colors.error
                    ),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Description field (optional)
                Text(
                    text = "Description (Optional)",
                    style = RevibesTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    color = RevibesTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Add a description",
                            style = RevibesTheme.typography.body2
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "📝",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    minLines = 2,
                    maxLines = 3,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RevibesTheme.colors.primary,
                        unfocusedBorderColor = RevibesTheme.colors.outline
                    ),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Quota field (optional)
                Text(
                    text = "Quota (Optional)",
                    style = RevibesTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    color = RevibesTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Set to -1 for unlimited",
                    style = RevibesTheme.typography.label1,
                    color = RevibesTheme.colors.textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = quota,
                    onValueChange = { quota = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "-1 (unlimited)",
                            style = RevibesTheme.typography.body2
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "📊",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RevibesTheme.colors.primary,
                        unfocusedBorderColor = RevibesTheme.colors.outline
                    ),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(16.dp))

                // End date field (optional)
                Text(
                    text = "End Date (Optional)",
                    style = RevibesTheme.typography.body2,
                    fontWeight = FontWeight.Medium,
                    color = RevibesTheme.colors.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Format: YYYY-MM-DD",
                    style = RevibesTheme.typography.label1,
                    color = RevibesTheme.colors.textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = endedAt,
                    onValueChange = { endedAt = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "2025-12-31",
                            style = RevibesTheme.typography.body2
                        )
                    },
                    leadingIcon = {
                        Text(
                            text = "📅",
                            style = MaterialTheme.typography.titleMedium
                        )
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RevibesTheme.colors.primary,
                        unfocusedBorderColor = RevibesTheme.colors.outline
                    ),
                    enabled = !isLoading
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = RevibesTheme.colors.primary
                        ),
                        enabled = !isLoading
                    ) {
                        Text(
                            text = "Cancel",
                            style = RevibesTheme.typography.button,
                            modifier = Modifier.padding(vertical = 4.dp)
                        )
                    }

                    Button(
                        onClick = {
                            val amountInt = amount.toIntOrNull()
                            if (amountInt != null && amountInt > 0) {
                                val quotaInt = quota.toIntOrNull() ?: -1
                                val endDate = endedAt.ifBlank { null }
                                onConfirm(amountInt, description, quotaInt, endDate)
                            }
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = RevibesTheme.colors.primary,
                            contentColor = RevibesTheme.colors.onPrimary
                        ),
                        enabled = !isLoading && amountError == null && amount.isNotBlank()
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.padding(vertical = 4.dp),
                                color = RevibesTheme.colors.onPrimary,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                text = "Create Exchange",
                                style = RevibesTheme.typography.button,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
