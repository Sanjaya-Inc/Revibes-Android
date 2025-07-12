/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
package com.carissa.revibes.help_center.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.help_center.presentation.model.HelpCenterChildData
import com.carissa.revibes.help_center.presentation.model.HelpCenterRootData
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.toPersistentList
import org.koin.android.annotation.KoinViewModel

data class HelpCenterScreenUiState(
    val isLoading: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val items: PersistentList<HelpCenterRootData> = HelpCenterRootData.default()
) {
    val filteredItems
        get() = if (searchValue.text.isNotBlank()) {
            items.filter {
                it.title.contains(searchValue.text) || it.children.any { child ->
                    child.question.contains(
                        searchValue.text
                    )
                }
            }.map {
                it.copy(
                    children = it.children.filter { child ->
                        child.question.contains(searchValue.text)
                    }.toPersistentList()
                )
            }
        } else {
            items
        }
}

sealed interface HelpCenterScreenUiEvent {
    data class OnSearchChange(val query: TextFieldValue) : HelpCenterScreenUiEvent
    data object NavigateToProfile : HelpCenterScreenUiEvent, NavigationEvent
    data object ChatSupport : HelpCenterScreenUiEvent
    data class OnHelpChildClicked(
        val item: HelpCenterChildData
    ) : HelpCenterScreenUiEvent
}

@KoinViewModel
class HelpCenterScreenViewModel(
    private val navigationEventBus: NavigationEventBus,
) :
    BaseViewModel<HelpCenterScreenUiState, HelpCenterScreenUiEvent>(HelpCenterScreenUiState()) {

    override fun onEvent(event: HelpCenterScreenUiEvent) {
        super.onEvent(event)
        intent {
            when (event) {
                is HelpCenterScreenUiEvent.OnSearchChange -> reduce {
                    state.copy(searchValue = event.query)
                }

                is HelpCenterScreenUiEvent.NavigateToProfile -> navigationEventBus.post(event)
                is HelpCenterScreenUiEvent.ChatSupport -> postSideEffect(event)
                is HelpCenterScreenUiEvent.OnHelpChildClicked -> reduce {
                    val updatedItems = state.items.map { root ->
                        val updatedChildren = root.children.map { child ->
                            if (child == event.item) {
                                child.copy(isExpanded = !child.isExpanded)
                            } else {
                                child.copy(isExpanded = false)
                            }
                        }
                        root.copy(children = updatedChildren.toPersistentList())
                    }.toPersistentList()

                    state.copy(items = updatedItems)
                }

                else -> Unit
            }
        }
    }
}
