package com.carissa.revibes.manage_users.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_users.data.ManageUsersRepository
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.handler.ManageUsersExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class EditUserScreenUiState(
    val isLoading: Boolean = false,
    val isLoadingAddPoints: Boolean = false,
    val user: UserDomain? = null,
    val pointsToAdd: TextFieldValue = TextFieldValue(),
    val pointsError: String? = null,
    val error: String? = null,
    val showAddPointsDialog: Boolean = false,
    val isSuccess: Boolean = false
)

sealed interface EditUserScreenUiEvent : NavigationEvent {
    data object NavigateBack : EditUserScreenUiEvent
    data class SetUserId(val userId: String) : EditUserScreenUiEvent
    data object LoadUserDetail : EditUserScreenUiEvent
    data object ShowAddPointsDialog : EditUserScreenUiEvent
    data object HideAddPointsDialog : EditUserScreenUiEvent
    data object AddPoints : EditUserScreenUiEvent
    data class PointsToAddChanged(val points: TextFieldValue) : EditUserScreenUiEvent
    data class OnLoadUserFailed(val message: String) : EditUserScreenUiEvent
    data class OnAddPointsFailed(val message: String) : EditUserScreenUiEvent
}

@KoinViewModel
class EditUserScreenViewModel(
    private val repository: ManageUsersRepository,
    private val exceptionHandler: ManageUsersExceptionHandler
) : BaseViewModel<EditUserScreenUiState, EditUserScreenUiEvent>(
    initialState = EditUserScreenUiState(),
    exceptionHandler = { syntax, throwable ->
        if (throwable.message?.contains("user", ignoreCase = true) == true) {
            exceptionHandler.onLoadUserError(syntax, throwable)
        } else {
            exceptionHandler.onAddPointsError(syntax, throwable)
        }
    }
) {

    private var userId: String = ""

    override fun onEvent(event: EditUserScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is EditUserScreenUiEvent.SetUserId -> setUserId(event.userId)
            EditUserScreenUiEvent.LoadUserDetail -> loadUserDetail()
            EditUserScreenUiEvent.ShowAddPointsDialog -> showAddPointsDialog()
            EditUserScreenUiEvent.HideAddPointsDialog -> hideAddPointsDialog()
            EditUserScreenUiEvent.AddPoints -> addPoints()
            is EditUserScreenUiEvent.PointsToAddChanged -> onPointsToAddChanged(event.points)
            else -> Unit
        }
    }

    private fun setUserId(id: String) {
        userId = id
        onEvent(EditUserScreenUiEvent.LoadUserDetail)
    }

    private fun loadUserDetail() = intent {
        if (userId.isEmpty()) return@intent

        reduce { state.copy(isLoading = true, error = null) }

        val user = repository.getUserDetail(userId)

        reduce {
            state.copy(
                isLoading = false,
                user = user,
                error = null
            )
        }
    }

    private fun showAddPointsDialog() = intent {
        reduce {
            state.copy(
                showAddPointsDialog = true,
                pointsToAdd = TextFieldValue(),
                pointsError = null
            )
        }
    }

    private fun hideAddPointsDialog() = intent {
        reduce {
            state.copy(
                showAddPointsDialog = false,
                pointsToAdd = TextFieldValue(),
                pointsError = null
            )
        }
    }

    private fun onPointsToAddChanged(points: TextFieldValue) = intent {
        reduce {
            state.copy(
                pointsToAdd = points,
                pointsError = when {
                    points.text.isBlank() -> "Points amount is required"
                    points.text.toIntOrNull() == null -> "Please enter a valid number"
                    points.text.toIntOrNull()!! <= 0 -> "Points must be greater than 0"
                    else -> null
                }
            )
        }
    }

    private fun addPoints() = intent {
        val pointsAmount = state.pointsToAdd.text.toIntOrNull()
        if (pointsAmount == null || pointsAmount <= 0) {
            reduce {
                state.copy(
                    pointsError = when {
                        state.pointsToAdd.text.isBlank() -> "Points amount is required"
                        pointsAmount == null -> "Please enter a valid number"
                        pointsAmount <= 0 -> "Points must be greater than 0"
                        else -> null
                    }
                )
            }
            return@intent
        }

        reduce { state.copy(isLoadingAddPoints = true) }

        val updatedUser = repository.addPointsToUser(
            id = userId,
            points = pointsAmount,
        )
        println("ketai: User after adding points: $updatedUser")

        reduce {
            state.copy(
                isLoadingAddPoints = false,
                user = updatedUser,
                showAddPointsDialog = false,
                pointsToAdd = TextFieldValue(),
                pointsError = null,
                isSuccess = true
            )
        }
    }
}
