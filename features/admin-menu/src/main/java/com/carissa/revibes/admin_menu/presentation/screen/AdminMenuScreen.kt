/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.admin_menu.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.admin_menu.presentation.navigation.AdminMenuGraph
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.TabButton
import com.carissa.revibes.core.presentation.components.components.CommonHeader
import com.carissa.revibes.core.presentation.components.components.Text
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<AdminMenuGraph>(start = true)
@Composable
fun AdminMenuScreen(
    modifier: Modifier = Modifier,
    viewModel: AdminMenuScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    AdminMenuScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun AdminMenuScreenContent(
    uiState: AdminMenuScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<AdminMenuScreenUiEvent> = EventReceiver {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            CommonHeader(
                title = "ADMIN MENU",
                backgroundDrawRes = R.drawable.bg_help_center,
                searchTextFieldValue = uiState.searchValue,
                onTextChange = { eventReceiver.onEvent(AdminMenuScreenUiEvent.OnSearchChange(it)) },
                onProfileClicked = {
                    eventReceiver.onEvent(AdminMenuScreenUiEvent.NavigateToProfile)
                },
            )
        }
    ) { contentPadding ->
        Column(
            modifier = Modifier.padding(contentPadding).padding(16.dp)
        ) {
            val pagerState = rememberPagerState { 3 }
            val scope = rememberCoroutineScope()
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                TabButton(
                    "Users",
                    pagerState.currentPage == 0,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(0)
                        }
                    }
                )
                TabButton(
                    "Sembako",
                    pagerState.currentPage == 1,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(1)
                        }
                    }
                )
                TabButton(
                    "Vouchers",
                    pagerState.currentPage == 2,
                    modifier = Modifier.weight(1f),
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(2)
                        }
                    }
                )
            }
            HorizontalPager(pagerState, modifier = Modifier.weight(1f)) {
                Box {
                    Text("Page: $it")
                }
            }
        }
    }
}

@Composable
@Preview
private fun AdminMenuScreenPreview() {
    RevibesTheme {
        AdminMenuScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = AdminMenuScreenUiState()
        )
    }
}
