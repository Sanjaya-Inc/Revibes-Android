/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.drop_off.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.drop_off.presentation.navigation.DropOffGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<DropOffGraph>()
@Composable
fun DropOffConfirmationScreen(
    modifier: Modifier = Modifier,
    viewModel: DropOffConfirmationScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    DropOffConfirmationScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun DropOffConfirmationScreenContent(
    uiState: DropOffConfirmationScreenUiState,
    modifier: Modifier = Modifier
) {
    val navigator = RevibesTheme.navigator
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(64.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = navigator::navigateUp,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_cta),
                        modifier = Modifier.size(86.dp),
                        tint = Color.Unspecified,
                        contentDescription = "Back"
                    )
                }
                Text(
                    text = "Drop Off Booking Details",
                    style = RevibesTheme.typography.h2,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Light,
                    modifier = Modifier.weight(1f, fill = true),
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
        }
    }
}

@Composable
@Preview
private fun DropOffConfirmationScreenPreview() {
    RevibesTheme {
        DropOffConfirmationScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = DropOffConfirmationScreenUiState()
        )
    }
}
