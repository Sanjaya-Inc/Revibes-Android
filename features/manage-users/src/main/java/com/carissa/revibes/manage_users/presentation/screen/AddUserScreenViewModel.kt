package com.carissa.revibes.manage_users.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.util.EmailValidator
import com.carissa.revibes.core.presentation.util.FullNameValidator
import com.carissa.revibes.core.presentation.util.PasswordValidator
import com.carissa.revibes.core.presentation.util.PhoneValidator
import com.carissa.revibes.manage_users.data.ManageUsersRepository
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.handler.ManageUsersExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class AddUserScreenUiState(
    val isLoading: Boolean = false,
    val name: TextFieldValue = TextFieldValue(),
    val email: TextFieldValue = TextFieldValue(),
    val phone: TextFieldValue = TextFieldValue(),
    val password: TextFieldValue = TextFieldValue(),
    val selectedRole: UserDomain.UserRole = UserDomain.UserRole.USER,
    val isSuccess: Boolean = false
) {
    val nameError: String?
        get() = FullNameValidator.validate(name.text)

    val emailError: String?
        get() = EmailValidator.validate(email.text)

    val phoneError: String?
        get() = PhoneValidator.validate(phone.text)

    val passwordError: String?
        get() = PasswordValidator.validate(password.text)

    val isButtonEnabled: Boolean
        get() = nameError == null && emailError == null &&
            phoneError == null && passwordError == null
}

sealed interface AddUserScreenUiEvent : NavigationEvent {
    data object NavigateBack : AddUserScreenUiEvent
    data object CreateUser : AddUserScreenUiEvent
    data class NameChanged(val name: TextFieldValue) : AddUserScreenUiEvent
    data class EmailChanged(val email: TextFieldValue) : AddUserScreenUiEvent
    data class PhoneChanged(val phone: TextFieldValue) : AddUserScreenUiEvent
    data class PasswordChanged(val password: TextFieldValue) : AddUserScreenUiEvent
    data class RoleChanged(val role: UserDomain.UserRole) : AddUserScreenUiEvent
    data class OnCreateUserFailed(val message: String) : AddUserScreenUiEvent
}

@KoinViewModel
class AddUserScreenViewModel(
    private val repository: ManageUsersRepository,
    private val exceptionHandler: ManageUsersExceptionHandler
) : BaseViewModel<AddUserScreenUiState, AddUserScreenUiEvent>(
    initialState = AddUserScreenUiState(),
    exceptionHandler = { syntax, throwable ->
        exceptionHandler.onCreateUserError(syntax, throwable)
    }
) {

    override fun onEvent(event: AddUserScreenUiEvent) {
        super.onEvent(event)
        intent {
            super.onEvent(event)
            when (event) {
                is AddUserScreenUiEvent.NameChanged -> reduce {
                    this.state.copy(name = event.name)
                }

                is AddUserScreenUiEvent.EmailChanged -> reduce {
                    this.state.copy(email = event.email)
                }

                is AddUserScreenUiEvent.PhoneChanged -> reduce {
                    this.state.copy(phone = event.phone)
                }

                is AddUserScreenUiEvent.PasswordChanged -> reduce {
                    this.state.copy(password = event.password)
                }

                is AddUserScreenUiEvent.RoleChanged -> reduce {
                    this.state.copy(selectedRole = event.role)
                }

                is AddUserScreenUiEvent.CreateUser -> createUser()
                else -> postSideEffect(event)
            }
        }
    }

    private fun createUser() = intent {
        reduce { state.copy(isLoading = true) }

        repository.createUser(
            name = state.name.text.trim(),
            email = state.email.text.trim(),
            phone = state.phone.text.trim(),
            role = state.selectedRole.name.lowercase(),
            password = state.password.text,
        )

        reduce {
            state.copy(
                isLoading = false,
                isSuccess = true
            )
        }
    }
}
