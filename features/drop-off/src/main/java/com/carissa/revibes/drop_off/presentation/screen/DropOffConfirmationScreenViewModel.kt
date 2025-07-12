/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.drop_off.presentation.screen

import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.drop_off.data.DropOffRepository
import com.carissa.revibes.drop_off.data.SubmitOrderItemData
import com.carissa.revibes.drop_off.domain.model.StoreData
import com.carissa.revibes.drop_off.presentation.handler.DropOffExceptionHandler
import org.koin.android.annotation.KoinViewModel

data class DropOffConfirmationScreenUiState(
    val isLoading: Boolean = false,
)

sealed interface DropOffConfirmationScreenUiEvent {
    data object NavigateToHome : DropOffConfirmationScreenUiEvent, NavigationEvent

    data class MakeOrder(val arguments: DropOffConfirmationScreenArguments) : DropOffConfirmationScreenUiEvent

    data class OnMakeOrderFailed(val message: String) : DropOffConfirmationScreenUiEvent
}

@KoinViewModel
class DropOffConfirmationScreenViewModel(
    private val dropOffRepository: DropOffRepository,
    private val dropOffExceptionHandler: DropOffExceptionHandler,
) : BaseViewModel<DropOffConfirmationScreenUiState, DropOffConfirmationScreenUiEvent>(
    initialState = DropOffConfirmationScreenUiState(),
    exceptionHandler = { syntax, exception ->
        dropOffExceptionHandler.onDropOffError(syntax, exception)
    }
) {
    override fun onEvent(event: DropOffConfirmationScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is DropOffConfirmationScreenUiEvent.MakeOrder -> {
                    val orderItems = event.arguments.items.map { item ->
                        SubmitOrderItemData(
                            id = item.id,
                            name = item.name,
                            type = item.type,
                            weight = item.weight?.second ?: -1
                        )
                    }
                    submitLogisticOrder(
                        orderId = event.arguments.orderId,
                        type = event.arguments.type,
                        name = event.arguments.name,
                        store = event.arguments.store,
                        items = orderItems
                    )
                }

                is DropOffConfirmationScreenUiEvent.OnMakeOrderFailed -> Unit
                else -> Unit
            }
        }
    }

    private fun submitLogisticOrder(
        orderId: String,
        type: String,
        name: String,
        store: StoreData,
        items: List<SubmitOrderItemData>
    ) {
        intent {
            reduce { state.copy(isLoading = true) }
            dropOffRepository.submitOrder(
                orderId = orderId,
                type = type,
                name = name,
                country = store.country,
                storeId = store.id,
                items = items
            )
            reduce { state.copy(isLoading = false) }
            onEvent(DropOffConfirmationScreenUiEvent.NavigateToHome)
        }
    }
}
