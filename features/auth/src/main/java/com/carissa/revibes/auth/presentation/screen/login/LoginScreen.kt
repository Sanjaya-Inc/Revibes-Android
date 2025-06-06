/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.auth.presentation.screen.login

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.carissa.revibes.auth.R
import com.carissa.revibes.auth.presentation.components.AuthActionButton
import com.carissa.revibes.auth.presentation.components.AuthNavigationRow
import com.carissa.revibes.auth.presentation.components.AuthScreenLayout
import com.carissa.revibes.auth.presentation.components.EmailField
import com.carissa.revibes.auth.presentation.components.PasswordField
import com.carissa.revibes.auth.presentation.navigation.AuthGraph
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Text
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.auth.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.auth.destinations.RegisterScreenDestination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<AuthGraph>()
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val navigator = RevibesTheme.navigator
    val context = LocalContext.current
    viewModel.collectSideEffect { event ->
        when (event) {
            is LoginScreenUiEvent.NavigateBack -> navigator.navigateUp()
            is LoginScreenUiEvent.NavigateToRegister -> navigator.navigate(RegisterScreenDestination) {
                popUpTo(LoginScreenDestination) {
                    inclusive = true
                }
            }

            is LoginScreenUiEvent.LoginError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }
    LoginScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun LoginScreenContent(
    uiState: LoginScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<LoginScreenUiEvent> = EventReceiver {}
) {
    AuthScreenLayout(
        onBackClick = { eventReceiver.onEvent(LoginScreenUiEvent.NavigateBack) },
        content = {
            Text(
                text = stringResource(R.string.label_welcome_back),
                style = RevibesTheme.typography.h1,
                letterSpacing = 10.sp,
                textAlign = TextAlign.Center,
                color = RevibesTheme.colors.primary,
            )
            Text(
                text = stringResource(R.string.label_to),
                style = RevibesTheme.typography.h1,
                letterSpacing = 10.sp,
                textAlign = TextAlign.Center,
                color = RevibesTheme.colors.primary,
            )
            AsyncImage(
                R.drawable.main_logo,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 56.dp)
                    .padding(bottom = 32.dp)
            )

            EmailField(
                value = uiState.email,
                onValueChange = { eventReceiver.onEvent(LoginScreenUiEvent.EmailChanged(it)) },
                label = stringResource(R.string.label_enter_email),
                isEnabled = !uiState.isLoading,
                errorText = uiState.emailError
            )

            PasswordField(
                value = uiState.password,
                onValueChange = { eventReceiver.onEvent(LoginScreenUiEvent.PasswordChanged(it)) },
                label = stringResource(R.string.label_enter_pass),
                isEnabled = !uiState.isLoading,
                errorText = uiState.passwordError,
                onImeAction = {
                    if (uiState.isButtonEnabled) eventReceiver.onEvent(LoginScreenUiEvent.SubmitLogin)
                }
            )

            AsyncImage(
                R.drawable.main_char_both,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(256.dp)
            )
        },
        bottomContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthActionButton(
                    text = stringResource(R.string.cta_login),
                    onClick = { eventReceiver.onEvent(LoginScreenUiEvent.SubmitLogin) },
                    isEnabled = uiState.isButtonEnabled,
                    isLoading = uiState.isLoading
                )

                AuthNavigationRow(
                    text = stringResource(R.string.label_not_have_an_account),
                    actionText = stringResource(R.string.cta_register),
                    onActionClick = { eventReceiver.onEvent(LoginScreenUiEvent.NavigateToRegister) }
                )
            }
        },
        modifier = modifier
    )
}

@Composable
@Preview
private fun LoginScreenPreview() {
    RevibesTheme {
        LoginScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = LoginScreenUiState()
        )
    }
}
