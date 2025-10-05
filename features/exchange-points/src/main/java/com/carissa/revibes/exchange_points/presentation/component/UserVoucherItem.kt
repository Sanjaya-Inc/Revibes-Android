package com.carissa.revibes.exchange_points.presentation.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.exchange_points.domain.model.UserVoucher

@Composable
fun UserVoucherItem(
    userVoucher: UserVoucher,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AsyncImage(
                model = userVoucher.imageUri,
                contentDescription = null,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = userVoucher.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = userVoucher.code,
                    style = MaterialTheme.typography.bodyMedium,
                    color = RevibesTheme.colors.primary
                )

                Box(
                    modifier = Modifier
                        .background(
                            color = when (userVoucher.status) {
                                "available" -> Color.Green.copy(alpha = 0.1f)
                                "claimed" -> Color.Yellow.copy(alpha = 0.1f)
                                "expired" -> Color.Red.copy(alpha = 0.1f)
                                else -> Color.Gray.copy(alpha = 0.1f)
                            },
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = userVoucher.status.uppercase(),
                        style = MaterialTheme.typography.labelSmall,
                        color = when (userVoucher.status) {
                            "available" -> Color.Green
                            "claimed" -> Color.Yellow
                            "expired" -> Color.Red
                            else -> Color.Gray
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun UserVoucherItemPreview() {
    RevibesTheme {
        UserVoucherItem(
            userVoucher = UserVoucher(
                id = "1",
                voucherId = "voucher1",
                status = "available",
                claimedAt = null,
                expiredAt = null,
                createdAt = "2025-10-05T13:07:29.226Z",
                updatedAt = "2025-10-05T13:07:29.226Z",
                name = "Economical Grocery Package",
                description = "Affordable Grocery Sembako Package",
                imageUri = "",
                code = "RVB-ESP-65481",
                guides = emptyList(),
                termConditions = emptyList()
            ),
            onClick = {}
        )
    }
}
