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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ManageUsersGraph>(start = true)
@Composable
fun ManageUsersScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: ManageUsersScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(ManageUsersScreenUiEvent.Refresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageUsersScreenContent(
    uiState: ManageUsersScreenUiState,
    onEvent: (ManageUsersScreenUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }

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

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            onEvent(ManageUsersScreenUiEvent.Refresh)
        }
    }

    LaunchedEffect(uiState.isLoading) {
        if (!uiState.isLoading) {
            isRefreshing = false
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
        PullToRefreshBox(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = isRefreshing,
            onRefresh = { isRefreshing = true },
            state = pullToRefreshState,
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
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

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Sort by:",
                        style = RevibesTheme.typography.body2,
                        color = RevibesTheme.colors.onSurface,
                        fontWeight = FontWeight.Medium
                    )

                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ManageUsersScreenUiState.SortType.entries) { sortType ->
                            FilterChip(
                                selected = uiState.sortBy == sortType,
                                onClick = { onEvent(ManageUsersScreenUiEvent.SortByChanged(sortType)) },
                                label = {
                                    Text(
                                        text = sortType.label,
                                        style = RevibesTheme.typography.body2,
                                        fontWeight = if (uiState.sortBy == sortType) {
                                            FontWeight.SemiBold
                                        } else {
                                            FontWeight.Normal
                                        }
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RevibesTheme.colors.primary,
                                    selectedLabelColor = RevibesTheme.colors.onPrimary,
                                    containerColor = RevibesTheme.colors.surface,
                                    labelColor = RevibesTheme.colors.onSurface
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = uiState.sortBy == sortType,
                                    borderColor = RevibesTheme.colors.onSurface.copy(alpha = 0.2f),
                                    selectedBorderColor = RevibesTheme.colors.primary,
                                    borderWidth = 1.dp,
                                    selectedBorderWidth = 1.dp
                                )
                            )
                        }

                        items(ManageUsersScreenUiState.SortOrder.entries) { sortOrder ->
                            FilterChip(
                                selected = uiState.sortOrder == sortOrder,
                                onClick = {
                                    onEvent(
                                        ManageUsersScreenUiEvent.SortOrderChanged(
                                            sortOrder
                                        )
                                    )
                                },
                                label = {
                                    Text(
                                        text = "${
                                            if (sortOrder == ManageUsersScreenUiState.SortOrder.ASC) {
                                                "⬆️"
                                            } else {
                                                "⬇️"
                                            }
                                        } ${sortOrder.label}",
                                        style = RevibesTheme.typography.body2,
                                        fontWeight = if (uiState.sortOrder == sortOrder) {
                                            FontWeight.SemiBold
                                        } else {
                                            FontWeight.Normal
                                        }
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = RevibesTheme.colors.primary,
                                    selectedLabelColor = RevibesTheme.colors.onPrimary,
                                    containerColor = RevibesTheme.colors.surface,
                                    labelColor = RevibesTheme.colors.onSurface
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = uiState.sortOrder == sortOrder,
                                    borderColor = RevibesTheme.colors.onSurface.copy(alpha = 0.2f),
                                    selectedBorderColor = RevibesTheme.colors.primary,
                                    borderWidth = 1.dp,
                                    selectedBorderWidth = 1.dp
                                )
                            )
                        }
                    }
                }

                ContentStateSwitcher(
                    uiState.isLoading && uiState.users.isEmpty(),
                    error = uiState.error,
                    actionButton = "Refresh" to {
                        onEvent(ManageUsersScreenUiEvent.Refresh)
                    }
                ) {
                    if (uiState.users.isEmpty()) {
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
                                items = uiState.users,
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
                )
            ),
            onEvent = {},
            modifier = Modifier.background(RevibesTheme.colors.background)
        )
    }
}
