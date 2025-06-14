/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.presentation.screen.onboarding.component.page

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.onboarding.R
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun OnboardingPage(
    page: Int,
    modifier: Modifier = Modifier,
    viewModel: OnboardingPageViewModel = koinViewModel(key = "onboarding_page_$page") {
        parametersOf(page)
    }
) {
    val state = viewModel.collectAsState().value
    OnboardingPageContent(state = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun OnboardingPageContent(
    state: OnboardingPageUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<OnboardingPageUiEvent> = EventReceiver { }
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 48.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            state.image,
            contentDescription = null,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .height(350.dp)
        )
        Text(
            text = stringResource(state.text),
            style = RevibesTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = RevibesTheme.colors.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        AnimatedVisibility(state.isShowLoginRegister) {
            Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
                Button(
                    text = stringResource(R.string.cta_register),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        eventReceiver.onEvent(OnboardingPageUiEvent.NavigateToRegister)
                    }
                )
                Button(
                    text = stringResource(R.string.cta_login),
                    modifier = Modifier.weight(1f),
                    onClick = {
                        eventReceiver.onEvent(OnboardingPageUiEvent.NavigateToLogin)
                    }
                )
            }
        }
    }
}

@Composable
@Preview
private fun OnboardingPagePreview() {
    RevibesTheme {
        OnboardingPageContent(
            modifier = Modifier.background(Color.White),
            state = OnboardingPageUiState()
        )
    }
}
