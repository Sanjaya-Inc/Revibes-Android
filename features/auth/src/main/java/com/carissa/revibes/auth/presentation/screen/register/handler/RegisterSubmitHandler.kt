package com.carissa.revibes.auth.presentation.screen.register.handler

import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.auth.presentation.screen.register.RegisterScreenUiEvent
import com.carissa.revibes.auth.presentation.screen.register.RegisterScreenUiState
import com.carissa.revibes.core.data.utils.ApiException
import com.carissa.revibes.core.domain.utils.GeneralErrorMapper
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class RegisterSubmitHandler(
    private val authRepo: AuthRepository,
    private val generalErrorMapper: GeneralErrorMapper
) {
    suspend fun doRegister(
        syntax: Syntax<RegisterScreenUiState, RegisterScreenUiEvent>,
    ) {
        syntax.reduce {
            this.state.copy(isLoading = true)
        }
        runCatching {
            authRepo.signUpWithEmail(
                syntax.state.email.text.trim(),
                syntax.state.fullName.text,
                syntax.state.phone.text.trim(),
                syntax.state.password.text.trim()
            )
        }.onFailure {
            throw if (it is ApiException) {
                if (it.statusCode == 400 && it.errorResponse?.error?.contains("AUTH.EMAIL_USED") == true) {
                    Throwable("Account already registered", it)
                } else {
                    generalErrorMapper.mapError(it)
                }
            } else {
                it
            }
        }
        syntax.reduce {
            this.state.copy(isLoading = false)
        }
        syntax.postSideEffect(RegisterScreenUiEvent.RegisterSuccess)
    }
}
