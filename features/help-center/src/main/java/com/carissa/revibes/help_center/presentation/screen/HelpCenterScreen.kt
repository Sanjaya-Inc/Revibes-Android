/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.help_center.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.ButtonVariant
import com.carissa.revibes.core.presentation.compose.components.CommonHeader
import com.carissa.revibes.core.presentation.compose.components.SearchConfig
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.core.presentation.util.openSupportWhatsApp
import com.carissa.revibes.help_center.R
import com.carissa.revibes.help_center.presentation.component.HelpItemRoot
import com.carissa.revibes.help_center.presentation.navigation.HelpCenterGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<HelpCenterGraph>(start = true)
@Composable
fun HelpCenterScreen(
    modifier: Modifier = Modifier,
    viewModel: HelpCenterScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    viewModel.collectSideEffect {}
    HelpCenterScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun HelpCenterScreenContent(
    uiState: HelpCenterScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<HelpCenterScreenUiEvent> = EventReceiver { }
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            CommonHeader(
                title = "HELP CENTER",
                backgroundDrawRes = R.drawable.bg_help_center,
                searchConfig = SearchConfig.Enabled(
                    value = uiState.searchValue,
                    onValueChange = { eventReceiver.onEvent(HelpCenterScreenUiEvent.OnSearchChange(it)) }
                ),
                onProfileClicked = {
                    eventReceiver.onEvent(HelpCenterScreenUiEvent.NavigateToProfile)
                },
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(contentPadding)
                .padding(horizontal = 16.dp)
                .padding(top = 16.dp)
        ) {
            uiState.filteredItems.forEachIndexed { index, data ->
                HelpItemRoot(
                    data,
                    onChildClicked = { child ->
                        eventReceiver.onEvent(HelpCenterScreenUiEvent.OnHelpChildClicked(child))
                    }
                )
            }
            val context = LocalContext.current
            Button(
                variant = ButtonVariant.SecondaryOutlined,
                onClick = {
                    context.openSupportWhatsApp()
                },
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally)
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_chat),
                        contentDescription = null,
                        tint = RevibesTheme.colors.secondary,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        "Chat Team Revibes",
                        style = RevibesTheme.typography.button,
                        color = RevibesTheme.colors.secondary
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun HelpCenterScreenPreview() {
    RevibesTheme {
        HelpCenterScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = HelpCenterScreenUiState()
        )
    }
}
