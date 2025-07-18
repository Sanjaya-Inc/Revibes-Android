/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.auth.presentation.screen.register

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.auth.presentation.screen.register.handler.RegisterExceptionHandler
import com.carissa.revibes.auth.presentation.screen.register.handler.RegisterSubmitHandler
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.util.EmailValidator
import com.carissa.revibes.core.presentation.util.FullNameValidator
import com.carissa.revibes.core.presentation.util.PasswordValidator
import com.carissa.revibes.core.presentation.util.PhoneValidator
import org.koin.android.annotation.KoinViewModel

data class RegisterScreenUiState(
    val isLoading: Boolean = false,
    val fullName: TextFieldValue = TextFieldValue(),
    val email: TextFieldValue = TextFieldValue(),
    val phone: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue(),
    val confirmPassword: TextFieldValue = TextFieldValue(),
) {
    val fullNameError: String?
        get() = FullNameValidator.validate(fullName.text)

    val emailError: String?
        get() = EmailValidator.validate(email.text)

    val phoneError: String?
        get() = PhoneValidator.validate(phone.text)

    val passwordError: String?
        get() = PasswordValidator.validate(password.text, confirmPassword.text)

    val confirmPasswordError: String?
        get() = PasswordValidator.validate(confirmPassword.text, password.text)

    val isButtonEnabled: Boolean
        get() = fullNameError == null && emailError == null &&
            phoneError == null && passwordError == null &&
            confirmPasswordError == null && !isLoading
}

sealed interface RegisterScreenUiEvent {
    data object NavigateBack : RegisterScreenUiEvent
    data object NavigateToLogin : RegisterScreenUiEvent
    data class FullNameChanged(val fullName: TextFieldValue) : RegisterScreenUiEvent
    data class EmailChanged(val email: TextFieldValue) : RegisterScreenUiEvent
    data class PhoneChanged(val phone: TextFieldValue) : RegisterScreenUiEvent
    data class PasswordChanged(val password: TextFieldValue) : RegisterScreenUiEvent
    data class ConfirmPasswordChanged(val confirmPassword: TextFieldValue) : RegisterScreenUiEvent
    data object SubmitRegister : RegisterScreenUiEvent
    data class RegisterError(val message: String) : RegisterScreenUiEvent
}

@KoinViewModel
class RegisterScreenViewModel(
    private val registerSubmitHandler: RegisterSubmitHandler,
    private val registerExceptionHandler: RegisterExceptionHandler
) :
    BaseViewModel<RegisterScreenUiState, RegisterScreenUiEvent>(
        RegisterScreenUiState(),
        exceptionHandler = { syntax, exc ->
            registerExceptionHandler.onRegisterError(syntax, exc)
        }
    ) {

    override fun onEvent(event: RegisterScreenUiEvent) {
        super.onEvent(event)
        intent {
            super.onEvent(event)
            when (event) {
                is RegisterScreenUiEvent.ConfirmPasswordChanged -> reduce {
                    this.state.copy(confirmPassword = event.confirmPassword)
                }

                is RegisterScreenUiEvent.EmailChanged -> reduce {
                    this.state.copy(email = event.email)
                }

                is RegisterScreenUiEvent.FullNameChanged -> reduce {
                    this.state.copy(fullName = event.fullName)
                }

                is RegisterScreenUiEvent.PasswordChanged -> reduce {
                    this.state.copy(password = event.password)
                }

                is RegisterScreenUiEvent.PhoneChanged -> reduce {
                    this.state.copy(phone = event.phone)
                }

                RegisterScreenUiEvent.SubmitRegister -> registerSubmitHandler.doRegister(this)

                else -> postSideEffect(event)
            }
        }
    }
}
