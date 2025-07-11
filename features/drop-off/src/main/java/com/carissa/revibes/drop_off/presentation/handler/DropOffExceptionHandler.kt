package com.carissa.revibes.drop_off.presentation.handler

import android.util.Log
import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.drop_off.presentation.screen.DropOffScreenUiEvent
import com.carissa.revibes.drop_off.presentation.screen.DropOffScreenUiState
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class DropOffExceptionHandler(
    private val authTokenDataSource: AuthTokenDataSource,
    private val userDataSource: UserDataSource,
) {
    suspend fun onDropOffError(
        syntax: Syntax<DropOffScreenUiState, DropOffScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        postSideEffect(DropOffScreenUiEvent.OnMakeOrderFailed(throwable.message.orEmpty()))
        Log.e(TAG, "onDropOffError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "DropOffExceptionHandler"
    }
}
