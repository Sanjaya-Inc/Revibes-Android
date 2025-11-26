package com.carissa.revibes.manage_voucher.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.core.presentation.compose.components.RevibesEmptyState
import com.carissa.revibes.manage_voucher.presentation.component.VoucherItem
import com.carissa.revibes.manage_voucher.presentation.navigation.ManageVoucherGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ManageVoucherGraph>(start = true)
@Composable
fun ManageVoucherScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: ManageVoucherScreenViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val listState = rememberLazyListState()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == androidx.lifecycle.Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(ManageVoucherScreenUiEvent.Refresh)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    viewModel.collectSideEffect { event ->
        when (event) {
            is ManageVoucherScreenUiEvent.OnToggleStatusSuccess -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is ManageVoucherScreenUiEvent.OnToggleStatusFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    val shouldLoadMore by remember {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val totalItemsNumber = layoutInfo.totalItemsCount
            val lastVisibleItemIndex = (layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: 0) + 1

            lastVisibleItemIndex > (totalItemsNumber - 3)
        }
    }

    LaunchedEffect(shouldLoadMore) {
        if (shouldLoadMore && !state.isLoadingMore) {
            viewModel.onEvent(ManageVoucherScreenUiEvent.LoadMore)
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            viewModel.onEvent(ManageVoucherScreenUiEvent.Refresh)
        }
    }

    LaunchedEffect(state.isLoading) {
        if (!state.isLoading) {
            isRefreshing = false
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Vouchers",
                        style = RevibesTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navigator.navigateUp()
                    }) {
                        Icon(
                            painter = painterResource(R.drawable.back_cta),
                            modifier = Modifier.size(86.dp),
                            tint = RevibesTheme.colors.primary,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = RevibesTheme.colors.primary
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.onEvent(ManageVoucherScreenUiEvent.NavigateToAddVoucher)
                },
                containerColor = RevibesTheme.colors.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Voucher",
                    tint = RevibesTheme.colors.onPrimary
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
                modifier = Modifier.fillMaxSize()
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    OutlinedTextField(
                        value = state.searchValue,
                        onValueChange = {
                            viewModel.onEvent(ManageVoucherScreenUiEvent.SearchValueChanged(it))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            com.carissa.revibes.core.presentation.compose.components.Text(
                                text = "Search vouchers...",
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
                }

                ContentStateSwitcher(
                    state.isLoading && state.vouchers.isEmpty(),
                    error = state.error,
                    actionButton = "Refresh" to {
                        viewModel.onEvent(ManageVoucherScreenUiEvent.Initialize)
                    }
                ) {
                    when {
                        state.filteredVouchers.isEmpty() && state.searchValue.text.isNotBlank() -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = "No vouchers found",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = RevibesTheme.colors.primary
                                    )
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text(
                                        text = "Try adjusting your search terms",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = RevibesTheme.colors.primary,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }

                        state.vouchers.isEmpty() -> {
                            RevibesEmptyState(
                                title = "No Vouchers Yet",
                                message = "Create your first voucher to get started",
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        else -> {
                            LazyColumn(
                                state = listState,
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(
                                    items = state.filteredVouchers,
                                    key = { it.id }
                                ) { voucher ->
                                    VoucherItem(
                                        voucher = voucher,
                                        onDeleteClick = {
                                            viewModel.onEvent(
                                                ManageVoucherScreenUiEvent.ShowDeleteDialog(voucher)
                                            )
                                        },
                                        onToggleStatus = {
                                            viewModel.onEvent(
                                                ManageVoucherScreenUiEvent.ToggleVoucherStatus(
                                                    voucher
                                                )
                                            )
                                        },
                                        onEditClick = {
                                            viewModel.onEvent(
                                                ManageVoucherScreenUiEvent.NavigateToEditVoucher(
                                                    voucher
                                                )
                                            )
                                        }
                                    )
                                }

                                if (state.isLoadingMore) {
                                    item {
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            horizontalArrangement = Arrangement.Center
                                        ) {
                                            CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Loading more...",
                                                style = MaterialTheme.typography.bodyMedium,
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

    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = {
                viewModel.onEvent(ManageVoucherScreenUiEvent.HideDeleteDialog)
            },
            title = {
                Text("Delete Voucher")
            },
            text = {
                Text(
                    "Are you sure you want to delete \"${state.voucherToDelete?.name}\"? This action cannot be undone."
                )
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(ManageVoucherScreenUiEvent.ConfirmDelete)
                    }
                ) {
                    Text(
                        text = "Delete",
                        color = RevibesTheme.colors.error
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.onEvent(ManageVoucherScreenUiEvent.HideDeleteDialog)
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}
