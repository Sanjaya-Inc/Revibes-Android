/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.drop_off.presentation.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.koin.android.annotation.KoinViewModel

data class DropOffScreenUiState(val isLoading: Boolean = false)

sealed interface DropOffScreenUiEvent : NavigationEvent {
    data object NavigateToProfile : DropOffScreenUiEvent
}

// Data class for each item
data class DropOffItem(
    val name: String = "",
    val type: String = "",
    val weight: String = ""
)

// Add LocationInfo data class inside the ViewModel file (top-level or inside the ViewModel class)
data class LocationInfo(
    val name: String,
    val address: String,
    val postalCode: Long,
    val distanceKm: Double
)

@KoinViewModel
class DropOffScreenViewModel(
    private val navigationEventBus: NavigationEventBus
) : BaseViewModel<DropOffScreenUiState, DropOffScreenUiEvent>(DropOffScreenUiState()) {

    private val _items = MutableStateFlow(emptyList<DropOffItem>())
    val items: StateFlow<List<DropOffItem>> = _items.asStateFlow()

    // New fields for the form
    var name by mutableStateOf(TextFieldValue(""))
        private set
    var selectedLocation by mutableStateOf<LocationInfo?>(null)
        private set

    // Add mock nearest locations
    val nearestLocations = persistentListOf(
        LocationInfo("Revibes Drop Off - Mall A", "Jl. Sudirman No. 1, Jakarta", 10240, 1.2),
        LocationInfo("Revibes Drop Off - Plaza B", "Jl. Thamrin No. 2, Jakarta", 10240, 2.0),
        LocationInfo("Revibes Drop Off - Market C", "Jl. Merdeka No. 3, Jakarta", 10240, 2.5)
    )

    fun onNameChange(value: TextFieldValue) {
        name = value
    }

    fun onLocationSelected(locationInfo: LocationInfo) {
        selectedLocation = locationInfo
    }

    override fun onEvent(event: DropOffScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            DropOffScreenUiEvent.NavigateToProfile -> navigationEventBus.post(event)
        }
    }

    fun addItem() {
        _items.value += DropOffItem()
    }

    fun makeOrder() {
        // TODO: Implement order creation logic
    }

    fun updateItem(index: Int, item: DropOffItem) {
        _items.value = _items.value.toMutableList().also { it[index] = item }
    }

    fun removeItem(index: Int) {
        if (_items.value.size > 1) {
            _items.value = _items.value.toMutableList().also { it.removeAt(index) }
        }
    }
}
