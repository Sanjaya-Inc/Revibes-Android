package com.carissa.revibes.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.koin.android.annotation.KoinViewModel
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.viewmodel.container

@KoinViewModel
class MainActivityViewModel : ViewModel(), ContainerHost<LoginState, LoginEvent> {

    override val container = container<LoginState, LoginEvent>(LoginState())

    fun onEvent(event: LoginEvent) = intent {
        when (event) {
            is LoginEvent.UsernameChanged -> {
                reduce {
                    state.copy(username = event.username)
                }
            }
            is LoginEvent.PasswordChanged -> {
                reduce {
                    state.copy(password = event.password)
                }
            }
            LoginEvent.LoginClicked -> {
                reduce { state.copy(isLoading = true) }
                viewModelScope.launch {
                    try {
//                        loginUseCase(state.username, state.password)
                        reduce {
                            state.copy(
                                isLoading = false,
                                isLoggedIn = true,
                                error = null
                            )
                        }
                    } catch (e: Exception) {
                        reduce {
                            state.copy(
                                isLoading = false,
                                error = e.message ?: "Login failed"
                            )
                        }
                    }
                }
            }
        }
    }
}
