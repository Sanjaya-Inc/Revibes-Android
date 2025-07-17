package com.carissa.revibes.manage_users.presentation.handler

import android.util.Log
import com.carissa.revibes.manage_users.presentation.screen.AddUserScreenUiEvent
import com.carissa.revibes.manage_users.presentation.screen.AddUserScreenUiState
import com.carissa.revibes.manage_users.presentation.screen.EditUserScreenUiEvent
import com.carissa.revibes.manage_users.presentation.screen.EditUserScreenUiState
import com.carissa.revibes.manage_users.presentation.screen.ManageUsersScreenUiEvent
import com.carissa.revibes.manage_users.presentation.screen.ManageUsersScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class ManageUsersExceptionHandler {
    suspend fun onManageUsersError(
        syntax: Syntax<ManageUsersScreenUiState, ManageUsersScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                isLoadingMore = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }

        Log.e(TAG, "onManageUsersError: ${throwable.message}", throwable)
    }

    suspend fun onLoadUserError(
        syntax: Syntax<EditUserScreenUiState, EditUserScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce {
            state.copy(
                isLoading = false,
                error = throwable.message ?: "Unknown error occurred"
            )
        }

        Log.e(TAG, "onLoadUserError: ${throwable.message}", throwable)
    }

    suspend fun onAddPointsError(
        syntax: Syntax<EditUserScreenUiState, EditUserScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(EditUserScreenUiEvent.OnAddPointsFailed(throwable.message.orEmpty()))
        Log.e(TAG, "onAddPointsError: ${throwable.message}", throwable)
    }

    suspend fun onCreateUserError(
        syntax: Syntax<AddUserScreenUiState, AddUserScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(AddUserScreenUiEvent.OnCreateUserFailed(throwable.message.orEmpty()))
        Log.e(TAG, "onCreateUserError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "ManageUsersExceptionHandler"
    }
}
