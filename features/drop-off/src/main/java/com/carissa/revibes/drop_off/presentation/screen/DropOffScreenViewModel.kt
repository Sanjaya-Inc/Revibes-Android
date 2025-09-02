/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.drop_off.presentation.screen

import android.content.Context
import android.net.Uri
import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.data.main.remote.config.ConfigRepository
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.drop_off.data.DropOffRepository
import com.carissa.revibes.drop_off.domain.model.StoreData
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.Serializable
import org.koin.android.annotation.KoinViewModel

data class DropOffScreenUiState(
    val isLoading: Boolean = false,
    val isMaintenance: Boolean = false,
    val stores: ImmutableList<StoreData> = persistentListOf(),
    val currentOrderId: String? = null,
    val items: ImmutableList<DropOffItem> = persistentListOf(),
    val selectedStore: StoreData? = null,
    val name: TextFieldValue = TextFieldValue(),
    val isFormValid: Boolean = false,
    val validationErrors: ValidationErrors = ValidationErrors(),
)

data class ValidationErrors(
    val nameError: String? = null,
    val storeError: String? = null,
    val itemsError: String? = null,
)

sealed interface DropOffScreenUiEvent {
    data object NavigateBack : DropOffScreenUiEvent
    data object NavigateToProfile : DropOffScreenUiEvent, NavigationEvent
    data class NavigateToConfirmOrder(val arguments: DropOffConfirmationScreenArguments) :
        DropOffScreenUiEvent, NavigationEvent

    data object LoadDropOffData : DropOffScreenUiEvent
    data class OnLoadDropOffDataFailed(override val message: String) : DropOffScreenUiEvent,
        Throwable(message)

    data class AddItemToOrder(val orderId: String) : DropOffScreenUiEvent
    data object MakeOrder : DropOffScreenUiEvent
    data class UpdateItem(val index: Int, val item: DropOffItem) : DropOffScreenUiEvent
    data class RemoveItem(val index: Int) : DropOffScreenUiEvent
    data class OnNameChange(val value: TextFieldValue) : DropOffScreenUiEvent
    data class OnStoreSelected(val storeData: StoreData) : DropOffScreenUiEvent
    data class GetPresignedUrl(
        val orderId: String,
        val itemId: String,
        val contentType: String
    ) : DropOffScreenUiEvent

    data class UploadImage(
        val context: Context,
        val orderId: String,
        val itemId: String,
        val itemIndex: Int,
        val imageUri: Uri,
        val contentType: String
    ) : DropOffScreenUiEvent

    data class OnImageUploadSuccess(
        val itemIndex: Int,
        val imageUrl: String
    ) : DropOffScreenUiEvent

    data class OnImageUploadFailed(
        val message: String
    ) : DropOffScreenUiEvent
}

@Serializable
data class DropOffItem(
    val id: String,
    val name: String = "",
    val type: String = "",
    val weight: Pair<String, Int>? = null,
    val photos: List<String> = emptyList(),
)

