/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.profile.presentation.screen

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
import com.carissa.revibes.profile.presentation.component.ProfileHeader
import com.carissa.revibes.profile.presentation.navigation.ProfileGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<ProfileGraph>(start = true)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    ProfileScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileScreenUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier, containerColor = Color.Transparent, topBar = {
        ProfileHeader(uiState.searchValue, modifier = Modifier.statusBarsPadding().padding(16.dp))
    }) {
        Box(modifier = Modifier.padding(it))
    }
}

@Composable
@Preview
private fun ProfileScreenPreview() {
    RevibesTheme {
        ProfileScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = ProfileScreenUiState()
        )
    }
}
