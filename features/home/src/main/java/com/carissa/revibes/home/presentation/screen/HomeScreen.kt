/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.home.data.model.HomeBannerData
import com.carissa.revibes.home.presentation.component.HomeBanner
import com.carissa.revibes.home.presentation.component.HomeFooter
import com.carissa.revibes.home.presentation.component.HomeHeader
import com.carissa.revibes.home.presentation.navigation.HomeGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
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
    Scaffold(modifier, containerColor = Color.Transparent, topBar = {
        HomeHeader(
            uiState.searchValue,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp),
            onProfileClicked = {
                eventReceiver.onEvent(HomeScreenUiEvent.NavigateToProfile)
            }
        )
    }, bottomBar = {
        HomeFooter(uiState.footerItems)
    }) {
        Column(
            modifier = Modifier
                .padding(it)
                .padding(16.dp)
                .verticalScroll(
                    rememberScrollState()
                )
        ) {
            HomeBanner(
                banners = persistentListOf(
                    HomeBannerData(
                        imageUrl = "https://sample-files.com/downloads/images/jpg/landscape_hires_4000x2667_6.83mb.jpg",
                        deeplink = ""
                    ),
                    HomeBannerData(
                        imageUrl = "https://sample-files.com/downloads/images/jpg/landscape_hires_4000x2667_6.83mb.jpg",
                        deeplink = ""
                    ),
                    HomeBannerData(
                        imageUrl = "https://sample-files.com/downloads/images/jpg/landscape_hires_4000x2667_6.83mb.jpg",
                        deeplink = ""
                    )
                )
            )
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
