package com.carissa.revibes.home_admin.presentation.screen

import android.content.Context
import android.location.Geocoder
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.viewModelScope
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.util.AppDispatchers
import com.carissa.revibes.core.presentation.util.DeeplinkHandler
import com.carissa.revibes.home_admin.data.GoogleMapsLinkResolver
import com.carissa.revibes.home_admin.data.StoreRepository
import com.carissa.revibes.home_admin.data.model.CreateStoreRequest
import com.carissa.revibes.home_admin.data.model.StoreData
import com.carissa.revibes.home_admin.data.model.StorePositionRequest
import com.carissa.revibes.home_admin.data.model.UpdateStoreRequest
import com.carissa.revibes.home_admin.domain.GoogleMapsPoint
import com.carissa.revibes.home_admin.domain.PendingGoogleMapsShare
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel
import java.util.Locale

data class AddDropOffPointScreenUiState(
    val isLoading: Boolean = false,
    val isResolvingAddress: Boolean = false,
    val storeId: String? = null,
    val storeStatus: String = "active",
    val name: TextFieldValue = TextFieldValue(),
    val country: TextFieldValue = TextFieldValue("ID"),
    val address: TextFieldValue = TextFieldValue(),
    val postalCode: TextFieldValue = TextFieldValue(),
    val latitude: TextFieldValue = TextFieldValue(),
    val longitude: TextFieldValue = TextFieldValue(),
    val mapsInput: TextFieldValue = TextFieldValue(),
    val errorMessage: String? = null,
    val successMessage: String? = null
) {
    val isEditing: Boolean get() = !storeId.isNullOrBlank()

    val parsedPoint: Pair<Double, Double>?
        get() {
            val latitudeValue = latitude.text.toDoubleOrNull()
            val longitudeValue = longitude.text.toDoubleOrNull()
            return if (
                latitudeValue != null &&
                longitudeValue != null &&
                GoogleMapsPoint.isValid(latitudeValue, longitudeValue)
            ) {
                latitudeValue to longitudeValue
            } else {
                GoogleMapsPoint.parse(mapsInput.text)
            }
        }
}

sealed interface AddDropOffPointScreenUiEvent {
    data object NavigateBack : AddDropOffPointScreenUiEvent, NavigationEvent
    data class PrefillStore(val store: StoreData) : AddDropOffPointScreenUiEvent
    data class NameChanged(val name: TextFieldValue) : AddDropOffPointScreenUiEvent
    data class CountryChanged(val country: TextFieldValue) : AddDropOffPointScreenUiEvent
    data class AddressChanged(val address: TextFieldValue) : AddDropOffPointScreenUiEvent
    data class PostalCodeChanged(val postalCode: TextFieldValue) : AddDropOffPointScreenUiEvent
    data class LatitudeChanged(val latitude: TextFieldValue) : AddDropOffPointScreenUiEvent
    data class LongitudeChanged(val longitude: TextFieldValue) : AddDropOffPointScreenUiEvent
    data class MapsInputChanged(val mapsInput: TextFieldValue) : AddDropOffPointScreenUiEvent
    data class ResolveAddress(val context: Context) : AddDropOffPointScreenUiEvent
    data object OpenInGoogleMaps : AddDropOffPointScreenUiEvent
    data object Submit : AddDropOffPointScreenUiEvent
    data object ClearMessage : AddDropOffPointScreenUiEvent
}

