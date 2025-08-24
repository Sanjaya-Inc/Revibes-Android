package com.carissa.revibes.auth.presentation.screen.register.handler

import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.auth.presentation.screen.register.RegisterScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.register.RegisterScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class RegisterSubmitHandler(
    private val authRepo: AuthRepository
) {
    suspend fun doRegister(
        syntax: Syntax<RegisterScreenUiState, RegisterScreenUiEvent>,
    ) {
        syntax.reduce {
            this.state.copy(isLoading = true)
        }
        authRepo.signUpWithEmail(
            syntax.state.email.text.trim(),
            syntax.state.fullName.text,
            syntax.state.phone.text.trim(),
            syntax.state.password.text.trim()
        )
        syntax.reduce {
            this.state.copy(isLoading = false)
        }
        syntax.postSideEffect(RegisterScreenUiEvent.RegisterSuccess)
    }
}
