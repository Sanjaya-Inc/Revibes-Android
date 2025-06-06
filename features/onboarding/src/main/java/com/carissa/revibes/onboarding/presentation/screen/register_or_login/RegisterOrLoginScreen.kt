/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.onboarding.presentation.screen.register_or_login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.onboarding.R
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Composable
fun RegisterOrLoginScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterOrLoginScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    RegisterOrLoginScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun RegisterOrLoginScreenContent(
    uiState: RegisterOrLoginScreenUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 48.dp, vertical = 64.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AsyncImage(
            R.drawable.main_char_both,
            contentDescription = null,
            modifier = Modifier.padding(16.dp).fillMaxWidth().height(500.dp)
        )
        Text(
            text = stringResource(R.string.title_onboarding_page_3),
            style = RevibesTheme.typography.h1,
            textAlign = TextAlign.Center,
            color = RevibesTheme.colors.primary,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Row(horizontalArrangement = Arrangement.spacedBy(48.dp)) {
            Button(text = stringResource(R.string.cta_register), modifier = Modifier.weight(1f))
            Button(text = stringResource(R.string.cta_login), modifier = Modifier.weight(1f))
        }
    }
}

@Composable
@Preview
private fun RegisterOrLoginScreenPreview() {
    RevibesTheme {
        RegisterOrLoginScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = RegisterOrLoginScreenUiState()
        )
    }
}
