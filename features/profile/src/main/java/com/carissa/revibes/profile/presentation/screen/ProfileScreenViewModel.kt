/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.profile.presentation.screen

import androidx.annotation.DrawableRes
import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.data.user.local.UserDataSource
import com.carissa.revibes.core.data.user.model.UserData
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.profile.R
import com.carissa.revibes.profile.presentation.screen.handler.LogoutHandler
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import org.koin.android.annotation.KoinViewModel

data class ProfileScreenUiState(
    val isLoading: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val userData: UserData? = null,
    val menu: PersistentList<Menu> = Menu.default()
) {
    data class Menu(
        val name: String,
        @DrawableRes val icon: Int,
        val event: ProfileScreenUiEvent
    ) {
        companion object {
            fun default() = persistentListOf(
                Menu(
                    "Your Profile",
                    R.drawable.ic_profile_alt,
                    ProfileScreenUiEvent.YourPofileClicked
                ),
                Menu(
                    "Support Center",
                    R.drawable.ic_support_center,
                    ProfileScreenUiEvent.SupportCenterClicked
                ),
                Menu(
                    "Settings",
                    R.drawable.ic_settings,
                    ProfileScreenUiEvent.SettingsClicked
                ),
                Menu(
                    "Privacy Policy",
                    R.drawable.ic_privacy,
                    ProfileScreenUiEvent.PrivacyPolicyClicked
                ),
                Menu(
                    "Terms of Service",
                    R.drawable.ic_term,
                    ProfileScreenUiEvent.TermsAndConditionsClicked
                )
            )
        }
    }
}

sealed interface ProfileScreenUiEvent {
    data object NavigateBack : ProfileScreenUiEvent
    data object NavigateToLogin : ProfileScreenUiEvent, NavigationEvent
    data object LogoutClicked : ProfileScreenUiEvent
    data object YourPofileClicked : ProfileScreenUiEvent
    data object SupportCenterClicked : ProfileScreenUiEvent
    data object SettingsClicked : ProfileScreenUiEvent
    data object PrivacyPolicyClicked : ProfileScreenUiEvent
    data object TermsAndConditionsClicked : ProfileScreenUiEvent
    data class SearchTextChanged(val text: TextFieldValue) : ProfileScreenUiEvent
}

@KoinViewModel
class ProfileScreenViewModel(
    userDataSource: UserDataSource,
    private val logoutHandler: LogoutHandler,
) : BaseViewModel<ProfileScreenUiState, ProfileScreenUiEvent>(
    initialState = ProfileScreenUiState(userData = userDataSource.getUserValue().getOrThrow()),
    exceptionHandler = { syntax, _ ->
        logoutHandler.onLogout(this, syntax)
    }
) {
    override fun onEvent(event: ProfileScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is ProfileScreenUiEvent.SearchTextChanged -> reduce {
                    state.copy(searchValue = event.text)
                }

                is ProfileScreenUiEvent.LogoutClicked -> logoutHandler.onLogout(
                    this@ProfileScreenViewModel,
                    this
                )

                else -> postSideEffect(event)
            }
        }
    }
}
