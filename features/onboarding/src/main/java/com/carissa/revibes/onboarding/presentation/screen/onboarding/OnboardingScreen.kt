/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.presentation.screen.onboarding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.onboarding.presentation.navigation.OnboardingGraph
import com.carissa.revibes.core.presentation.components.PagerIndicator
import com.carissa.revibes.onboarding.presentation.screen.onboarding.component.page.OnboardingPage
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
    OnboardingScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun OnboardingScreenContent(
    uiState: OnboardingScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<OnboardingScreenUiEvent> = EventReceiver { }
) {
    Scaffold(modifier = modifier, containerColor = Color.Transparent) { paddingValues ->
        val pagerState = rememberPagerState(initialPage = uiState.initialPage, pageCount = {
            uiState.pageCount
        })
        LaunchedEffect(pagerState.currentPage) {
            if (pagerState.currentPage == uiState.pageCount - 1) {
                eventReceiver.onEvent(
                    OnboardingScreenUiEvent.OnboardingFinished
                )
            }
        }
        Box(Modifier.fillMaxSize()) {
            HorizontalPager(
                pagerState,
                modifier = Modifier.padding(paddingValues),
                beyondViewportPageCount = 3
            ) { page ->
                OnboardingPage(page = page)
            }
            PagerIndicator(
                currentPage = pagerState.currentPage,
                totalPage = uiState.pageCount,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = paddingValues.calculateBottomPadding() + 44.dp)
            )
        }
    }
}
