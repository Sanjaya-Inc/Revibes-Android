package com.carissa.revibes.core.presentation.compose.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf

@Composable
fun TransactionDetailsContent(
    customerName: String,
    locationAddress: String,
    dateLabel: String,
    date: String,
    itemDetailsTitle: String,
    items: ImmutableList<TransactionItem>,
    calculatingPointsText: String,
    totalPointsFormat: String,
    itemPointsFormat: String,
    nameLabel: String,
    locationLabel: String,
    modifier: Modifier = Modifier,
    status: String? = null,
    pointsDisclaimer: String = stringResource(R.string.points_disclaimer),
    isEstimatingPoints: Boolean = false,
    totalPoints: Int = 0,
    itemPoints: ImmutableMap<String, Int> = persistentMapOf(),
    actionButton: @Composable (() -> Unit)? = null
) {
    val context = LocalContext.current
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.White)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start
    ) {
        Image(
            painter = painterResource(R.drawable.main_logo),
            contentDescription = "Revibes Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .height(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .align(Alignment.Start)
        )
        Text(
            text = itemDetailsTitle,
            style = RevibesTheme.typography.h1,
            fontWeight = FontWeight.W500,
            color = RevibesTheme.colors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        status?.let { statusValue ->
            Text(
                text = "Status: $statusValue",
                style = RevibesTheme.typography.body1,
                fontWeight = FontWeight.Medium,
                color = RevibesTheme.colors.primary,
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(top = 8.dp)
            )
        }

        DetailRow(
            label = nameLabel,
            value = customerName,
            modifier = Modifier.padding(top = 8.dp)
        )
        DetailRow(
            label = locationLabel,
            value = locationAddress,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        DetailRow(
            label = dateLabel,
            value = date,
            modifier = Modifier.padding(vertical = 2.dp)
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = RevibesTheme.colors.primary
        )
        Text(
            text = "Item Details",
            style = RevibesTheme.typography.h4,
            fontWeight = FontWeight.W500,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp)
        )

        items.forEachIndexed { index, item ->
            DetailRow(
                label = "Item ${index + 1}",
                value = item.name
            )
            DetailRow(
                label = "Type",
                value = item.type
            )
            DetailRow(
                label = "Weight",
                value = item.weight
            )
            Spacer(Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(start = 24.dp)
            ) {
                item.photos.forEach { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            }
            Spacer(Modifier.height(16.dp))
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 8.dp),
            thickness = 1.dp,
            color = RevibesTheme.colors.primary
        )
        if (isEstimatingPoints) {
            Row(
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(16.dp),
                    strokeWidth = 2.dp,
                    color = RevibesTheme.colors.primary
                )
                Text(
                    text = calculatingPointsText,
                    style = RevibesTheme.typography.body2,
                    color = RevibesTheme.colors.primary
                )
            }
        } else {
            Text(
                text = totalPointsFormat.format(totalPoints),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 2.dp),
            )
            items.forEachIndexed { index, _ ->
                Text(
                    text = itemPointsFormat.format(
                        index + 1,
                        itemPoints[index.toString()] ?: 0
                    ),
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(vertical = 2.dp),
                )
            }
        }

        Spacer(Modifier.height(16.dp))
        Text(
            text = pointsDisclaimer,
            style = RevibesTheme.typography.label3,
            color = Color.Red,
            fontWeight = FontWeight.W500,
        )

        actionButton?.invoke()
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = "$label:",
            fontWeight = FontWeight.Bold
        )
        Text(text = value)
    }
}

data class TransactionItem(
    val id: String,
    val name: String,
    val type: String,
    val weight: String,
    val photos: List<String>
)
