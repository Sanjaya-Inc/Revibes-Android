/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.onboarding.presentation.navigation.OnboardingGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<OnboardingGraph>(start = true)
@Composable
fun OnboardingScreen(
    modifier: Modifier = Modifier,
    viewModel: OnboardingScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    OnboardingScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun OnboardingScreenContent(
    uiState: OnboardingScreenUiState,
    modifier: Modifier = Modifier
) {
    TODO()
}

@Composable
@Preview
private fun OnboardingScreenPreview() {
    RevibesTheme {
        OnboardingScreenContent(OnboardingScreenUiState())
    }
}
