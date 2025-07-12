/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.home.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.home.data.HomeRepository
import com.carissa.revibes.home.data.model.HomeBannerData
import com.carissa.revibes.home.presentation.component.FooterItem
import com.carissa.revibes.home.presentation.screen.handler.HomeExceptionHandler
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.android.annotation.KoinViewModel

data class HomeScreenUiState(
    val isLoading: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val footerItems: ImmutableList<FooterItem> = FooterItem.default(),
    val banners: ImmutableList<HomeBannerData> = persistentListOf(),
    val userPoints: Int = 0,
    val userName: String = "",
    val isAdmin: Boolean = false
)

sealed interface HomeScreenUiEvent {
    data object NavigateToProfile : HomeScreenUiEvent, NavigationEvent
    data object NavigateToShop : HomeScreenUiEvent, NavigationEvent
    data object NavigateToExchangePoints : HomeScreenUiEvent, NavigationEvent
    data object NavigateToTransactionHistory : HomeScreenUiEvent, NavigationEvent
    data object NavigateToAboutUs : HomeScreenUiEvent, NavigationEvent
    data object NavigateToDropOff : HomeScreenUiEvent, NavigationEvent
    data object NavigateToLogin : HomeScreenUiEvent, NavigationEvent
    data object NavigateToHelpCenter : HomeScreenUiEvent, NavigationEvent
    data object NavigateToAdminMenu : HomeScreenUiEvent, NavigationEvent
    data object LoadHomeData : HomeScreenUiEvent
}

@KoinViewModel
class HomeScreenViewModel(
    private val homeRepository: HomeRepository,
    private val homeExceptionHandler: HomeExceptionHandler,
) : BaseViewModel<HomeScreenUiState, HomeScreenUiEvent>(
    initialState = HomeScreenUiState(),
    onCreate = {
        onEvent(HomeScreenUiEvent.LoadHomeData)
    },
    exceptionHandler = { syntax, exception ->
        homeExceptionHandler.onHomeError(syntax, exception)
    }
) {

    override fun onEvent(event: HomeScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is HomeScreenUiEvent.LoadHomeData -> loadHomeData()
                else -> Unit
            }
        }
    }

    private fun loadHomeData() {
        intent {
            reduce { state.copy(isLoading = true) }
            val homeData = homeRepository.getHomeData()
            reduce {
                state.copy(
                    banners = homeData.banners.toImmutableList(),
                    userPoints = homeData.userData.coins,
                    userName = homeData.userData.name,
                    isAdmin = homeData.userData.role == "admin",
                    isLoading = false
                )
            }
        }
    }
}
