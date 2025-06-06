/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */

package com.carissa.revibes.auth.presentation.screen.login

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.carissa.revibes.auth.R
import com.carissa.revibes.auth.presentation.navigation.AuthGraph
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.core.presentation.components.components.textfield.TextField
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.auth.destinations.LoginScreenDestination
import com.ramcosta.composedestinations.generated.auth.destinations.RegisterScreenDestination
import kotlinx.coroutines.launch
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
    Scaffold(modifier = modifier, containerColor = Color.Transparent, topBar = {
        IconButton(
            onClick = {
                eventReceiver.onEvent(LoginScreenUiEvent.NavigateBack)
            },
            modifier = Modifier
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.back_cta),
                modifier = Modifier.size(86.dp),
                tint = Color.Unspecified,
                contentDescription = null
            )
        }
    }) { paddingValues ->
        val scrollState = rememberScrollState()
        val coroutineScope = rememberCoroutineScope()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .padding(horizontal = 32.dp)
                    .padding(top = 32.dp, bottom = 100.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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

                TextField(
                    value = uiState.email,
                    onValueChange = { eventReceiver.onEvent(LoginScreenUiEvent.EmailChanged(it)) },
                    label = { Text(stringResource(R.string.label_enter_email)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next,
                        showKeyboardOnFocus = true
                    ),
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    isError = !uiState.emailError.isNullOrBlank(),
                    supportingText = {
                        if (uiState.emailError != null) {
                            Text(uiState.emailError.orEmpty())
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                TextField(
                    value = uiState.password,
                    onValueChange = { eventReceiver.onEvent(LoginScreenUiEvent.PasswordChanged(it)) },
                    label = { Text(stringResource(R.string.label_enter_pass)) },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done,
                        showKeyboardOnFocus = true
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        if (uiState.isButtonEnabled) eventReceiver.onEvent(LoginScreenUiEvent.SubmitLogin)
                    }),
                    visualTransformation = PasswordVisualTransformation(),
                    singleLine = true,
                    enabled = !uiState.isLoading,
                    isError = !uiState.passwordError.isNullOrBlank(),
                    supportingText = {
                        if (uiState.passwordError != null) {
                            Text(uiState.passwordError.orEmpty())
                        }
                    },
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                AsyncImage(
                    R.drawable.main_char_both,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .height(256.dp)
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(32.dp)
                    .fillMaxWidth()
            ) {
                Button(
                    text = stringResource(R.string.cta_login),
                    enabled = uiState.isButtonEnabled,
                    modifier = Modifier.fillMaxWidth(),
                    loading = uiState.isLoading,
                    onClick = {
                        eventReceiver.onEvent(LoginScreenUiEvent.SubmitLogin)
                    }
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(stringResource(R.string.label_not_have_an_account))
                    TextButton(onClick = {
                        eventReceiver.onEvent(LoginScreenUiEvent.NavigateToRegister)
                    }) {
                        Text(
                            stringResource(R.string.cta_register),
                            color = RevibesTheme.colors.secondary
                        )
                    }
                }
            }

            val showScrollButton by remember {
                derivedStateOf {
                    scrollState.value < scrollState.maxValue
                }
            }
            val infiniteTransition = rememberInfiniteTransition()
            val offsetY by infiniteTransition.animateFloat(
                initialValue = 0f,
                targetValue = -10f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1000, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Reverse
                )
            )

            AnimatedVisibility(
                visible = showScrollButton,
                enter = fadeIn() + slideInVertically { it / 2 },
                exit = fadeOut() + slideOutVertically { it / 2 },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(bottom = 150.dp, end = 24.dp)
            ) {
                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            scrollState.animateScrollTo(scrollState.maxValue)
                        }
                    },
                    modifier = Modifier
                        .offset(y = offsetY.dp)
                        .size(56.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_cta),
                        modifier = Modifier
                            .size(32.dp)
                            .rotate(270f),
                        tint = RevibesTheme.colors.primary,
                        contentDescription = "Scroll to Bottom"
                    )
                }
            }
        }
    }
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
