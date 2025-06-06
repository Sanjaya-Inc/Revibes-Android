package com.carissa.revibes.auth.presentation.screen.login.handler

import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.login.LoginScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class LoginExceptionHandler {
    suspend fun onLoginError(
        syntax: Syntax<LoginScreenUiState, LoginScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(LoginScreenUiEvent.LoginError(throwable.message.orEmpty()))
    }
}
