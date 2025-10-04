package com.carissa.revibes.manage_transaction.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.manage_transaction.R
import com.carissa.revibes.manage_transaction.presentation.component.TransactionItem
import com.carissa.revibes.manage_transaction.presentation.navigation.ManageTransactionGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.managetransaction.destinations.TransactionDetailScreenDestination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ManageTransactionGraph>(start = true)
@Composable
fun ManageTransactionScreen(
    modifier: Modifier = Modifier,
    viewModel: ManageTransactionScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current
    val navigator = RevibesTheme.navigator

    viewModel.collectSideEffect { event ->
        when (event) {
            is ManageTransactionScreenUiEvent.NavigateToDetail -> {
                navigator.navigate(TransactionDetailScreenDestination(event.transactionId))
            }

            is ManageTransactionScreenUiEvent.OnLoadTransactionsFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    ManageTransactionScreenContent(
        uiState = uiState,
        modifier = modifier.fillMaxSize(),
        eventReceiver = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageTransactionScreenContent(
    uiState: ManageTransactionScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ManageTransactionScreenUiEvent> = EventReceiver { },
) {
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val navigator = RevibesTheme.navigator

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            eventReceiver.onEvent(ManageTransactionScreenUiEvent.RefreshTransactions)
            pullToRefreshState.animateToHidden()
            isRefreshing = false
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = stringResource(R.string.manage_transaction_title),
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
                            painter = painterResource(com.carissa.revibes.core.R.drawable.back_cta),
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
        }
    ) { paddingValues ->
        PullToRefreshBox(
            state = pullToRefreshState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            isRefreshing = isRefreshing,
            onRefresh = { isRefreshing = true },
            contentAlignment = Alignment.TopCenter,
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = uiState.searchQuery,
                        onValueChange = {
                            eventReceiver.onEvent(
                                ManageTransactionScreenUiEvent.OnSearchQueryChanged(
                                    it
                                )
                            )
                        },
                        label = { Text(stringResource(R.string.search_transactions)) },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )

                    StatusFilterDropdown(
                        selectedStatus = uiState.selectedStatus,
                        onStatusSelected = { status ->
                            eventReceiver.onEvent(
                                ManageTransactionScreenUiEvent.OnStatusFilterChanged(status)
                            )
                        }
                    )
                }

                ContentStateSwitcher(uiState.isLoading && uiState.transactions.isEmpty()) {
                    when {
                        uiState.transactions.isEmpty() && !uiState.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = when (uiState.selectedStatus) {
                                        TransactionStatus.ALL -> stringResource(R.string.no_transactions_found)
                                        TransactionStatus.PENDING -> stringResource(R.string.no_pending_transactions)
                                        TransactionStatus.REJECTED -> stringResource(R.string.no_rejected_transactions)
                                        TransactionStatus.COMPLETED -> stringResource(
                                            R.string.no_completed_transactions
                                        )
                                    },
                                    style = RevibesTheme.typography.body1,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(16.dp)
                                )
                            }
                        }

                        else -> {
                            LazyColumn(
                                contentPadding = PaddingValues(16.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(uiState.filteredTransactions) { transaction ->
                                    TransactionItem(
                                        transaction = transaction,
                                        onClick = {
                                            eventReceiver.onEvent(
                                                ManageTransactionScreenUiEvent.NavigateToDetail(
                                                    transaction.id
                                                )
                                            )
                                        }
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

@Composable
private fun StatusFilterDropdown(
    selectedStatus: TransactionStatus,
    onStatusSelected: (TransactionStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true }
        ) {
            Text(text = selectedStatus.displayName)
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            TransactionStatus.entries.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status.displayName) },
                    onClick = {
                        onStatusSelected(status)
                        expanded = false
                    }
                )
            }
        }
    }
}
