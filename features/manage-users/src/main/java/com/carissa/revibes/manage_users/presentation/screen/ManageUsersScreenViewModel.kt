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
import org.koin.android.annotation.KoinViewModel

data class ManageUsersScreenUiState(
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val searchValue: TextFieldValue = TextFieldValue(),
    val users: PersistentList<UserDomain> = persistentListOf(),
    val filteredUsers: PersistentList<UserDomain> = persistentListOf(),
    val pagination: PaginationData? = null,
    val error: String? = null
)

sealed interface ManageUsersScreenUiEvent : NavigationEvent {
    data class NavigateToEditUser(val userId: String) : ManageUsersScreenUiEvent
    data object NavigateToAddUser : ManageUsersScreenUiEvent
    data object Initialize : ManageUsersScreenUiEvent
    data object LoadMore : ManageUsersScreenUiEvent
    data object Refresh : ManageUsersScreenUiEvent
    data class SearchValueChanged(val searchValue: TextFieldValue) : ManageUsersScreenUiEvent
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

    override fun onEvent(event: ManageUsersScreenUiEvent) {
        super.onEvent(event)
        when (event) {
            ManageUsersScreenUiEvent.Initialize -> loadUsers()
            ManageUsersScreenUiEvent.LoadMore -> loadMoreUsers()
            ManageUsersScreenUiEvent.Refresh -> onRefresh()
            is ManageUsersScreenUiEvent.SearchValueChanged -> onSearchValueChanged(event.searchValue)
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

        val result = repository.getUserList(
            limit = DATA_PER_PAGE,
            lastDocId = lastDocId,
            direction = "next"
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
                filteredUsers = filterUsers(newUsers, state.searchValue.text),
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
        val filteredUsers = filterUsers(state.users, searchValue.text)
        reduce {
            state.copy(
                searchValue = searchValue,
                filteredUsers = filteredUsers
            )
        }
    }

    private fun onRefresh() {
        loadUsers(refresh = true)
    }

    private fun filterUsers(
        users: PersistentList<UserDomain>,
        searchQuery: String
    ): PersistentList<UserDomain> {
        return if (searchQuery.isBlank()) {
            users
        } else {
            users.filter { user ->
                user.name.contains(searchQuery, ignoreCase = true) ||
                    user.email.contains(searchQuery, ignoreCase = true) ||
                    user.phone.contains(searchQuery, ignoreCase = true) ||
                    user.role.name.contains(searchQuery, ignoreCase = true)
            }.toPersistentList()
        }
    }

    companion object {
        private const val DATA_PER_PAGE = 20
    }
}
