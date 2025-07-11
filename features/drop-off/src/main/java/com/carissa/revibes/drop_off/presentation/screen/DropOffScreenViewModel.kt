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
import com.carissa.revibes.drop_off.data.DropOffRepository
import com.carissa.revibes.drop_off.data.SubmitOrderItemData
import com.carissa.revibes.drop_off.domain.model.StoreData
import com.carissa.revibes.drop_off.presentation.handler.DropOffExceptionHandler
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.koin.android.annotation.KoinViewModel

data class DropOffScreenUiState(
    val isLoading: Boolean = false,
    val stores: ImmutableList<StoreData> = persistentListOf(),
    val currentOrderId: String? = null,
    val items: ImmutableList<DropOffItem> = persistentListOf(),
)

sealed interface DropOffScreenUiEvent : NavigationEvent {
    data object NavigateToProfile : DropOffScreenUiEvent
    data object NavigateToHome : DropOffScreenUiEvent
    data object LoadDropOffData : DropOffScreenUiEvent
    data class AddItemToOrder(val orderId: String) : DropOffScreenUiEvent
    data object MakeOrder : DropOffScreenUiEvent
    data class UpdateItem(val index: Int, val item: DropOffItem) : DropOffScreenUiEvent
    data class RemoveItem(val index: Int) : DropOffScreenUiEvent
    data class OnNameChange(val value: TextFieldValue) : DropOffScreenUiEvent
    data class OnStoreSelected(val storeData: StoreData) : DropOffScreenUiEvent
    data class OnMakeOrderFailed(val message: String) : DropOffScreenUiEvent

    data class GetPresignedUrl(
        val orderId: String,
        val itemId: String,
        val contentType: String
    ) : DropOffScreenUiEvent

    data class SubmitOrder(
        val orderId: String,
        val type: String,
        val name: String,
        val country: String,
        val storeLocation: String,
        val items: List<SubmitOrderItemData>
    ) : DropOffScreenUiEvent
}

data class DropOffItem(
    val id: String,
    val name: String = "",
    val type: String = "",
    val weight: Int? = null
)

@KoinViewModel
class DropOffScreenViewModel(
    private val navigationEventBus: NavigationEventBus,
    private val dropOffRepository: DropOffRepository,
    private val dropOffExceptionHandler: DropOffExceptionHandler,
) : BaseViewModel<DropOffScreenUiState, DropOffScreenUiEvent>(
    initialState = DropOffScreenUiState(),
    onCreate = {
        onEvent(DropOffScreenUiEvent.LoadDropOffData)
    },
    exceptionHandler = { syntax, exception ->
        dropOffExceptionHandler.onDropOffError(syntax, exception)
    }
) {
    var name by mutableStateOf(TextFieldValue(""))
        private set
    var selectedStore by mutableStateOf<StoreData?>(null)
        private set

    override fun onEvent(event: DropOffScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is DropOffScreenUiEvent.NavigateToProfile -> navigationEventBus.post(event)
                is DropOffScreenUiEvent.NavigateToHome -> navigationEventBus.post(event)
                is DropOffScreenUiEvent.LoadDropOffData -> loadDropOffData()
                is DropOffScreenUiEvent.AddItemToOrder -> addItemToOrder(event.orderId)
                is DropOffScreenUiEvent.MakeOrder -> makeOrder()
                is DropOffScreenUiEvent.UpdateItem -> updateItem(event.index, event.item)
                is DropOffScreenUiEvent.RemoveItem -> removeItem(event.index)
                is DropOffScreenUiEvent.OnNameChange -> onNameChange(event.value)
                is DropOffScreenUiEvent.OnStoreSelected -> onStoreSelected(event.storeData)

                is DropOffScreenUiEvent.GetPresignedUrl -> getPresignedUrlForMedia(
                    event.orderId,
                    event.itemId,
                    event.contentType
                )

                is DropOffScreenUiEvent.SubmitOrder -> submitLogisticOrder(
                    event.orderId,
                    event.type,
                    event.name,
                    event.country,
                    event.storeLocation,
                    event.items
                )

                is DropOffScreenUiEvent.OnMakeOrderFailed -> Unit
            }
        }
    }

    private fun onNameChange(value: TextFieldValue) {
        name = value
    }

    private fun onStoreSelected(storeData: StoreData) {
        selectedStore = storeData
    }

    private fun loadDropOffData() {
        intent {
            reduce { state.copy(isLoading = true) }

            coroutineScope {
                val storesDeferred = async {
                    dropOffRepository.getStores(
                        longitude = 106.8456,
                        latitude = -6.2088
                    )
                }
                val orderIdDeferred = async { dropOffRepository.createLogisticOrder() }

                val (stores, orderId) = storesDeferred.await() to orderIdDeferred.await()
                reduce {
                    state.copy(
                        stores = stores.toImmutableList(),
                        currentOrderId = orderId,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun addItemToOrder(orderId: String) {
        intent {
            reduce { state.copy(isLoading = true) }
            val itemId = dropOffRepository.createLogisticOrderItem(orderId = orderId)
            val newItems = state.items.toMutableList().apply {
                add(DropOffItem(id = itemId))
            }.toImmutableList()
            reduce { state.copy(isLoading = false, items = newItems) }
        }
    }

    private fun getPresignedUrlForMedia(
        orderId: String,
        itemId: String,
        contentType: String
    ) {
        intent {
            reduce { state.copy(isLoading = true) }
            dropOffRepository.getPresignedUrl(
                orderId = orderId,
                itemId = itemId,
                contentType = contentType
            )
            reduce { state.copy(isLoading = false) }
        }
    }

    private fun submitLogisticOrder(
        orderId: String,
        type: String,
        name: String,
        country: String,
        storeLocation: String,
        items: List<SubmitOrderItemData>
    ) {
        intent {
            reduce { state.copy(isLoading = true) }
            dropOffRepository.submitOrder(
                orderId = orderId,
                type = type,
                name = name,
                country = country,
                storeLocation = storeLocation,
                items = items
            )
            reduce { state.copy(isLoading = false) }
            navigationEventBus.post(DropOffScreenUiEvent.NavigateToHome)
        }
    }

    private fun makeOrder() {
        intent {
            reduce { state.copy(isLoading = true) }
            state.currentOrderId?.let { orderId ->
                val orderItems = state.items.map { item ->
                    SubmitOrderItemData(
                        id = item.id,
                        name = item.name,
                        type = item.type,
                        weight = item.weight ?: -1
                    )
                }

                onEvent(
                    DropOffScreenUiEvent.SubmitOrder(
                        orderId = orderId,
                        type = ORDER_TYPE_DROP_OFF,
                        name = name.text,
                        country = selectedStore?.country.orEmpty(),
                        storeLocation = selectedStore?.id.orEmpty(),
                        items = orderItems
                    )
                )
                reduce { state.copy(isLoading = false) }
            }
        }
    }

    private fun updateItem(index: Int, item: DropOffItem) {
        intent {
            reduce {
                state.copy(
                    items = state.items.toMutableList().also { it[index] = item }.toImmutableList()
                )
            }
        }
    }

    private fun removeItem(index: Int) {
        intent {
            reduce {
                state.copy(
                    items = state.items.toMutableList().also { it.removeAt(index) }
                        .toImmutableList()
                )
            }
        }
    }
    companion object {
        private const val ORDER_TYPE_DROP_OFF = "drop-off"
    }
}
