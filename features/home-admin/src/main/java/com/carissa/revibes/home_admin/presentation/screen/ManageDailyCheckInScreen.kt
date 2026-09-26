package com.carissa.revibes.home_admin.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.core.presentation.compose.components.textfield.OutlinedTextField
import com.carissa.revibes.home_admin.R
import com.carissa.revibes.home_admin.presentation.navigation.HomeAdminGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import com.carissa.revibes.core.R as CoreR

@Destination<HomeAdminGraph>
@Composable
fun ManageDailyCheckInScreen(
    modifier: Modifier = Modifier,
    viewModel: ManageDailyCheckInScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val navigator = RevibesTheme.navigator

    viewModel.collectSideEffect { event ->
        when (event) {
            is ManageDailyCheckInScreenUiEvent.NavigateBack -> navigator.navigateUp()
            else -> Unit
        }
    }

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            Toast.makeText(context, state.successMessage, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ManageDailyCheckInScreenUiEvent.ClearMessage)
        }
    }

    LaunchedEffect(state.errorMessage, state.hasLoaded) {
        val message = state.errorMessage ?: return@LaunchedEffect
        if (state.hasLoaded) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ManageDailyCheckInScreenUiEvent.ClearMessage)
        }
    }

    ManageDailyCheckInScreenContent(
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageDailyCheckInScreenContent(
    uiState: ManageDailyCheckInScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ManageDailyCheckInScreenUiEvent> = EventReceiver { }
) {
    val navigator = RevibesTheme.navigator
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manage_daily_check_in),
                        style = RevibesTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        color = RevibesTheme.colors.primary,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            painter = painterResource(CoreR.drawable.back_cta),
                            modifier = Modifier.size(86.dp),
                            tint = RevibesTheme.colors.primary,
                            contentDescription = stringResource(R.string.back_desc)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = RevibesTheme.colors.primary
                )
            )
        }
    ) { contentPadding ->
        ContentStateSwitcher(
            isLoading = uiState.isLoading,
            error = uiState.errorMessage.takeIf { !uiState.hasLoaded },
            actionButton = stringResource(R.string.retry) to {
                eventReceiver.onEvent(ManageDailyCheckInScreenUiEvent.LoadSettings)
            }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = stringResource(R.string.manage_daily_check_in_hint),
                        style = RevibesTheme.typography.body1,
                        color = RevibesTheme.colors.onSurface
                    )
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.daily_check_in_banner_text),
                            style = RevibesTheme.typography.body1,
                            color = RevibesTheme.colors.primary
                        )
                        OutlinedTextField(
                            value = uiState.bannerTextInput,
                            onValueChange = {
                                eventReceiver.onEvent(ManageDailyCheckInScreenUiEvent.OnBannerTextChange(it))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            text = stringResource(R.string.daily_check_in_point_amount),
                            style = RevibesTheme.typography.body1,
                            color = RevibesTheme.colors.primary
                        )
                        OutlinedTextField(
                            value = uiState.pointAmountInput,
                            onValueChange = {
                                eventReceiver.onEvent(ManageDailyCheckInScreenUiEvent.OnPointAmountChange(it))
                            },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                        )
                    }
                    Button(
                        text = stringResource(R.string.save_conversion),
                        onClick = {
                            eventReceiver.onEvent(ManageDailyCheckInScreenUiEvent.SaveSettings)
                        },
                        loading = uiState.isSubmitting,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ManageDailyCheckInScreenPreview() {
    RevibesTheme {
        ManageDailyCheckInScreenContent(
            uiState = ManageDailyCheckInScreenUiState(
                bannerTextInput = TextFieldValue("Check in 30 Days & Get Voucher Rp25k"),
                pointAmountInput = TextFieldValue("1")
            )
        )
    }
}
