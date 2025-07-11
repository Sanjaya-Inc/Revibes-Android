/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.drop_off.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.RevibesLoading
import com.carissa.revibes.core.presentation.util.DateUtil
import com.carissa.revibes.drop_off.domain.model.StoreData
import com.carissa.revibes.drop_off.presentation.navigation.DropOffGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Serializable
data class DropOffConfirmationScreenArguments(
    val orderId: String,
    val type: String,
    val name: String,
    val store: StoreData,
    val totalPoints: Int,
    val items: List<DropOffItem>
)

@Destination<DropOffGraph>()
@Composable
fun DropOffConfirmationScreen(
    arguments: DropOffConfirmationScreenArguments,
    modifier: Modifier = Modifier,
    viewModel: DropOffConfirmationScreenViewModel = koinViewModel()
) {
    val navigator = RevibesTheme.navigator
    val state by viewModel.collectAsState()

    DropOffConfirmationWrapperScreen(
        arguments = arguments,
        onBackClick = navigator::navigateUp,
        uiState = state,
        onSubmit = {},
        modifier = modifier
    )
}

@Composable
private fun DropOffConfirmationWrapperScreen(
    arguments: DropOffConfirmationScreenArguments,
    uiState: DropOffConfirmationScreenUiState,
    onBackClick: () -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier.statusBarsPadding(),
        containerColor = RevibesTheme.colors.background,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        painter = painterResource(R.drawable.back_cta),
                        modifier = Modifier.size(86.dp),
                        tint = Color.Unspecified,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Drop Off Booking Details",
                    style = RevibesTheme.typography.h2,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f, fill = true),
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .dashedBorder(
                        color = RevibesTheme.colors.primary,
                        strokeWidth = 2.dp,
                        dashLength = 8.dp,
                        gapLength = 4.dp,
                        cornerRadius = 8.dp
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
                    .dashedBorder(
                        color = RevibesTheme.colors.primary,
                        strokeWidth = 2.dp,
                        dashLength = 8.dp,
                        gapLength = 4.dp,
                        cornerRadius = 8.dp
                    )
                    .padding(16.dp)
            ) {
                DropOffConfirmationScreenContent(
                    arguments = arguments,
                    uiState = uiState,
                    onSubmit = onSubmit,
                )
            }
        }
    }
}

@Composable
private fun DropOffConfirmationScreenContent(
    arguments: DropOffConfirmationScreenArguments,
    uiState: DropOffConfirmationScreenUiState,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier
) {
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
            "DELIVERY DETAILS",
            style = RevibesTheme.typography.h1,
            fontWeight = FontWeight.W500,
            color = RevibesTheme.colors.primary,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        DetailRow(
            "Name",
            arguments.name,
            Modifier.padding(top = 8.dp)
        )
        DetailRow(
            "Location Drop Off",
            arguments.store.address,
            Modifier.padding(vertical = 2.dp)
        )
        DetailRow(
            "Drop Off Date",
            DateUtil.getTodayDate(),
            Modifier.padding(vertical = 2.dp)
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

        arguments.items.forEachIndexed { index, item ->
            DetailRow("Item ${index + 1}", item.name)
            DetailRow("Type", item.type)
            DetailRow("Weight", item.weight?.first.orEmpty())
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
        Text(
            text = "Total Points: ${arguments.totalPoints} Points",
            modifier = Modifier
                .align(Alignment.End)
                .padding(vertical = 2.dp),
        )
        arguments.items.forEachIndexed { index, item ->
            Text(
                text = "Item ${index + 1}: ${item.point} Points",
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(vertical = 2.dp),
            )
        }

        Spacer(Modifier.height(16.dp))
        Text(
            "*The points above are temporary. Points will change after the Revibe team " +
                "re-checks the items regarding the type of item and weight of the item.\n*Please" +
                " double check your shipping data details before submit.",
            style = RevibesTheme.typography.label3.copy(color = Color.Red),
            fontWeight = FontWeight.W500,
        )

        Button(
            text = "SUBMIT",
            onClick = onSubmit,
            enabled = true,
            loading = uiState.isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 8.dp),
        )
    }
}

@Composable
fun DetailRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text("$label:", fontWeight = FontWeight.Bold)
        Text(value)
    }
}

@Composable
@Preview(showBackground = true, showSystemUi = true)
private fun DropOffConfirmationScreenPreview() {
    RevibesTheme {
        DropOffConfirmationWrapperScreen(
            arguments = DropOffConfirmationScreenArguments(
                orderId = "12345",
                type = "Electronics",
                name = "John Doe",
                store = StoreData(
                    id = "store1",
                    name = "Tech Store",
                    address = "123 Tech Lane, Silicon Valley, CA",
                    latitude = 37.7749,
                    longitude = -122.4194,
                    country = "ID",
                    postalCode = "94016",
                    distance = 200.3,
                    status = "success",
                ),
                totalPoints = 200,
                items = listOf(
                    DropOffItem(
                        id = "1",
                        name = "Laptop",
                        type = "organic",
                        weight = "> 1 kg" to 1,
                        point = 100,
                        photos = listOf(
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShZRhY" +
                                "JuVc5eH192WQSGqveK-Qe1q-l5Fzkw&s",
                            "https://images.theconversation.com/files/529526/original/file-2023" +
                                "0601-15-v2l0pv.jpg?ixlib=rb-4.1.0&rect=64%2C0%2C898%2C449&q=" +
                                "45&auto=format&w=668&h=324&fit=crop"
                        )
                    ),
                    DropOffItem(
                        id = "2",
                        name = "Smartphone",
                        type = "organic",
                        weight = "> 10 kg" to 10,
                        point = 50,
                        photos = listOf(
                            "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShZRhY" +
                                "JuVc5eH192WQSGqveK-Qe1q-l5Fzkw&s",
                            "https://images.theconversation.com/files/529526/original/file-2023" +
                                "0601-15-v2l0pv.jpg?ixlib=rb-4.1.0&rect=64%2C0%2C898%2C44" +
                                "9&q=45&auto=format&w=668&h=324&fit=crop"
                        )
                    )
                )
            ),
            modifier = Modifier.background(Color.White),
            onBackClick = {},
            onSubmit = {},
            uiState = DropOffConfirmationScreenUiState(isLoading = false)
        )
    }
}

private fun Modifier.dashedBorder(
    color: Color,
    strokeWidth: Dp = 1.dp,
    dashLength: Dp = 10.dp,
    gapLength: Dp = 10.dp,
    cornerRadius: Dp = 0.dp
): Modifier = this.then(
    Modifier.drawBehind {
        val stroke = Stroke(
            width = strokeWidth.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                floatArrayOf(dashLength.toPx(), gapLength.toPx()),
                0f
            )
        )

        drawRoundRect(
            color = color,
            size = size,
            style = stroke,
            cornerRadius = CornerRadius(cornerRadius.toPx())
        )
    }
)
