/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.home.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
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
    HomeScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun HomeScreenContent(
    uiState: HomeScreenUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier, containerColor = Color.Transparent, topBar = {
        HomeHeader(
            uiState.searchValue,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
        )
    }, bottomBar = {
        HomeFooter(uiState.footerItems)
    }) {
        Box(modifier = Modifier.padding(it))
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
