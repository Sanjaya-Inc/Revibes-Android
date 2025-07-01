package com.carissa.revibes.home.presentation.screen.handler

import android.util.Log
import com.carissa.revibes.core.data.auth.local.AuthTokenDataSource
import com.carissa.revibes.core.data.main.remote.ApiException
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.home.presentation.screen.HomeScreenUiEvent
import com.carissa.revibes.home.presentation.screen.HomeScreenUiState
import io.ktor.http.HttpStatusCode
import org.koin.core.annotation.Factory
import org.orbitmvi.orbit.syntax.Syntax

@Factory
class HomeExceptionHandler(
    private val authTokenDataSource: AuthTokenDataSource,
    private val userDataSource: UserDataSource,
) {
    suspend fun onHomeError(
        syntax: Syntax<HomeScreenUiState, HomeScreenUiEvent>,
        throwable: Throwable
    ) = syntax.run {
        reduce { state.copy(isLoading = false) }
        if (throwable is ApiException && throwable.statusCode == HttpStatusCode.Unauthorized.value) {
            postSideEffect(HomeScreenUiEvent.NavigateToLogin)
            authTokenDataSource.clearAuthToken()
            userDataSource.clearUserData()
        }
        Log.e(TAG, "onHomeError: ${throwable.message}", throwable)
    }

    companion object {
        private const val TAG = "HomeExceptionHandler"
    }
}
