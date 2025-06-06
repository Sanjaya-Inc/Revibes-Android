package com.carissa.revibes.auth.presentation.screen.register.handler

import com.carissa.revibes.auth.presentation.screen.register.RegisterScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.register.RegisterScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class RegisterExceptionHandler {
    suspend fun onRegisterError(
        syntax: Syntax<RegisterScreenUiState, RegisterScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(RegisterScreenUiEvent.RegisterError(throwable.message.orEmpty()))
    }
}
