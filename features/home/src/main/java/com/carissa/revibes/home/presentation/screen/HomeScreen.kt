/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.screen

import android.R.attr.onClick
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import com.carissa.revibes.core.presentation.components.components.Surface
import com.carissa.revibes.home.R
import com.carissa.revibes.home.presentation.component.CtaMenu
import com.carissa.revibes.home.presentation.component.CtaYourPoint
import com.carissa.revibes.home.presentation.component.HomeBanner
import com.carissa.revibes.home.presentation.component.HomeFooter
import com.carissa.revibes.home.presentation.component.HomeHeader
import com.carissa.revibes.home.presentation.navigation.HomeGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<HomeGraph>(start = true)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    HomeScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<HomeScreenUiEvent> = EventReceiver { }
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            HomeHeader(
                uiState.searchValue,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp),
                onProfileClicked = {
                    eventReceiver.onEvent(HomeScreenUiEvent.NavigateToProfile)
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            item {
                HomeBanner(
                    uiState.banners,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                )
            }

            item {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                ) {
                    CtaYourPoint(
                        uiState.userPoints,
                        modifier = Modifier
                            .height(250.dp)
                    )
                    Column {
                        Row {
                            CtaMenu(
                                stringResource(R.string.cta_drop_off),
                                R.drawable.cta_dropoff,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    eventReceiver.onEvent(HomeScreenUiEvent.NavigateToDropOff)
                                }
                            )
                            CtaMenu(
                                stringResource(R.string.cta_pick_up),
                                R.drawable.cta_pickup,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    eventReceiver.onEvent(HomeScreenUiEvent.NavigateToPickUp)
                                }
                            )

                            if (uiState.isAdmin) {
                                CtaMenu(
                                    "Admin Menu",
                                    R.drawable.girl_character,
                                    onClick = {
                                        eventReceiver.onEvent(HomeScreenUiEvent.NavigateToAdminMenu)
                                    }
                                )
                            }
                        }
                        Row {
                            CtaMenu(
                                stringResource(R.string.cta_exchange_points),
                                R.drawable.cta_exchange_points,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    eventReceiver.onEvent(HomeScreenUiEvent.NavigateToExchangePoints)
                                }
                            )
                            CtaMenu(
                                stringResource(R.string.cta_shop),
                                R.drawable.cta_shop,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    eventReceiver.onEvent(HomeScreenUiEvent.NavigateToShop)
                                }
                            )
                            CtaMenu(
                                stringResource(R.string.cta_transacion_history),
                                R.drawable.cta_transaction_history,
                                modifier = Modifier.weight(1f),
                                onClick = {
                                    eventReceiver.onEvent(HomeScreenUiEvent.NavigateToTransactionHistory)
                                }
                            )
                        }
                    }
                }
            }

            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Surface(
                            color = Color.White,
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth(0.87f)
                        ) {
                            AsyncImage(
                                R.drawable.main_logo,
                                contentDescription = "Logo",
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                        Button(
                            variant = ButtonVariant.Secondary,
                            text = stringResource(R.string.cta_help_center),
                            modifier = Modifier.fillMaxWidth(0.72f),
                            onClick = {
                                eventReceiver.onEvent(HomeScreenUiEvent.NavigateToHelpCenter)
                            }
                        )
                        Button(
                            variant = ButtonVariant.Secondary,
                            text = stringResource(R.string.about_us),
                            modifier = Modifier.fillMaxWidth(0.72f),
                            onClick = {
                                eventReceiver.onEvent(HomeScreenUiEvent.NavigateToAboutUs)
                            }
                        )
                    }

                    AsyncImage(
                        R.drawable.char_assistant,
                        contentDescription = "Char Assistant",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .width(95.dp)
                    )
                }
            }

            item {
                HomeFooter(uiState.footerItems)
            }
        }
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    RevibesTheme {
        HomeScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = HomeScreenUiState()
        )
    }
}
