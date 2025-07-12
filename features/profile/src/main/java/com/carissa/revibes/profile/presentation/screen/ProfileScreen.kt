/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.profile.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import com.carissa.revibes.core.presentation.util.openSupportWhatsApp
import com.carissa.revibes.profile.presentation.component.ProfileHeader
import com.carissa.revibes.profile.presentation.component.ProfileMain
import com.carissa.revibes.profile.presentation.component.ProfileOptionMenu
import com.carissa.revibes.profile.presentation.navigation.ProfileGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<ProfileGraph>(start = true)
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: ProfileScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val navigator = RevibesTheme.navigator
    val context = LocalContext.current
    viewModel.collectSideEffect {
        when (it) {
            ProfileScreenUiEvent.NavigateBack -> navigator.popBackStack()
            ProfileScreenUiEvent.SupportCenterClicked -> context.openSupportWhatsApp()
            else -> Unit
        }
    }
    ProfileScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun ProfileScreenContent(
    uiState: ProfileScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ProfileScreenUiEvent> = EventReceiver {}
) {
    Scaffold(modifier, containerColor = Color.Transparent, topBar = {
        ProfileHeader(
            uiState.searchValue,
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp),
            onBackClicked = {
                eventReceiver.onEvent(ProfileScreenUiEvent.NavigateBack)
            },
            onTextChange = {
                eventReceiver.onEvent(ProfileScreenUiEvent.SearchTextChanged(it))
            }
        )
    }) {
        LazyColumn(
            modifier = Modifier
                .padding(it)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            uiState.userData?.let { userData ->
                item { ProfileMain(userData) }
            }

            items(uiState.menu) { menu ->
                ProfileOptionMenu(menu.icon, menu.name, onClick = {
                    eventReceiver.onEvent(menu.event)
                })
            }

            item {
                Button(
                    text = "LOGOUT",
                    variant = ButtonVariant.Secondary,
                    onClick = {
                        eventReceiver.onEvent(ProfileScreenUiEvent.LogoutClicked)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp)
                )
            }
        }
    }
}

@Composable
@Preview
private fun ProfileScreenPreview() {
    RevibesTheme {
        ProfileScreenContent(
            modifier = Modifier.background(Color.Gray),
            uiState = ProfileScreenUiState(
                userData = UserData.dummy(),
                menu = ProfileScreenUiState.Menu.default()
            )
        )
    }
}
