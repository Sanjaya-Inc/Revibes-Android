/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.pick_up.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.ComingSoon
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.pick_up.R
import com.carissa.revibes.pick_up.presentation.navigation.PickUpGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<PickUpGraph>(start = true)
@Composable
fun PickUpScreen(
    modifier: Modifier = Modifier,
    viewModel: PickUpScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    PickUpScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun PickUpScreenContent(
    uiState: PickUpScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<PickUpScreenUiEvent> = EventReceiver { }
) {
    val context = LocalContext.current
    var searchText by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            CommonHeader(
                title = stringResource(R.string.pick_up_title),
                backgroundDrawRes = R.drawable.bg_pick_up,
                searchTextFieldValue = searchText,
                onTextChange = { searchText = it },
                onProfileClicked = {
                    eventReceiver.onEvent(PickUpScreenUiEvent.NavigateToProfile)
                },
            )
        }
    ) { contentPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .background(RevibesTheme.colors.background),
            contentAlignment = Alignment.Center
        ) {
            ComingSoon(
                featureName = "Pick up",
                modifier = Modifier.padding(32.dp),
                onClick = {
                    eventReceiver.onEvent(PickUpScreenUiEvent.NavigateToHome)
                }
            )
        }
    }
}

@Composable
@Preview
private fun PickUpScreenPreview() {
    RevibesTheme {
        PickUpScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = PickUpScreenUiState()
        )
    }
}
