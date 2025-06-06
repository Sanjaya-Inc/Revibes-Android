/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.auth.presentation.screen.login

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.util.EmailValidator
import com.carissa.revibes.core.presentation.util.PasswordValidator
import org.koin.android.annotation.KoinViewModel

data class LoginScreenUiState(
    val isLoading: Boolean = false,
    val email: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue()
) {
    val emailError: String?
        get() = EmailValidator.validate(email.text)

    val passwordError: String?
        get() = PasswordValidator.validate(password.text)

    val isButtonEnabled: Boolean
        get() = emailError == null && passwordError == null
}

sealed interface LoginScreenUiEvent {
    data object NavigateBack : LoginScreenUiEvent
    data object NavigateToRegister : LoginScreenUiEvent
    data class EmailChanged(val email: TextFieldValue) : LoginScreenUiEvent
    data class PasswordChanged(val password: TextFieldValue) : LoginScreenUiEvent
}

@KoinViewModel
class LoginScreenViewModel :
    BaseViewModel<LoginScreenUiState, LoginScreenUiEvent>(LoginScreenUiState()) {

    override fun onEvent(event: LoginScreenUiEvent) {
        intent {
            super.onEvent(event)
            when (event) {
                is LoginScreenUiEvent.EmailChanged -> reduce {
                    this.state.copy(email = event.email)
                }

                is LoginScreenUiEvent.PasswordChanged -> reduce {
                    this.state.copy(password = event.password)
                }

                else -> postSideEffect(event)
            }
        }
    }
}
