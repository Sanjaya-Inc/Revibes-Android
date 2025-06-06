/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.auth.presentation.screen.register

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
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.carissa.revibes.auth.R
import com.carissa.revibes.auth.presentation.components.AuthActionButton
import com.carissa.revibes.auth.presentation.components.AuthNavigationRow
import com.carissa.revibes.auth.presentation.components.AuthScreenLayout
import com.carissa.revibes.auth.presentation.components.AuthTextField
import com.carissa.revibes.auth.presentation.components.EmailField
import com.carissa.revibes.auth.presentation.components.PasswordField
import com.carissa.revibes.auth.presentation.components.PhoneField
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

@Destination<AuthGraph>(start = true)
@Composable
fun RegisterScreen(
    modifier: Modifier = Modifier,
    viewModel: RegisterScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val navigator = RevibesTheme.navigator
    val context = LocalContext.current
    viewModel.collectSideEffect { event ->
        when (event) {
            is RegisterScreenUiEvent.NavigateBack -> navigator.navigateUp()
            is RegisterScreenUiEvent.NavigateToLogin -> navigator.navigate(LoginScreenDestination) {
                popUpTo(RegisterScreenDestination) {
                    inclusive = true
                }
            }

            is RegisterScreenUiEvent.RegisterError -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }
    RegisterScreenContent(uiState = state, modifier = modifier, eventReceiver = viewModel)
}

@Composable
private fun RegisterScreenContent(
    uiState: RegisterScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<RegisterScreenUiEvent> = EventReceiver { }
) {
    AuthScreenLayout(
        onBackClick = { eventReceiver.onEvent(RegisterScreenUiEvent.NavigateBack) },
        content = {
            AsyncImage(
                R.drawable.main_char_both,
                contentDescription = null,
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .height(256.dp)
            )
            Text(
                text = stringResource(R.string.label_welcome),
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

            AuthTextField(
                value = uiState.fullName,
                onValueChange = { eventReceiver.onEvent(RegisterScreenUiEvent.FullNameChanged(it)) },
                label = stringResource(R.string.label_enter_full_name),
                isEnabled = !uiState.isLoading,
                errorText = uiState.fullNameError
            )

            EmailField(
                value = uiState.email,
                onValueChange = { eventReceiver.onEvent(RegisterScreenUiEvent.EmailChanged(it)) },
                label = stringResource(R.string.label_enter_email),
                isEnabled = !uiState.isLoading,
                errorText = uiState.emailError
            )

            PhoneField(
                value = uiState.phone,
                onValueChange = { eventReceiver.onEvent(RegisterScreenUiEvent.PhoneChanged(it)) },
                label = stringResource(R.string.label_enter_phone),
                isEnabled = !uiState.isLoading,
                errorText = uiState.phoneError
            )

            PasswordField(
                value = uiState.password,
                onValueChange = { eventReceiver.onEvent(RegisterScreenUiEvent.PasswordChanged(it)) },
                label = stringResource(R.string.label_enter_pass),
                imeAction = ImeAction.Next,
                isEnabled = !uiState.isLoading,
                errorText = uiState.passwordError
            )

            PasswordField(
                value = uiState.confirmPassword,
                onValueChange = {
                    eventReceiver.onEvent(
                        RegisterScreenUiEvent.ConfirmPasswordChanged(
                            it
                        )
                    )
                },
                label = stringResource(R.string.label_enter_confirm_pass),
                isEnabled = !uiState.isLoading,
                errorText = uiState.confirmPasswordError,
                onImeAction = {
                    if (uiState.isButtonEnabled) eventReceiver.onEvent(RegisterScreenUiEvent.SubmitRegister)
                }
            )
        },
        bottomContent = {
            Column(modifier = Modifier.fillMaxWidth()) {
                AuthActionButton(
                    text = stringResource(R.string.cta_register),
                    onClick = { eventReceiver.onEvent(RegisterScreenUiEvent.SubmitRegister) },
                    isEnabled = uiState.isButtonEnabled,
                    isLoading = uiState.isLoading
                )

                AuthNavigationRow(
                    text = stringResource(R.string.label_already_have_an_account),
                    actionText = stringResource(R.string.cta_login),
                    onActionClick = { eventReceiver.onEvent(RegisterScreenUiEvent.NavigateToLogin) }
                )
            }
        },
        modifier = modifier,
        contentPadding = Modifier
            .padding(horizontal = 32.dp)
            .padding(top = 32.dp, bottom = 158.dp)
    )
}

@Composable
@Preview
private fun RegisterScreenPreview() {
    RevibesTheme {
        RegisterScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = RegisterScreenUiState()
        )
    }
}
