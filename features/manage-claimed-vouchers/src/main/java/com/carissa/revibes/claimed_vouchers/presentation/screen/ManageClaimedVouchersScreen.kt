package com.carissa.revibes.claimed_vouchers.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.carissa.revibes.claimed_vouchers.R
import com.carissa.revibes.claimed_vouchers.presentation.component.ClaimedVoucherItem
import com.carissa.revibes.claimed_vouchers.presentation.navigation.ManageClaimedVouchersGraph
import com.carissa.revibes.core.presentation.EventReceiver
import androidx.compose.material3.MaterialTheme
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ManageClaimedVouchersGraph>(start = true)
@Composable
fun ManageClaimedVouchersScreen(
    modifier: Modifier = Modifier,
    viewModel: ManageClaimedVouchersScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { event ->
        when (event) {
            is ManageClaimedVouchersScreenUiEvent.OnLoadClaimedVouchersFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }
            else -> Unit
        }
    }

    ManageClaimedVouchersScreenContent(
        uiState = uiState,
        modifier = modifier.fillMaxSize(),
        eventReceiver = viewModel,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManageClaimedVouchersScreenContent(
    uiState: ManageClaimedVouchersScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ManageClaimedVouchersScreenUiEvent> = EventReceiver { },
) {
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember { mutableStateOf(false) }
    val navigator = RevibesTheme.navigator

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            eventReceiver.onEvent(ManageClaimedVouchersScreenUiEvent.RefreshClaimedVouchers)
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
                    Text(
                        text = stringResource(R.string.manage_claimed_vouchers_title),
                        style = MaterialTheme.typography.headlineMedium,
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
                // Search field
                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = {
                        eventReceiver.onEvent(
                            ManageClaimedVouchersScreenUiEvent.OnSearchQueryChanged(it)
                        )
                    },
                    label = { Text(stringResource(R.string.search_claimed_vouchers)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    singleLine = true
                )

                ContentStateSwitcher(
                    uiState.isLoading && uiState.claimedVouchers.isEmpty(),
                    error = uiState.error,
                    actionButton = "Refresh" to {
                        eventReceiver.onEvent(ManageClaimedVouchersScreenUiEvent.RefreshClaimedVouchers)
                    }
                ) {
                    when {
                        uiState.filteredClaimedVouchers.isEmpty() && !uiState.isLoading -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = if (uiState.searchQuery.isNotBlank()) {
                                        stringResource(R.string.no_claimed_vouchers_found_for_search)
                                    } else {
                                        stringResource(R.string.no_claimed_vouchers_found)
                                    },
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
                                items(uiState.filteredClaimedVouchers) { claimedVoucher ->
                                    ClaimedVoucherItem(
                                        claimedVoucher = claimedVoucher,
                                        onClick = {
                                            // Handle click if needed (e.g., navigate to detail)
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
