package com.carissa.revibes.manage_users.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.manage_users.R
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.component.UserItem
import com.carissa.revibes.manage_users.presentation.navigation.ManageUsersGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.manageusers.destinations.AddUserScreenDestination
import com.ramcosta.composedestinations.generated.manageusers.destinations.EditUserScreenDestination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<ManageUsersGraph>(start = true)
@Composable
fun ManageUsersScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: ManageUsersScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { event ->
        when (event) {
            is ManageUsersScreenUiEvent.NavigateToAddUser -> {
                navigator.navigate(AddUserScreenDestination)
            }

            is ManageUsersScreenUiEvent.NavigateToEditUser -> {
                navigator.navigate(EditUserScreenDestination(userId = event.userId))
            }

            is ManageUsersScreenUiEvent.OnLoadUsersFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    ManageUsersScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        modifier = modifier
    )
}

@Composable
private fun ManageUsersScreenContent(
    uiState: ManageUsersScreenUiState,
    onEvent: (ManageUsersScreenUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - 3)
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !uiState.isLoadingMore) {
            onEvent(ManageUsersScreenUiEvent.LoadMore)
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.manage_users),
                    style = RevibesTheme.typography.h1,
                    color = RevibesTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onEvent(ManageUsersScreenUiEvent.NavigateToAddUser) },
                containerColor = RevibesTheme.colors.primary,
                contentColor = RevibesTheme.colors.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.add_user)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
        ) {
            OutlinedTextField(
                value = uiState.searchValue,
                onValueChange = { onEvent(ManageUsersScreenUiEvent.SearchValueChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = stringResource(R.string.search_users),
                        style = RevibesTheme.typography.body1,
                        color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = RevibesTheme.colors.primary,
                    unfocusedBorderColor = RevibesTheme.colors.onSurface.copy(alpha = 0.2f)
                ),
                singleLine = true
            )

            ContentStateSwitcher(
                uiState.isLoading && uiState.users.isEmpty(),
                error = uiState.error,
                actionButton = "Refresh" to {
                    onEvent(ManageUsersScreenUiEvent.Refresh)
                }
            ) {
                if (uiState.filteredUsers.isEmpty() && uiState.searchValue.text.isNotBlank()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.no_users_found),
                            style = RevibesTheme.typography.body1,
                            color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                        )
                    }
                } else {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(
                            items = uiState.filteredUsers,
                            key = { it.id }
                        ) { user ->
                            UserItem(
                                user = user,
                                onUserClick = { userId ->
                                    onEvent(ManageUsersScreenUiEvent.NavigateToEditUser(userId))
                                }
                            )
                        }

                        if (uiState.isLoadingMore) {
                            item {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    CircularProgressIndicator(
                                        color = RevibesTheme.colors.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun ManageUsersScreenPreview() {
    RevibesTheme {
        ManageUsersScreenContent(
            uiState = ManageUsersScreenUiState(
                users = persistentListOf(
                    UserDomain.dummy(),
                    UserDomain.dummy()
                        .copy(id = "2", name = "Jane Smith", email = "jane@example.com")
                ),
                filteredUsers = persistentListOf(
                    UserDomain.dummy(),
                    UserDomain.dummy()
                        .copy(id = "2", name = "Jane Smith", email = "jane@example.com")
                )
            ),
            onEvent = {},
            modifier = Modifier.background(RevibesTheme.colors.background)
        )
    }
}
