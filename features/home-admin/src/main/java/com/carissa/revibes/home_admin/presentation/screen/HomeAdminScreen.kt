package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Surface
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.home_admin.R
import com.carissa.revibes.home_admin.presentation.component.AdminMenuCard
import com.carissa.revibes.home_admin.presentation.navigation.HomeAdminGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<HomeAdminGraph>(start = true)
@Composable
fun HomeAdminScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeAdminScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value

    HomeAdminScreenContent(
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@Composable
private fun HomeAdminScreenContent(
    uiState: HomeAdminScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<HomeAdminScreenUiEvent> = EventReceiver { }
) {
    Scaffold(
        modifier = modifier,
        containerColor = RevibesTheme.colors.background,
        topBar = {
            AdminHeader(
                userName = uiState.userName,
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp),
                onProfileClicked = {
                    eventReceiver.onEvent(HomeAdminScreenUiEvent.NavigateToProfile)
                },
            )
        }
    ) { paddingValues ->
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            item {
                WelcomeSection()
            }

            item {
                AdminMenuSection(eventReceiver = eventReceiver)
            }
        }
    }
}

@Composable
private fun AdminHeader(
    userName: String,
    modifier: Modifier = Modifier,
    onProfileClicked: () -> Unit = {},
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = stringResource(R.string.admin_dashboard),
            style = RevibesTheme.typography.h1,
            color = RevibesTheme.colors.primary
        )

        Surface(
            color = RevibesTheme.colors.primary,
            shape = RoundedCornerShape(50),
            onClick = onProfileClicked,
            modifier = Modifier.size(48.dp)
        ) {
            Text(
                text = userName
                    .split(" ")
                    .joinToString("") { it.take(1).uppercase() },
                style = RevibesTheme.typography.h2,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(12.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
private fun WelcomeSection() {
    Surface(
        color = Color.White,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.welcome_admin),
                style = RevibesTheme.typography.h2,
                color = RevibesTheme.colors.primary
            )
            Text(
                text = stringResource(R.string.admin_welcome_message),
                style = RevibesTheme.typography.body1,
                color = RevibesTheme.colors.onSurface
            )
        }
    }
}

@Composable
private fun AdminMenuSection(
    eventReceiver: EventReceiver<HomeAdminScreenUiEvent>
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.admin_features),
            style = RevibesTheme.typography.h2,
            color = RevibesTheme.colors.primary
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AdminMenuCard(
                title = stringResource(R.string.manage_users),
                description = stringResource(R.string.manage_users_desc),
                onClick = {
                    eventReceiver.onEvent(HomeAdminScreenUiEvent.NavigateToManageUsers)
                }
            )

            AdminMenuCard(
                title = stringResource(R.string.manage_vouchers),
                description = stringResource(R.string.manage_vouchers_desc),
                onClick = {
                    eventReceiver.onEvent(HomeAdminScreenUiEvent.NavigateToManageVouchers)
                }
            )

            AdminMenuCard(
                title = stringResource(R.string.manage_transactions),
                description = stringResource(R.string.manage_transactions_desc),
                onClick = {
                    eventReceiver.onEvent(HomeAdminScreenUiEvent.NavigateToManageTransactions)
                }
            )
        }
    }
}

@Composable
@Preview
private fun HomeAdminScreenPreview() {
    RevibesTheme {
        HomeAdminScreenContent(
            modifier = Modifier.background(RevibesTheme.colors.background),
            uiState = HomeAdminScreenUiState()
        )
    }
}