@KoinViewModel
class AddDropOffPointScreenViewModel(
    private val storeRepository: StoreRepository,
    private val deeplinkHandler: DeeplinkHandler,
    private val appDispatchers: AppDispatchers,
    private val mapsLinkResolver: GoogleMapsLinkResolver,
    pendingGoogleMapsShare: PendingGoogleMapsShare
) : BaseViewModel<AddDropOffPointScreenUiState, AddDropOffPointScreenUiEvent>(
    initialState = AddDropOffPointScreenUiState()
) {
    init {
        viewModelScope.launch {
            pendingGoogleMapsShare.incoming.collect { sharedText ->
                onEvent(AddDropOffPointScreenUiEvent.MapsInputChanged(TextFieldValue(sharedText)))
            }
        }
    }

    override fun onEvent(event: AddDropOffPointScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            is AddDropOffPointScreenUiEvent.NameChanged -> intent {
                reduce { state.copy(name = event.name, errorMessage = null) }
            }
            is AddDropOffPointScreenUiEvent.CountryChanged -> intent {
                reduce { state.copy(country = event.country, errorMessage = null) }
            }
            is AddDropOffPointScreenUiEvent.AddressChanged -> intent {
                reduce { state.copy(address = event.address, errorMessage = null) }
            }
            is AddDropOffPointScreenUiEvent.PostalCodeChanged -> intent {
                reduce { state.copy(postalCode = event.postalCode, errorMessage = null) }
            }
            is AddDropOffPointScreenUiEvent.LatitudeChanged -> intent {
                reduce { state.copy(latitude = event.latitude, errorMessage = null) }
            }
            is AddDropOffPointScreenUiEvent.LongitudeChanged -> intent {
                reduce { state.copy(longitude = event.longitude, errorMessage = null) }
            }
            is AddDropOffPointScreenUiEvent.PrefillStore -> prefillStore(event.store)
            is AddDropOffPointScreenUiEvent.MapsInputChanged -> applyMapsInput(event.mapsInput)
            is AddDropOffPointScreenUiEvent.ResolveAddress -> resolveAddress(event.context)
            AddDropOffPointScreenUiEvent.OpenInGoogleMaps -> openInGoogleMaps()
            AddDropOffPointScreenUiEvent.Submit -> submit()
            AddDropOffPointScreenUiEvent.ClearMessage -> intent {
                reduce { state.copy(errorMessage = null, successMessage = null) }
            }
            else -> Unit
        }
    }

    private fun prefillStore(store: StoreData) {
        intent {
            if (state.storeId == store.id) return@intent
            val position = store.position
            reduce {
                state.copy(
                    storeId = store.id,
                    storeStatus = store.status.ifBlank { "active" },
                    name = TextFieldValue(store.name),
                    country = TextFieldValue(store.country.ifBlank { "ID" }),
                    address = TextFieldValue(store.address),
                    postalCode = TextFieldValue(store.postalCode),
                    latitude = TextFieldValue(position?.latitude?.toString().orEmpty()),
                    longitude = TextFieldValue(position?.longitude?.toString().orEmpty()),
                    mapsInput = if (position != null) {
                        TextFieldValue(
                            GoogleMapsPoint.searchUrl(position.latitude, position.longitude)
                        )
                    } else {
                        TextFieldValue()
                    }
                )
            }
        }
    }

    private fun applyMapsInput(mapsInput: TextFieldValue) {
        intent {
            val placeName = GoogleMapsPoint.extractPlaceName(mapsInput.text)
            val filledName = if (state.name.text.isBlank() && !placeName.isNullOrBlank()) {
                TextFieldValue(placeName)
            } else {
                state.name
            }

            val parsed = GoogleMapsPoint.parse(mapsInput.text)
            if (parsed != null) {
                reduce {
                    state.copy(
                        mapsInput = mapsInput,
                        name = filledName,
                        latitude = TextFieldValue(parsed.first.toString()),
                        longitude = TextFieldValue(parsed.second.toString()),
                        errorMessage = null
                    )
                }
                return@intent
            }

            if (!GoogleMapsPoint.isShortLink(mapsInput.text)) {
                reduce { state.copy(mapsInput = mapsInput, name = filledName) }
                return@intent
            }

            reduce {
                state.copy(
                    mapsInput = mapsInput,
                    name = filledName,
                    isResolvingAddress = true,
                    errorMessage = null
                )
            }
            val resolved = runCatching { mapsLinkResolver.resolve(mapsInput.text) }.getOrNull()
            if (resolved == null) {
                reduce {
                    state.copy(
                        isResolvingAddress = false,
                        errorMessage = "Could not read coordinates from that Maps link"
                    )
                }
                return@intent
            }
            reduce {
                state.copy(
                    isResolvingAddress = false,
                    latitude = TextFieldValue(resolved.first.toString()),
                    longitude = TextFieldValue(resolved.second.toString()),
                    errorMessage = null
                )
            }
        }
    }

    private fun resolveAddress(context: Context) {
        intent {
            val query = listOf(state.address.text, state.postalCode.text, state.country.text)
                .map { it.trim() }
                .filter { it.isNotBlank() }
                .joinToString(", ")
            if (query.isBlank()) {
                reduce { state.copy(errorMessage = "Enter an address first") }
                return@intent
            }

            reduce { state.copy(isResolvingAddress = true, errorMessage = null) }
            val location = runCatching {
                withContext(appDispatchers.io) {
                    @Suppress("DEPRECATION")
                    Geocoder(context, Locale.getDefault()).getFromLocationName(query, 1)?.firstOrNull()
                }
            }.getOrNull()

            if (location == null) {
                reduce {
                    state.copy(
                        isResolvingAddress = false,
                        errorMessage = "Could not find that address on Google Maps"
                    )
                }
                return@intent
            }

            reduce {
                state.copy(
                    isResolvingAddress = false,
                    latitude = TextFieldValue(location.latitude.toString()),
                    longitude = TextFieldValue(location.longitude.toString())
                )
            }
        }
    }

    private fun openInGoogleMaps() {
        intent {
            val point = state.parsedPoint
            if (point == null) {
                reduce { state.copy(errorMessage = "Set a map point first") }
                return@intent
            }
            deeplinkHandler.openUrl(GoogleMapsPoint.searchUrl(point.first, point.second))
        }
    }

    private fun submit() {
        intent {
            val name = state.name.text.trim()
            val country = state.country.text.trim()
            val address = state.address.text.trim()
            val postalCode = state.postalCode.text.trim()
            val point = state.parsedPoint

            val validationError = when {
                name.length < 3 -> "Name must be at least 3 characters"
                country.length < 2 -> "Country code is required"
                address.isBlank() -> "Address is required"
                postalCode.isBlank() -> "Postal code is required"
                point == null -> "Google Maps point is required"
                else -> null
            }
            if (validationError != null) {
                reduce { state.copy(errorMessage = validationError) }
                return@intent
            }

            reduce { state.copy(isLoading = true, errorMessage = null, successMessage = null) }
            val mapsPoint = requireNotNull(point)
            val editingId = state.storeId
            val position = StorePositionRequest(mapsPoint.first, mapsPoint.second)
            runCatching {
                if (editingId.isNullOrBlank()) {
                    storeRepository.createStore(
                        CreateStoreRequest(
                            name = name,
                            country = country,
                            address = address,
                            postalCode = postalCode,
                            position = position
                        )
                    )
                } else {
                    storeRepository.updateStore(
                        editingId,
                        UpdateStoreRequest(
                            name = name,
                            country = country,
                            address = address,
                            postalCode = postalCode,
                            position = position,
                            status = state.storeStatus
                        )
                    )
                }
            }.onSuccess {
                reduce {
                    state.copy(
                        isLoading = false,
                        successMessage = if (editingId.isNullOrBlank()) {
                            "Drop-off point added"
                        } else {
                            "Drop-off point updated"
                        }
                    )
                }
            }.onFailure { error ->
                reduce {
                    state.copy(
                        isLoading = false,
                        errorMessage = error.message ?: if (editingId.isNullOrBlank()) {
                            "Failed to add drop-off point"
                        } else {
                            "Failed to update drop-off point"
                        }
                    )
                }
            }
        }
    }
}
