/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.drop_off.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.DashedBorderContainer
import com.carissa.revibes.core.presentation.compose.components.TransactionDetailsContent
import com.carissa.revibes.core.presentation.compose.components.TransactionItem
import com.carissa.revibes.core.presentation.util.DateUtil
import com.carissa.revibes.drop_off.domain.model.StoreData
import com.carissa.revibes.drop_off.presentation.navigation.DropOffGraph
import com.carissa.revibes.drop_off.presentation.screen.DropOffConfirmationScreenUiEvent.MakeOrder
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.toImmutableList
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Serializable
data class DropOffConfirmationScreenArguments(
    val orderId: String,
    val type: String,
    val name: String,
    val store: StoreData,
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
    val context = LocalContext.current

    LaunchedEffect(arguments) {
        viewModel.onEvent(DropOffConfirmationScreenUiEvent.InitializeScreen(arguments))
    }

    viewModel.collectSideEffect { event ->
        when (event) {
            is DropOffConfirmationScreenUiEvent.OnMakeOrderFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    DropOffConfirmationWrapperScreen(
        arguments = arguments,
        onBackClick = navigator::navigateUp,
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

private fun DropOffItem.toTransactionItem(): TransactionItem {
    return TransactionItem(
        id = this.id,
        name = this.name,
        type = this.type,
        weight = this.weight?.first.orEmpty(),
        photos = this.photos
    )
}

@Composable
private fun DropOffConfirmationWrapperScreen(
    arguments: DropOffConfirmationScreenArguments,
    uiState: DropOffConfirmationScreenUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<DropOffConfirmationScreenUiEvent> = EventReceiver { },
) {
    val context = LocalContext.current
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
                        contentDescription = context.getString(com.carissa.revibes.drop_off.R.string.back_desc)
                    )
                }
                Text(
                    text = context.getString(com.carissa.revibes.drop_off.R.string.drop_off_booking_details),
                    style = RevibesTheme.typography.h2,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.weight(1f, fill = true),
                )
            }
        }
    ) { paddingValues ->
        DashedBorderContainer(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            TransactionDetailsContent(
                customerName = arguments.name,
                locationAddress = arguments.store.address,
                dateLabel = context.getString(com.carissa.revibes.drop_off.R.string.drop_off_date_label),
                date = DateUtil.getTodayDate(),
                itemDetailsTitle = context.getString(com.carissa.revibes.drop_off.R.string.delivery_details),
                items = arguments.items.map { it.toTransactionItem() }.toImmutableList(),
                isEstimatingPoints = uiState.isEstimatingPoints,
                totalPoints = uiState.totalPoints,
                itemPoints = uiState.itemPoints.toImmutableMap(),
                calculatingPointsText = context.getString(com.carissa.revibes.drop_off.R.string.calculating_points),
                totalPointsFormat = context.getString(com.carissa.revibes.drop_off.R.string.total_points_format),
                itemPointsFormat = context.getString(com.carissa.revibes.drop_off.R.string.item_points_format),
                nameLabel = context.getString(com.carissa.revibes.drop_off.R.string.name_label),
                locationLabel = context.getString(com.carissa.revibes.drop_off.R.string.location_drop_off_label),
                actionButton = {
                    Button(
                        text = context.getString(com.carissa.revibes.drop_off.R.string.submit_button),
                        onClick = { eventReceiver.onEvent(event = MakeOrder(arguments = arguments)) },
                        enabled = true,
                        loading = uiState.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 8.dp),
                    )
                }
            )
        }
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
                items = listOf(
                    DropOffItem(
                        id = "1",
                        name = "Laptop",
                        type = "organic",
                        weight = "> 1 kg" to 1,
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
            uiState = DropOffConfirmationScreenUiState(isLoading = false)
        )
    }
}
