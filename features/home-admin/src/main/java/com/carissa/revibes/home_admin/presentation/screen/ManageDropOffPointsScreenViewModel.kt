package com.carissa.revibes.home_admin.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.util.DeeplinkHandler
import com.carissa.revibes.home_admin.data.StoreRepository
import com.carissa.revibes.home_admin.data.model.StoreData
import com.carissa.revibes.home_admin.domain.GoogleMapsPoint
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import org.koin.core.annotation.KoinViewModel

data class ManageDropOffPointsScreenUiState(
    val isLoading: Boolean = false,
    val stores: ImmutableList<StoreData> = persistentListOf(),
    val error: String? = null
)

sealed interface ManageDropOffPointsScreenUiEvent {
    data object NavigateBack : ManageDropOffPointsScreenUiEvent, NavigationEvent
    data object NavigateToAddDropOffPoint : ManageDropOffPointsScreenUiEvent, NavigationEvent
    data class NavigateToEditDropOffPoint(
        val store: StoreData
    ) : ManageDropOffPointsScreenUiEvent, NavigationEvent
    data object LoadStores : ManageDropOffPointsScreenUiEvent
    data class OpenInGoogleMaps(val latitude: Double, val longitude: Double) : ManageDropOffPointsScreenUiEvent
}

@KoinViewModel
class ManageDropOffPointsScreenViewModel(
    private val storeRepository: StoreRepository,
    private val deeplinkHandler: DeeplinkHandler
) : BaseViewModel<ManageDropOffPointsScreenUiState, ManageDropOffPointsScreenUiEvent>(
    initialState = ManageDropOffPointsScreenUiState(isLoading = true),
    onCreate = {
        onEvent(ManageDropOffPointsScreenUiEvent.LoadStores)
    }
) {
    override fun onEvent(event: ManageDropOffPointsScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ManageDropOffPointsScreenUiEvent.LoadStores -> loadStores()
            is ManageDropOffPointsScreenUiEvent.OpenInGoogleMaps -> {
                deeplinkHandler.openUrl(GoogleMapsPoint.searchUrl(event.latitude, event.longitude))
            }
            else -> Unit
        }
    }

    private fun loadStores() {
        intent {
            reduce { state.copy(isLoading = true, error = null) }
            runCatching {
                storeRepository.getStores()
            }.onSuccess { stores ->
                reduce {
                    state.copy(
                        isLoading = false,
                        stores = stores.toImmutableList(),
                        error = null
                    )
                }
            }.onFailure { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            }
        }
    }
}
