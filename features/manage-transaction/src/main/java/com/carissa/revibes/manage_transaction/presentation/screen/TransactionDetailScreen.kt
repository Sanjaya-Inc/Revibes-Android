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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import com.carissa.revibes.manage_transaction.R
import com.carissa.revibes.manage_transaction.domain.model.TransactionDetailDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionDetailItemDomain
import com.carissa.revibes.manage_transaction.domain.model.TransactionStatus
import com.carissa.revibes.manage_transaction.presentation.navigation.ManageTransactionGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ManageTransactionGraph>
@Composable
fun TransactionDetailScreen(
    transactionId: String,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: TransactionDetailScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.collectAsState()
    var showRejectDialog by remember { mutableStateOf(false) }
    var showCompleteDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    LaunchedEffect(transactionId) {
        viewModel.onEvent(TransactionDetailScreenUiEvent.LoadTransactionDetail(transactionId))
    }

    viewModel.collectSideEffect { event ->
        when (event) {
            is TransactionDetailScreenUiEvent.OnTransactionActionFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    LaunchedEffect(uiState.actionCompleted) {
        if (uiState.actionCompleted) {
            Toast.makeText(context, uiState.actionMessage, Toast.LENGTH_SHORT).show()
            navigator.navigateUp()
        }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.transaction_detail_title),
                        style = MaterialTheme.typography.headlineSmall
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.transactionDetail != null -> {
                    TransactionDetailContent(
                        transaction = uiState.transactionDetail!!,
                        onAccept = { showCompleteDialog = true },
                        onReject = { showRejectDialog = true },
                        isRejecting = uiState.isRejecting,
                        isProcessing = uiState.isProcessing
                    )
                }

                uiState.error != null -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.error!!,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }

    if (showRejectDialog) {
        RejectTransactionDialog(
            onConfirm = { reason ->
                viewModel.onEvent(TransactionDetailScreenUiEvent.RejectTransaction(reason))
                showRejectDialog = false
            },
            onDismiss = { showRejectDialog = false }
        )
    }

    if (showCompleteDialog) {
        CompleteTransactionDialog(
            onConfirm = {
                viewModel.onEvent(TransactionDetailScreenUiEvent.CompleteTransaction)
                showCompleteDialog = false
            },
            onDismiss = { showCompleteDialog = false }
        )
    }
}

@Composable
private fun TransactionDetailContent(
    transaction: TransactionDetailDomain,
    onAccept: () -> Unit,
    onReject: () -> Unit,
    isRejecting: Boolean,
    isProcessing: Boolean,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            TransactionInfoCard(transaction = transaction)
        }

        item {
            Text(
                text = stringResource(R.string.transaction_items),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
        }

        items(transaction.items) { item ->
            TransactionItemCard(item = item)
        }

        if (transaction.status == TransactionStatus.PENDING) {
            item {
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        text = stringResource(R.string.reject_transaction),
                        onClick = onReject,
                        enabled = !isProcessing && !isRejecting,
                        loading = isRejecting,
                        modifier = Modifier.weight(1f),
                        variant = ButtonVariant.SecondaryOutlined
                    )

                    Button(
                        text = stringResource(R.string.accept_transaction),
                        onClick = onAccept,
                        enabled = !isProcessing && !isRejecting,
                        loading = isProcessing,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun TransactionInfoCard(
    transaction: TransactionDetailDomain,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = stringResource(R.string.transaction_information),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            InfoRow(
                label = stringResource(R.string.transaction_id),
                value = transaction.id
            )

            InfoRow(
                label = stringResource(R.string.customer_name),
                value = transaction.name
            )

            InfoRow(
                label = stringResource(R.string.store_name),
                value = transaction.storeName
            )

            InfoRow(
                label = stringResource(R.string.address),
                value = transaction.address
            )

            InfoRow(
                label = stringResource(R.string.postal_code),
                value = transaction.postalCode
            )

            InfoRow(
                label = stringResource(R.string.total_points),
                value = transaction.totalPoint.toString()
            )

            InfoRow(
                label = stringResource(R.string.status),
                value = when (transaction.status) {
                    TransactionStatus.PENDING -> stringResource(R.string.status_pending)
                    TransactionStatus.COMPLETED -> stringResource(R.string.status_completed)
                    TransactionStatus.REJECTED -> stringResource(R.string.status_rejected)
                }
            )
        }
    }
}

@Composable
private fun TransactionItemCard(
    item: TransactionDetailItemDomain,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = item.name,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Medium
            )

            InfoRow(
                label = stringResource(R.string.type),
                value = item.type
            )

            InfoRow(
                label = stringResource(R.string.weight),
                value = stringResource(R.string.weight_kg, item.weight)
            )

            InfoRow(
                label = stringResource(R.string.points),
                value = item.point.toString()
            )
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(item.media) { imageUrl ->
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        placeholder = painterResource(R.drawable.image_placeholder)
                    )
                }
            }
        }
    }
}

@Composable
private fun InfoRow(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun RejectTransactionDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    var reason by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.reject_transaction_title))
        },
        text = {
            Column {
                Text(stringResource(R.string.reject_transaction_message))
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text(stringResource(R.string.rejection_reason_optional)) },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        },
        confirmButton = {
            if (reason.isNotBlank()) {
                TextButton(
                    onClick = { onConfirm(reason) }
                ) {
                    Text(stringResource(R.string.reject))
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}

@Composable
private fun CompleteTransactionDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(stringResource(R.string.accept_transaction_title))
        },
        text = {
            Text(stringResource(R.string.accept_transaction_message))
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(stringResource(R.string.accept))
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.cancel))
            }
        }
    )
}
