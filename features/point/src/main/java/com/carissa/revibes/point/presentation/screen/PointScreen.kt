package com.carissa.revibes.point.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.PointGoldBg
import com.carissa.revibes.core.presentation.components.PointModalBg
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.point.R
import com.carissa.revibes.point.presentation.navigation.PointGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<PointGraph>(start = true)
@Composable
fun PointScreen(
    modifier: Modifier = Modifier,
    viewModel: PointScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current

    viewModel.collectSideEffect { event ->
        when (event) {
            is PointScreenUiEvent.OnLoadDailyRewardsFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is PointScreenUiEvent.OnClaimDailyRewardFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    PointScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun PointScreenContent(
    uiState: PointScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<PointScreenUiEvent> = EventReceiver { }
) {
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            CommonHeader(
                title = stringResource(R.string.point_title),
                searchTextFieldValue = searchText,
                onTextChange = { searchText = it },
                backgroundDrawRes = R.drawable.bg_point,
                onProfileClicked = {
                    eventReceiver.onEvent(PointScreenUiEvent.NavigateToProfile)
                },
            )
        }
    ) { contentPadding ->
        if (uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = RevibesTheme.colors.primary
                )
            }
        } else {
            Column(modifier = Modifier.padding(contentPadding)) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .background(PointModalBg, RoundedCornerShape(16.dp))
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(uiState.dailyRewards) { point ->
                            val isLastItem = point == uiState.dailyRewards.last()
                            val surfaceColor = if (point.claimedAt != null) {
                                RevibesTheme.colors.background
                            } else {
                                RevibesTheme.colors.primary
                            }
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .then(
                                        if (!isLastItem) {
                                            Modifier.border(
                                                width = 1.dp,
                                                color = RevibesTheme.colors.primary,
                                                shape = RoundedCornerShape(12.dp)
                                            )
                                        } else {
                                            Modifier
                                        }
                                    )
                                    .background(
                                        color = if (isLastItem) {
                                            PointGoldBg
                                        } else if (point.claimedAt != null) {
                                            RevibesTheme.colors.primary
                                        } else {
                                            RevibesTheme.colors.background
                                        },
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = "+${point.amount}",
                                    fontWeight = FontWeight.Bold,
                                    color = surfaceColor
                                )
                                Image(
                                    painter = painterResource(
                                        if (isLastItem) R.drawable.ic_coins else R.drawable.ic_coin
                                    ),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .then(
                                            if (isLastItem) Modifier else Modifier.padding(vertical = 3.dp)
                                        )
                                        .size(if (isLastItem) 30.dp else 24.dp),
                                )
                                Text(
                                    text = "Day ${point.dayIndex}",
                                    fontSize = 12.sp,
                                    color = surfaceColor
                                )
                            }
                        }
                    }
                    Text(
                        text = "Check this 7 days & Earn Points up to 50k coins",
                        fontSize = 12.sp,
                        color = RevibesTheme.colors.primary,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Button(
                        text = "CHECK-IN TODAY",
                        onClick = { eventReceiver.onEvent(PointScreenUiEvent.ClaimDailyReward) },
                        modifier = Modifier.fillMaxWidth(),
                        loading = uiState.isClaimingReward,
                        enabled = uiState.allowedToClaimReward && !uiState.isClaimingReward,
                    )
                }

                Column(
                    modifier = Modifier
                        .background(
                            color = RevibesTheme.colors.primary,
                            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                        )
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 16.dp, bottom = 4.dp)
                ) {
                    Text(
                        text = "Complete New Missions to Earn Points",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = RevibesTheme.colors.background,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        itemsIndexed(items = uiState.missions) { index, (title, desc, point) ->
                            MissionCard(
                                isRecycleIcon = title.contains("Change"),
                                title = title,
                                description = desc,
                                point = point
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MissionCard(isRecycleIcon: Boolean, title: String, description: String, point: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val icon: Painter = if (isRecycleIcon) {
                painterResource(id = R.drawable.ic_recycle)
            } else {
                painterResource(id = R.drawable.ic_account)
            }

            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 12.dp)
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.SemiBold,
                    color = RevibesTheme.colors.primary,
                    fontSize = 14.sp
                )
                Text(
                    text = description,
                    color = RevibesTheme.colors.primary.copy(alpha = 0.7f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(R.drawable.ic_coin),
                        contentDescription = null,
                        modifier = Modifier.size(16.dp),
                    )
                    Text(text = "$point Points", fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go",
                tint = Color.Gray
            )
        }
    }
}

@Composable
@Preview
private fun PointScreenPreview() {
    RevibesTheme {
        PointScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = PointScreenUiState(
                dailyRewards = listOf(
                    DailyReward("1", 1, 5, "2023-10-01T10:00:00Z"),
                    DailyReward("2", 2, 15),
                    DailyReward("3", 3, 20),
                    DailyReward("4", 4, 10),
                    DailyReward("5", 5, 50),
                    DailyReward("6", 6, 25),
                    DailyReward("7", 7, 50)
                ),
            )
        )
    }
}
