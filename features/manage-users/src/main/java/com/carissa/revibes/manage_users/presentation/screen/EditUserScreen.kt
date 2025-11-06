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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.text.isDigitsOnly
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.RevibesTheme.navigator
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.exchange_points.domain.model.UserVoucher
import com.carissa.revibes.exchange_points.presentation.component.UserVoucherItem
import com.carissa.revibes.manage_users.R
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.component.VerificationStatusCard
import com.carissa.revibes.manage_users.presentation.navigation.ManageUsersGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
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

            is EditUserScreenUiEvent.OnDeductPointsFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is EditUserScreenUiEvent.OnEditUserFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is EditUserScreenUiEvent.OnLoadUserFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is EditUserScreenUiEvent.OnLoadVouchersFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is EditUserScreenUiEvent.OnRedeemVoucherFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            is EditUserScreenUiEvent.ShowToast -> {
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
            Toast.makeText(context, "Operation completed successfully", Toast.LENGTH_SHORT).show()
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
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    androidx.compose.material3.Text(
                        text = stringResource(R.string.edit_user),
                        style = RevibesTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
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
        ContentStateSwitcher(
            uiState.isLoading,
            error = uiState.error,
            actionButton = "Refresh" to {
                onEvent.invoke(EditUserScreenUiEvent.LoadUserDetail)
            }
        ) {
            if (uiState.user != null) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    UserDetailCard(user = uiState.user)
                    VerificationStatusCard(uiState.user, isLoading = uiState.isLoadingVerification) {
                        onEvent(EditUserScreenUiEvent.ToggledVerification)
                    }

                    Button(
                        text = stringResource(R.string.edit_user),
                        onClick = { onEvent(EditUserScreenUiEvent.ShowEditUserDialog) },
                        modifier = Modifier.fillMaxWidth(),
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            text = stringResource(R.string.add_points),
                            onClick = { onEvent(EditUserScreenUiEvent.ShowAddPointsDialog) },
                            modifier = Modifier.weight(1f),
                        )

                        Button(
                            text = "Deduct Points",
                            onClick = { onEvent(EditUserScreenUiEvent.ShowDeductPointsDialog) },
                            modifier = Modifier.weight(1f),
                        )
                    }

                    UserVouchersSection(
                        vouchers = uiState.userVouchers,
                        isLoading = uiState.isLoadingVouchers,
                        error = uiState.vouchersError,
                        onRetry = { onEvent(EditUserScreenUiEvent.LoadUserVouchers) },
                        onVoucherSwipe = { voucher ->
                            onEvent(EditUserScreenUiEvent.ShowRedeemConfirmDialog(voucher))
                        }
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

        if (uiState.showDeductPointsDialog) {
            DeductPointsDialog(
                uiState = uiState,
                onEvent = onEvent,
                onDismiss = { onEvent(EditUserScreenUiEvent.HideDeductPointsDialog) }
            )
        }

        if (uiState.showEditUserDialog) {
            EditUserDialog(
                uiState = uiState,
                onEvent = onEvent,
                onDismiss = { onEvent(EditUserScreenUiEvent.HideEditUserDialog) }
            )
        }

        if (uiState.showRedeemConfirmDialog) {
            RedeemVoucherConfirmDialog(
                voucher = uiState.voucherToRedeem,
                isLoading = uiState.isRedeemingVoucher,
                onConfirm = { onEvent(EditUserScreenUiEvent.RedeemVoucher) },
                onDismiss = { onEvent(EditUserScreenUiEvent.HideRedeemConfirmDialog) }
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
                    value = user.phone.ifBlank { "-" }
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

@Composable
private fun DeductPointsDialog(
    uiState: EditUserScreenUiState,
    onEvent: (EditUserScreenUiEvent) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Deduct Points",
                style = RevibesTheme.typography.h3,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "Current points: ${uiState.user?.points ?: 0}",
                    style = RevibesTheme.typography.body1,
                    color = RevibesTheme.colors.onSurface.copy(alpha = 0.7f)
                )

                OutlinedTextField(
                    value = uiState.pointsToDeduct,
                    onValueChange = {
                        if (it.text.isDigitsOnly()) {
                            onEvent(EditUserScreenUiEvent.PointsToDeductChanged(it))
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Points to deduct",
                            style = RevibesTheme.typography.body1
                        )
                    },
                    isError = uiState.deductPointsError != null,
                    supportingText = uiState.deductPointsError?.let { error ->
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
                text = "Deduct Points",
                onClick = { onEvent(EditUserScreenUiEvent.DeductPoints) },
                enabled = !uiState.isLoadingDeductPoints,
                loading = uiState.isLoadingDeductPoints
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
private fun EditUserDialog(
    uiState: EditUserScreenUiState,
    onEvent: (EditUserScreenUiEvent) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = stringResource(R.string.edit_user),
                style = RevibesTheme.typography.h3,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                OutlinedTextField(
                    value = uiState.editUserName,
                    onValueChange = { onEvent(EditUserScreenUiEvent.EditUserNameChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = "Name",
                            style = RevibesTheme.typography.body1
                        )
                    },
                    isError = uiState.editUserNameError != null,
                    supportingText = uiState.editUserNameError?.let { error ->
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
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.editUserEmail,
                    onValueChange = { onEvent(EditUserScreenUiEvent.EditUserEmailChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.email_address),
                            style = RevibesTheme.typography.body1
                        )
                    },
                    isError = uiState.editUserEmailError != null,
                    supportingText = uiState.editUserEmailError?.let { error ->
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
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email),
                    singleLine = true
                )

                OutlinedTextField(
                    value = uiState.editUserPhone,
                    onValueChange = { onEvent(EditUserScreenUiEvent.EditUserPhoneChanged(it)) },
                    modifier = Modifier.fillMaxWidth(),
                    label = {
                        Text(
                            text = stringResource(R.string.phone_number),
                            style = RevibesTheme.typography.body1
                        )
                    },
                    isError = uiState.editUserPhoneError != null,
                    supportingText = uiState.editUserPhoneError?.let { error ->
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
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Phone),
                    singleLine = true
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Role",
                        style = RevibesTheme.typography.body1,
                        color = RevibesTheme.colors.onSurface.copy(alpha = 0.7f)
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = uiState.editUserRole == UserDomain.UserRole.USER,
                                onClick = {
                                    onEvent(
                                        EditUserScreenUiEvent.EditUserRoleChanged(UserDomain.UserRole.USER)
                                    )
                                }
                            )
                            Text(
                                text = stringResource(R.string.role_user),
                                style = RevibesTheme.typography.body1
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            RadioButton(
                                selected = uiState.editUserRole == UserDomain.UserRole.ADMIN,
                                onClick = {
                                    onEvent(
                                        EditUserScreenUiEvent.EditUserRoleChanged(UserDomain.UserRole.ADMIN)
                                    )
                                }
                            )
                            Text(
                                text = stringResource(R.string.role_admin),
                                style = RevibesTheme.typography.body1
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                text = "Update User",
                onClick = { onEvent(EditUserScreenUiEvent.EditUser) },
                enabled = !uiState.isLoadingEditUser,
                loading = uiState.isLoadingEditUser
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
private fun UserVouchersSection(
    vouchers: ImmutableList<UserVoucher>,
    isLoading: Boolean,
    error: String?,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
    onVoucherSwipe: (UserVoucher) -> Unit = {},
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
            Text(
                text = "User Vouchers",
                style = RevibesTheme.typography.h3,
                color = RevibesTheme.colors.onSurface,
                fontWeight = FontWeight.Bold
            )

            when {
                isLoading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            color = RevibesTheme.colors.primary
                        )
                    }
                }

                error != null -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            text = error,
                            style = RevibesTheme.typography.body1,
                            color = RevibesTheme.colors.error
                        )
                        Button(
                            text = "Retry",
                            onClick = onRetry
                        )
                    }
                }

                vouchers.isEmpty() -> {
                    Text(
                        text = "No vouchers found",
                        style = RevibesTheme.typography.body1,
                        color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .size(300.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(vouchers, key = { it.id }) { voucher ->
                            SwipeableVoucherItem(
                                voucher = voucher,
                                onSwipeToRedeem = { onVoucherSwipe(voucher) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeableVoucherItem(
    voucher: UserVoucher,
    onSwipeToRedeem: () -> Unit,
    modifier: Modifier = Modifier
) {
    val swipeState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onSwipeToRedeem()
                false
            } else {
                false
            }
        }
    )

    SwipeToDismissBox(
        state = swipeState,
        modifier = modifier,
        backgroundContent = {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .fillMaxSize()
                    .background(RevibesTheme.colors.primary)
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        painter = painterResource(com.carissa.revibes.core.R.drawable.back_cta),
                        contentDescription = "Redeem",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                    Text(
                        text = "Redeem",
                        style = RevibesTheme.typography.button,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true
    ) {
        UserVoucherItem(
            userVoucher = voucher,
            onClick = {}
        )
    }
}

@Composable
private fun RedeemVoucherConfirmDialog(
    voucher: UserVoucher?,
    isLoading: Boolean,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    if (voucher == null) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Redeem Voucher",
                style = RevibesTheme.typography.h3,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Are you sure you want to redeem this voucher?",
                    style = RevibesTheme.typography.body1
                )
                Text(
                    text = "Voucher: ${voucher.name}",
                    style = RevibesTheme.typography.body1,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = "Code: ${voucher.code}",
                    style = RevibesTheme.typography.body1,
                    color = RevibesTheme.colors.primary
                )
            }
        },
        confirmButton = {
            Button(
                text = "Redeem",
                onClick = onConfirm,
                enabled = !isLoading,
                loading = isLoading
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancel",
                    style = RevibesTheme.typography.button,
                    color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    )
}

@Preview
@Composable
private fun EditUserScreenPreview() {
    RevibesTheme {
        EditUserScreenContent(
            uiState = EditUserScreenUiState(
                user = UserDomain.dummy(),
                userVouchers = persistentListOf(
                    UserVoucher(
                        id = "1",
                        voucherId = "voucher1",
                        status = "available",
                        claimedAt = null,
                        expiredAt = null,
                        createdAt = "2025-10-05T13:07:29.226Z",
                        updatedAt = "2025-10-05T13:07:29.226Z",
                        name = "Economical Grocery Package",
                        description = "Affordable Grocery Sembako Package",
                        imageUri = "",
                        code = "RVB-ESP-65481",
                        guides = emptyList(),
                        termConditions = emptyList()
                    )
                )
            ),
            onEvent = {},
            onNavigateBack = {},
            modifier = Modifier.background(RevibesTheme.colors.background)
        )
    }
}
