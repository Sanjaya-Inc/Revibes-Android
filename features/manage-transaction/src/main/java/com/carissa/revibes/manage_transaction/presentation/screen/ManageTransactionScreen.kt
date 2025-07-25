package com.carissa.revibes.manage_transaction.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.components.RevibesTheme
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

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            eventReceiver.onEvent(ManageTransactionScreenUiEvent.RefreshTransactions)
            pullToRefreshState.animateToHidden()
            isRefreshing = false
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.manage_transaction_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true
                )

                when {
                    uiState.isLoading && uiState.transactions.isEmpty() -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }

                    uiState.transactions.isEmpty() && !uiState.isLoading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = stringResource(R.string.no_pending_transactions),
                                style = MaterialTheme.typography.bodyLarge,
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
