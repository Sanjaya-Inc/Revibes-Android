/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.auth.presentation.screen.login

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.auth.presentation.screen.login.handler.LoginExceptionHandler
import com.carissa.revibes.auth.presentation.screen.login.handler.LoginSubmitHandler
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.util.EmailValidator
import com.carissa.revibes.core.presentation.util.PasswordValidator
import com.carissa.revibes.core.presentation.util.PhoneValidator
import org.koin.android.annotation.KoinViewModel

data class LoginScreenUiState(
    val isLoading: Boolean = false,
    val loginType: Type = Type.EMAIL,
    val userName: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue()
) {
    val emailError: String?
        get() = EmailValidator.validate(userName.text)

    val phoneError: String?
        get() = PhoneValidator.validate(userName.text)

    val passwordError: String?
        get() = PasswordValidator.validate(password.text)

    val isButtonEnabled: Boolean
        get() = (emailError == null || phoneError == null) && passwordError == null

    enum class Type {
        EMAIL, PHONE_NUMBER
    }
}

sealed interface LoginScreenUiEvent {
    data object NavigateBack : NavigationEvent, LoginScreenUiEvent
    data object NavigateToRegister : NavigationEvent, LoginScreenUiEvent
    data object NavigateToHome : LoginScreenUiEvent, NavigationEvent
    data object NavigateToAdminHome : LoginScreenUiEvent, NavigationEvent
    data class EmailChanged(val email: TextFieldValue) : LoginScreenUiEvent
    data class PasswordChanged(val password: TextFieldValue) : LoginScreenUiEvent
    data object SubmitLogin : LoginScreenUiEvent
    data class LoginError(val message: String) : LoginScreenUiEvent
    data class ChangeLoginType(val type: LoginScreenUiState.Type) : LoginScreenUiEvent
}

@KoinViewModel
class LoginScreenViewModel(
    private val loginSubmitHandler: LoginSubmitHandler,
    private val loginExceptionHandler: LoginExceptionHandler
) : BaseViewModel<LoginScreenUiState, LoginScreenUiEvent>(
    initialState = LoginScreenUiState(),
    exceptionHandler = { syntax, exception ->
        loginExceptionHandler.onLoginError(syntax, exception)
    }
) {

    override fun onEvent(event: LoginScreenUiEvent) {
        super.onEvent(event)
        intent {
            super.onEvent(event)
            when (event) {
                is LoginScreenUiEvent.EmailChanged -> reduce {
                    this.state.copy(userName = event.email)
                }

                is LoginScreenUiEvent.PasswordChanged -> reduce {
                    this.state.copy(password = event.password)
                }

                LoginScreenUiEvent.SubmitLogin -> loginSubmitHandler.doLogin(
                    this.state.loginType,
                    this@LoginScreenViewModel,
                    this
                )

                is LoginScreenUiEvent.ChangeLoginType -> reduce {
                    this.state.copy(loginType = event.type, userName = TextFieldValue())
                }

                else -> postSideEffect(event)
            }
        }
    }
}
