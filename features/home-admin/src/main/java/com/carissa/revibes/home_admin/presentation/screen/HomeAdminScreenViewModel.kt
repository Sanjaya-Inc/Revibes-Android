package com.carissa.revibes.home_admin.presentation.screen

import com.carissa.revibes.core.data.user.local.UserDataSourceGetter
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import org.koin.android.annotation.KoinViewModel

data class HomeAdminScreenUiState(
    val isLoading: Boolean = false,
    val userName: String = ""
)

sealed interface HomeAdminScreenUiEvent {
    data object NavigateToManageUsers : HomeAdminScreenUiEvent, NavigationEvent
    data object NavigateToManageVouchers : HomeAdminScreenUiEvent, NavigationEvent
    data object NavigateToManageTransactions : HomeAdminScreenUiEvent, NavigationEvent
    data object NavigateToClaimedVouchers : HomeAdminScreenUiEvent, NavigationEvent
    data object NavigateToProfile : HomeAdminScreenUiEvent, NavigationEvent
    data object LoadAdminData : HomeAdminScreenUiEvent
}

@KoinViewModel
class HomeAdminScreenViewModel(
    private val userDataSourceGetter: UserDataSourceGetter,
) : BaseViewModel<HomeAdminScreenUiState, HomeAdminScreenUiEvent>(
    initialState = HomeAdminScreenUiState(),
    onCreate = {
        onEvent(HomeAdminScreenUiEvent.LoadAdminData)
    }
) {

    override fun onEvent(event: HomeAdminScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is HomeAdminScreenUiEvent.LoadAdminData -> loadAdminData()
                else -> Unit
            }
        }
    }

    private fun loadAdminData() {
        intent {
            reduce { state.copy(isLoading = true) }
            val user = userDataSourceGetter.getUserValue().getOrThrow()
            reduce {
                state.copy(
                    userName = user.name,
                    isLoading = false
                )
            }
        }
    }
}
