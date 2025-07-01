/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.home.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
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
    val banners: ImmutableList<HomeBannerData> = persistentListOf()
)

sealed interface HomeScreenUiEvent {
    data object NavigateToProfile : HomeScreenUiEvent, NavigationEvent
    data object NavigateToShop : HomeScreenUiEvent, NavigationEvent
    data object NavigateToExchangePoints : HomeScreenUiEvent, NavigationEvent
    data object NavigateToTransactionHistory : HomeScreenUiEvent, NavigationEvent
    data object NavigateToAboutUs : HomeScreenUiEvent, NavigationEvent
    data object NavigateToLogin : HomeScreenUiEvent, NavigationEvent
    data object LoadBanners : HomeScreenUiEvent
}

@KoinViewModel
class HomeScreenViewModel(
    private val navigationEventBus: NavigationEventBus,
    private val homeRepository: HomeRepository,
    private val homeExceptionHandler: HomeExceptionHandler,
) : BaseViewModel<HomeScreenUiState, HomeScreenUiEvent>(
    initialState = HomeScreenUiState(),
    onCreate = {
        onEvent(HomeScreenUiEvent.LoadBanners)
    },
    exceptionHandler = { exception ->
        homeExceptionHandler.onHomeError(this, exception)
    }
) {

    override fun onEvent(event: HomeScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is HomeScreenUiEvent.NavigateToProfile -> navigationEventBus.post(event)
                is HomeScreenUiEvent.NavigateToShop -> navigationEventBus.post(event)
                is HomeScreenUiEvent.NavigateToExchangePoints -> navigationEventBus.post(event)
                is HomeScreenUiEvent.NavigateToTransactionHistory -> navigationEventBus.post(event)
                is HomeScreenUiEvent.NavigateToAboutUs -> navigationEventBus.post(event)
                is HomeScreenUiEvent.LoadBanners -> loadBanners()
                is HomeScreenUiEvent.NavigateToLogin -> navigationEventBus.post(event)
            }
        }
    }

    private fun loadBanners() {
        intent {
            reduce { state.copy(isLoading = true) }
            val banners = homeRepository.getBanners()
            reduce {
                state.copy(
                    banners = banners.toImmutableList(),
                    isLoading = false
                )
            }
        }
    }
}
