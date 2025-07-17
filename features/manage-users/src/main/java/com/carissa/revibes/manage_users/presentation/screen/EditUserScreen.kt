package com.carissa.revibes.manage_users.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.manage_users.R
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.navigation.ManageUsersGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Destination<ManageUsersGraph>
@Composable
fun EditUserScreen(
    userId: String,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: EditUserScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { event ->
        when (event) {
            is EditUserScreenUiEvent.OnAddPointsFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is EditUserScreenUiEvent.OnLoadUserFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    LaunchedEffect(userId) {
        viewModel.onEvent(EditUserScreenUiEvent.SetUserId(userId))
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Successfully added points", Toast.LENGTH_SHORT).show()
        }
    }

    EditUserScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = { navigator.navigateUp() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditUserScreenContent(
    uiState: EditUserScreenUiState,
    onEvent: (EditUserScreenUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        containerColor = RevibesTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.edit_user),
                        style = RevibesTheme.typography.h2,
                        color = RevibesTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = RevibesTheme.colors.onSurface
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = RevibesTheme.colors.background
                ),
                modifier = Modifier.statusBarsPadding()
            )
        }
    ) { paddingValues ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = RevibesTheme.colors.primary
                    )
                }
            }

            uiState.error != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = uiState.error,
                            style = RevibesTheme.typography.body1,
                            color = RevibesTheme.colors.error,
                            textAlign = TextAlign.Center
                        )
                        Button(
                            text = stringResource(R.string.refresh),
                            onClick = { onEvent(EditUserScreenUiEvent.LoadUserDetail) }
                        )
                    }
                }
            }

            uiState.user != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UserDetailCard(user = uiState.user)

                    Button(
                        text = stringResource(R.string.add_points),
                        onClick = { onEvent(EditUserScreenUiEvent.ShowAddPointsDialog) },
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }
        }

        if (uiState.showAddPointsDialog) {
            AddPointsDialog(
                uiState = uiState,
                onEvent = onEvent,
                onDismiss = { onEvent(EditUserScreenUiEvent.HideAddPointsDialog) }
            )
        }
    }
}

@Composable
private fun UserDetailCard(
    user: UserDomain,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = RevibesTheme.colors.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(RevibesTheme.colors.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = user.name.take(2).uppercase(),
                        style = RevibesTheme.typography.h2,
                        color = RevibesTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = user.name,
                        style = RevibesTheme.typography.h2,
                        color = RevibesTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = getRoleDisplayName(user.role),
                        style = RevibesTheme.typography.body1,
                        color = RevibesTheme.colors.primary
                    )
                }

                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "${user.points}",
                        style = RevibesTheme.typography.h1,
                        color = RevibesTheme.colors.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = stringResource(R.string.points),
                        style = RevibesTheme.typography.label3,
                        color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                UserDetailItem(
                    label = stringResource(R.string.email_address),
                    value = user.email
                )
                UserDetailItem(
                    label = stringResource(R.string.phone_number),
                    value = user.phone
                )
                user.address?.let { address ->
                    UserDetailItem(
                        label = stringResource(R.string.address),
                        value = address
                    )
                }
                UserDetailItem(
                    label = "Status",
                    value = if (user.isActive) stringResource(R.string.active) else stringResource(R.string.inactive)
                )
                UserDetailItem(
                    label = "Created At",
                    value = formatDate(user.createdAt)
                )
            }
        }
    }
}

@Composable
private fun UserDetailItem(
    label: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = label,
            style = RevibesTheme.typography.label3,
            color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f),
            fontWeight = FontWeight.Medium
        )
        Text(
            text = value,
            style = RevibesTheme.typography.body1,
            color = RevibesTheme.colors.onSurface
        )
    }
}

@Composable
private fun AddPointsDialog(
    uiState: EditUserScreenUiState,
    onEvent: (EditUserScreenUiEvent) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.add_points),
                style = RevibesTheme.typography.h3,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.pointsToAdd,
                    onValueChange = {
                        if (it.text.isDigitsOnly()) {
                            onEvent(EditUserScreenUiEvent.PointsToAddChanged(it))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.points_to_add),
                            style = RevibesTheme.typography.body1
                        )
                    },
                    isError = uiState.pointsError != null,
                    supportingText = uiState.pointsError?.let { error ->
                        {
                            Text(
                                text = error,
                                style = RevibesTheme.typography.label3,
                                color = RevibesTheme.colors.error
                            )
                        }
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RevibesTheme.colors.primary,
                        unfocusedBorderColor = RevibesTheme.colors.onSurface.copy(alpha = 0.2f)
                    ),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                text = stringResource(R.string.add_points),
                onClick = { onEvent(EditUserScreenUiEvent.AddPoints) },
                enabled = !uiState.isLoadingAddPoints,
                loading = uiState.isLoadingAddPoints
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.cancel),
                    style = RevibesTheme.typography.button,
                    color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    )
}

@Composable
private fun getRoleDisplayName(role: UserDomain.UserRole): String {
    return when (role) {
        UserDomain.UserRole.ADMIN -> stringResource(R.string.role_admin)
        UserDomain.UserRole.USER -> stringResource(R.string.role_user)
    }
}

private fun formatDate(dateString: String): String {
    return try {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val outputFormat = SimpleDateFormat("dd MMMM yyyy, HH:mm", Locale.getDefault())
        val date = inputFormat.parse(dateString) ?: Date()
        outputFormat.format(date)
    } catch (e: Exception) {
        dateString
    }
}

@Preview
@Composable
private fun EditUserScreenPreview() {
    RevibesTheme {
        EditUserScreenContent(
            uiState = EditUserScreenUiState(
                user = UserDomain.dummy()
            ),
            onEvent = {},
            onNavigateBack = {},
            modifier = Modifier.background(RevibesTheme.colors.background)
        )
    }
}
