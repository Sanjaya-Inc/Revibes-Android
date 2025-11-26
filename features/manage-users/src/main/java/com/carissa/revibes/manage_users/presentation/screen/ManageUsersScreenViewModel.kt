package com.carissa.revibes.manage_users.presentation.screen

import androidx.compose.ui.text.input.TextFieldValue
import com.carissa.revibes.core.presentation.BaseViewModel
import com.carissa.revibes.core.presentation.navigation.NavigationEvent
import com.carissa.revibes.manage_users.data.ManageUsersRepository
import com.carissa.revibes.manage_users.data.model.PaginationData
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.handler.ManageUsersExceptionHandler
import kotlinx.collections.immutable.PersistentList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import org.koin.android.annotation.KoinViewModel

data class ManageUsersScreenUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val users: PersistentList<UserDomain> = persistentListOf(),
    val pagination: PaginationData? = null,
    val error: String? = null,
    val sortBy: SortType = SortType.CREATED_AT,
    val sortOrder: SortOrder = SortOrder.DESC
) {
    enum class SortType(val apiValue: String, val label: String) {
        DISPLAY_NAME("displayName", "Name"),
        CREATED_AT("createdAt", "Date")
    }

    enum class SortOrder(val apiValue: String, val label: String) {
        ASC("asc", "Ascending"),
        DESC("desc", "Descending")
    }
}

sealed interface ManageUsersScreenUiEvent {
    data class NavigateToEditUser(val userId: String) : NavigationEvent, ManageUsersScreenUiEvent
    data object NavigateToAddUser : NavigationEvent, ManageUsersScreenUiEvent
    data object Initialize : ManageUsersScreenUiEvent
    data object LoadMore : ManageUsersScreenUiEvent
    data object Refresh : ManageUsersScreenUiEvent
    data class SearchValueChanged(val searchValue: TextFieldValue) : ManageUsersScreenUiEvent
    data class SortByChanged(val sortBy: ManageUsersScreenUiState.SortType) : ManageUsersScreenUiEvent
    data class SortOrderChanged(val sortOrder: ManageUsersScreenUiState.SortOrder) : ManageUsersScreenUiEvent
    data class OnLoadUsersFailed(val message: String) : ManageUsersScreenUiEvent
}

@KoinViewModel
class ManageUsersScreenViewModel(
    private val repository: ManageUsersRepository,
    private val exceptionHandler: ManageUsersExceptionHandler
) : BaseViewModel<ManageUsersScreenUiState, ManageUsersScreenUiEvent>(
    initialState = ManageUsersScreenUiState(),
    onCreate = { onEvent(ManageUsersScreenUiEvent.Initialize) },
    exceptionHandler = { syntax, throwable ->
        exceptionHandler.onManageUsersError(syntax, throwable)
    }
) {
    private var searchJob: Job? = null

    override fun onEvent(event: ManageUsersScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ManageUsersScreenUiEvent.Initialize -> loadUsers()
            ManageUsersScreenUiEvent.LoadMore -> loadMoreUsers()
            ManageUsersScreenUiEvent.Refresh -> onRefresh()
            is ManageUsersScreenUiEvent.SearchValueChanged -> onSearchValueChanged(event.searchValue)
            is ManageUsersScreenUiEvent.SortByChanged -> onSortByChanged(event.sortBy)
            is ManageUsersScreenUiEvent.SortOrderChanged -> onSortOrderChanged(event.sortOrder)
            else -> Unit
        }
    }

    private fun loadUsers(refresh: Boolean = false) = intent {
        if (refresh) {
            reduce { state.copy(isLoading = true, error = null) }
        } else if (state.users.isEmpty()) {
            reduce { state.copy(isLoading = true, error = null) }
        } else {
            reduce { state.copy(isLoadingMore = true, error = null) }
        }

        val lastDocId = if (refresh) null else state.pagination?.lastDocId
        val searchQuery = state.searchValue.text.trim()

        val result = repository.getUserList(
            limit = DATA_PER_PAGE,
            sortBy = state.sortBy.apiValue,
            sortOrder = state.sortOrder.apiValue,
            lastDocId = lastDocId,
            direction = "next",
            search = searchQuery.ifBlank { null }
        )

        val newUsers = if (refresh) {
            result.users.toPersistentList()
        } else {
            (state.users + result.users).toPersistentList()
        }

        reduce {
            state.copy(
                isLoading = false,
                isLoadingMore = false,
                users = newUsers,
                pagination = result.pagination,
                error = null
            )
        }
    }

    private fun loadMoreUsers() = intent {
        if (state.pagination?.hasMoreNext == true && !state.isLoadingMore) {
            loadUsers(refresh = false)
        }
    }

    private fun onSearchValueChanged(searchValue: TextFieldValue) = intent {
        reduce { state.copy(searchValue = searchValue) }

        searchJob?.cancel()
        searchJob = intent {
            delay(SEARCH_DEBOUNCE_MILLIS)
            loadUsers(refresh = true)
        }
    }

    private fun onSortByChanged(sortBy: ManageUsersScreenUiState.SortType) = intent {
        if (state.sortBy != sortBy) {
            reduce { state.copy(sortBy = sortBy) }
            loadUsers(refresh = true)
        }
    }

    private fun onSortOrderChanged(sortOrder: ManageUsersScreenUiState.SortOrder) = intent {
        if (state.sortOrder != sortOrder) {
            reduce { state.copy(sortOrder = sortOrder) }
            loadUsers(refresh = true)
        }
    }

    private fun onRefresh() {
        loadUsers(refresh = true)
    }

    companion object {
        private const val DATA_PER_PAGE = 20
        private const val SEARCH_DEBOUNCE_MILLIS = 500L
    }
}
