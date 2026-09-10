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
import com.carissa.revibes.core.R as CoreR
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

@Destination<HomeAdminGraph>
@Composable
fun ManageDropOffConversionScreen(
    modifier: Modifier = Modifier,
    viewModel: ManageDropOffConversionScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val navigator = RevibesTheme.navigator

    viewModel.collectSideEffect { event ->
        when (event) {
            is ManageDropOffConversionScreenUiEvent.NavigateBack -> navigator.navigateUp()
            else -> Unit
        }
    }

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            Toast.makeText(context, state.successMessage, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ManageDropOffConversionScreenUiEvent.ClearMessage)
        }
    }

    LaunchedEffect(state.errorMessage, state.hasLoaded) {
        val message = state.errorMessage ?: return@LaunchedEffect
        if (state.hasLoaded) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ManageDropOffConversionScreenUiEvent.ClearMessage)
        }
    }

    ManageDropOffConversionScreenContent(
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageDropOffConversionScreenContent(
    uiState: ManageDropOffConversionScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ManageDropOffConversionScreenUiEvent> = EventReceiver { }
) {
    val navigator = RevibesTheme.navigator
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manage_drop_off_conversion),
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
                eventReceiver.onEvent(ManageDropOffConversionScreenUiEvent.LoadSettings)
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
                        text = stringResource(R.string.manage_drop_off_conversion_hint),
                        style = RevibesTheme.typography.body1,
                        color = RevibesTheme.colors.onSurface
                    )
                    PointField(
                        label = stringResource(R.string.organic_points),
                        value = uiState.organicInput,
                        onValueChange = {
                            eventReceiver.onEvent(ManageDropOffConversionScreenUiEvent.OnOrganicChange(it))
                        }
                    )
                    PointField(
                        label = stringResource(R.string.non_organic_points),
                        value = uiState.nonOrganicInput,
                        onValueChange = {
                            eventReceiver.onEvent(ManageDropOffConversionScreenUiEvent.OnNonOrganicChange(it))
                        }
                    )
                    PointField(
                        label = stringResource(R.string.b3_points),
                        value = uiState.b3Input,
                        onValueChange = {
                            eventReceiver.onEvent(ManageDropOffConversionScreenUiEvent.OnB3Change(it))
                        }
                    )
                    Button(
                        text = stringResource(R.string.save_conversion),
                        onClick = {
                            eventReceiver.onEvent(ManageDropOffConversionScreenUiEvent.SaveSettings)
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
private fun PointField(
    label: String,
    value: TextFieldValue,
    onValueChange: (TextFieldValue) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            style = RevibesTheme.typography.body1,
            color = RevibesTheme.colors.primary
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}

@Composable
@Preview
private fun ManageDropOffConversionScreenPreview() {
    RevibesTheme {
        ManageDropOffConversionScreenContent(
            uiState = ManageDropOffConversionScreenUiState(
                organicInput = TextFieldValue("5"),
                nonOrganicInput = TextFieldValue("5"),
                b3Input = TextFieldValue("5")
            )
        )
    }
}