@KoinViewModel
class DropOffScreenViewModel(
    private val dropOffRepository: DropOffRepository,
    configRepository: ConfigRepository
) : BaseViewModel<DropOffScreenUiState, DropOffScreenUiEvent>(
    initialState = DropOffScreenUiState(
        isMaintenance = !configRepository.getDropOffFeatureFlagEnabled()
    ),
    onCreate = {
        if (!it.state.isMaintenance) {
            onEvent(DropOffScreenUiEvent.LoadDropOffData)
        }
    },
    exceptionHandler = { syntax, throwable ->
        when (throwable) {
            is DropOffScreenUiEvent.OnLoadDropOffDataFailed -> {
                syntax.postSideEffect(throwable)
                syntax.reduce {
                    state.copy(isLoading = false)
                }
            }
        }
    }
) {
    override fun onEvent(event: DropOffScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is DropOffScreenUiEvent.NavigateBack -> postSideEffect(event)
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

                is DropOffScreenUiEvent.UploadImage -> uploadImage(
                    event.context,
                    event.orderId,
                    event.itemId,
                    event.itemIndex,
                    event.imageUri,
                    event.contentType
                )

                is DropOffScreenUiEvent.OnImageUploadSuccess -> onImageUploadSuccess(
                    event.itemIndex,
                    event.imageUrl
                )

                is DropOffScreenUiEvent.OnImageUploadFailed -> Unit
                else -> Unit
            }
        }
    }

    private fun onNameChange(value: TextFieldValue) = intent {
        reduce { state.copy(name = value) }
        validateForm()
    }

    private fun onStoreSelected(storeData: StoreData) = intent {
        reduce { state.copy(selectedStore = storeData) }
        validateForm()
    }

    private fun validateForm() {
        intent {
            val validationResult = validateFormWithErrors(state)
            reduce {
                state.copy(
                    isFormValid = validationResult.first,
                    validationErrors = validationResult.second
                )
            }
        }
    }

    private fun validateFormWithErrors(state: DropOffScreenUiState): Pair<Boolean, ValidationErrors> {
        val errors = ValidationErrors()
        var isValid = true

        val nameError = if (state.name.text.trim().isEmpty()) {
            isValid = false
            NAME_REQUIRED_ERROR
        } else {
            null
        }

        val storeError = if (state.selectedStore == null) {
            isValid = false
            LOCATION_REQUIRED_ERROR
        } else {
            null
        }

        val itemsError = when {
            state.items.isEmpty() -> {
                isValid = false
                ITEM_REQUIRED_ERROR
            }

            !state.items.all { item ->
                item.name.trim().isNotEmpty() &&
                    item.type.isNotEmpty() &&
                    item.weight != null &&
                    item.photos.isNotEmpty()
            } -> {
                isValid = false
                ITEM_DETAILS_REQUIRED_ERROR
            }

            else -> null
        }

        return isValid to errors.copy(
            nameError = nameError,
            storeError = storeError,
            itemsError = itemsError
        )
    }

    private fun isFormValid(state: DropOffScreenUiState): Boolean {
        return validateFormWithErrors(state).first
    }

    private fun loadDropOffData() {
        intent {
            reduce { state.copy(isLoading = true) }

            coroutineScope {
                runCatching {
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
                }.onFailure {
                    throw DropOffScreenUiEvent.OnLoadDropOffDataFailed(
                        it.message ?: "Something went wrong!"
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
            validateForm()
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

    private fun uploadImage(
        context: Context,
        orderId: String,
        itemId: String,
        itemIndex: Int,
        imageUri: Uri,
        contentType: String
    ) {
        intent {
            reduce { state.copy(isLoading = true) }
            try {
                val (uploadUrl, downloadUrl, _) = dropOffRepository.getPresignedUrl(
                    orderId = orderId,
                    itemId = itemId,
                    contentType = contentType
                )

                val uploadSuccess = dropOffRepository.uploadImageToUrl(
                    context = context,
                    uploadUrl = uploadUrl,
                    imageUri = imageUri,
                    contentType = contentType
                )

                if (uploadSuccess) {
                    onEvent(DropOffScreenUiEvent.OnImageUploadSuccess(itemIndex, downloadUrl))
                } else {
                    onEvent(DropOffScreenUiEvent.OnImageUploadFailed(UPLOAD_FAILED_MESSAGE))
                }
            } catch (e: Exception) {
                onEvent(
                    DropOffScreenUiEvent.OnImageUploadFailed(
                        e.message ?: UNKNOWN_ERROR_MESSAGE
                    )
                )
            }
            reduce { state.copy(isLoading = false) }
        }
    }

    private fun onImageUploadSuccess(itemIndex: Int, imageUrl: String) {
        intent {
            reduce {
                val updatedItems = state.items.toMutableList()
                if (itemIndex < updatedItems.size) {
                    val currentItem = updatedItems[itemIndex]
                    val updatedPhotos = currentItem.photos.toMutableList().apply { add(imageUrl) }
                    updatedItems[itemIndex] = currentItem.copy(photos = updatedPhotos)
                }
                state.copy(items = updatedItems.toImmutableList())
            }
            validateForm()
        }
    }

    private fun makeOrder() {
        intent {
            if (!isFormValid(state)) {
                reduce { state.copy(isLoading = false) }
                return@intent
            }

            reduce { state.copy(isLoading = true) }
            val orderId = requireNotNull(
                value = state.currentOrderId
            ) { DROP_OFF_SESSION_FAILED_MESSAGE }
            val selectedStore = requireNotNull(
                value = state.selectedStore
            ) { DROP_OFF_SESSION_FAILED_MESSAGE }
            val arguments = DropOffConfirmationScreenArguments(
                orderId = orderId,
                type = ORDER_TYPE_DROP_OFF,
                name = state.name.text,
                store = selectedStore,
                items = state.items,
            )
            onEvent(DropOffScreenUiEvent.NavigateToConfirmOrder(arguments))
            reduce { state.copy(isLoading = false) }
        }
    }

    private fun updateItem(index: Int, item: DropOffItem) {
        intent {
            reduce {
                state.copy(
                    items = state.items.toMutableList().also { it[index] = item }.toImmutableList()
                )
            }
            validateForm()
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
            validateForm()
        }
    }

    private companion object {
        const val ORDER_TYPE_DROP_OFF = "drop-off"

        const val NAME_REQUIRED_ERROR = "Name is required"
        const val LOCATION_REQUIRED_ERROR = "Please select a drop-off location"
        const val ITEM_REQUIRED_ERROR = "Please add at least one item"
        const val ITEM_DETAILS_REQUIRED_ERROR =
            "Please complete all item details (name, type, weight, and photo)"
        const val UPLOAD_FAILED_MESSAGE = "Failed to upload image"
        const val UNKNOWN_ERROR_MESSAGE = "Unknown error"
        const val DROP_OFF_SESSION_FAILED_MESSAGE = "Failed to initiate the Drop off session!"
    }
}
