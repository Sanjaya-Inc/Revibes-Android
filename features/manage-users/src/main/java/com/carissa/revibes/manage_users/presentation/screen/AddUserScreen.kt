package com.carissa.revibes.manage_users.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.auth.AuthTextField
import com.carissa.revibes.core.presentation.components.auth.EmailField
import com.carissa.revibes.core.presentation.components.auth.PasswordField
import com.carissa.revibes.core.presentation.components.auth.PhoneField
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import com.carissa.revibes.core.presentation.components.components.Text
import com.carissa.revibes.manage_users.R
import com.carissa.revibes.manage_users.domain.model.UserDomain
import com.carissa.revibes.manage_users.presentation.navigation.ManageUsersGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<ManageUsersGraph>
@Composable
fun AddUserScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: AddUserScreenViewModel = koinViewModel()
) {
    val uiState by viewModel.collectAsState()
    val context = LocalContext.current

    viewModel.collectSideEffect { event ->
        when (event) {
            is AddUserScreenUiEvent.OnCreateUserFailed -> {
                Toast.makeText(context, event.message, Toast.LENGTH_SHORT).show()
            }

            else -> Unit
        }
    }

    LaunchedEffect(uiState.isSuccess) {
        if (uiState.isSuccess) {
            Toast.makeText(context, "Successfully created user", Toast.LENGTH_SHORT).show()
            navigator.navigateUp()
        }
    }

    AddUserScreenContent(
        uiState = uiState,
        onEvent = viewModel::onEvent,
        onNavigateBack = { navigator.navigateUp() },
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddUserScreenContent(
    uiState: AddUserScreenUiState,
    onEvent: (AddUserScreenUiEvent) -> Unit,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isRoleDropdownExpanded by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = RevibesTheme.colors.background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.add_new_user),
                        style = RevibesTheme.typography.h2,
                        color = RevibesTheme.colors.onSurface,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            AuthTextField(
                value = uiState.name,
                onValueChange = { onEvent(AddUserScreenUiEvent.NameChanged(it)) },
                label = stringResource(R.string.full_name),
                isEnabled = !uiState.isLoading,
                errorText = uiState.nameError
            )

            EmailField(
                value = uiState.email,
                onValueChange = { onEvent(AddUserScreenUiEvent.EmailChanged(it)) },
                label = stringResource(R.string.email_address),
                isEnabled = !uiState.isLoading,
                errorText = uiState.emailError
            )

            PhoneField(
                value = uiState.phone,
                onValueChange = { onEvent(AddUserScreenUiEvent.PhoneChanged(it)) },
                label = stringResource(R.string.phone_number),
                isEnabled = !uiState.isLoading,
                errorText = uiState.phoneError
            )

            ExposedDropdownMenuBox(
                expanded = isRoleDropdownExpanded,
                onExpandedChange = { isRoleDropdownExpanded = it }
            ) {
                OutlinedTextField(
                    value = getRoleDisplayName(uiState.selectedRole),
                    onValueChange = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
                    readOnly = true,
                    label = {
                        Text(
                            text = stringResource(R.string.user_role),
                            style = RevibesTheme.typography.body1
                        )
                    },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = isRoleDropdownExpanded)
                    },
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = RevibesTheme.colors.primary,
                        unfocusedBorderColor = RevibesTheme.colors.onSurface.copy(alpha = 0.2f)
                    )
                )

                ExposedDropdownMenu(
                    expanded = isRoleDropdownExpanded,
                    onDismissRequest = { isRoleDropdownExpanded = false }
                ) {
                    UserDomain.UserRole.values().forEach { role ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = getRoleDisplayName(role),
                                    style = RevibesTheme.typography.body1
                                )
                            },
                            onClick = {
                                onEvent(AddUserScreenUiEvent.RoleChanged(role))
                                isRoleDropdownExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            PasswordField(
                value = uiState.password,
                onValueChange = { onEvent(AddUserScreenUiEvent.PasswordChanged(it)) },
                label = stringResource(R.string.password),
                imeAction = ImeAction.Next,
                isEnabled = !uiState.isLoading,
                errorText = uiState.passwordError
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    text = stringResource(R.string.cancel),
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f),
                    variant = ButtonVariant.SecondaryOutlined
                )

                Button(
                    text = stringResource(R.string.create_user),
                    onClick = { onEvent(AddUserScreenUiEvent.CreateUser) },
                    modifier = Modifier.weight(1f),
                    enabled = uiState.isButtonEnabled,
                    loading = uiState.isLoading
                )
            }
        }
    }
}

@Composable
private fun getRoleDisplayName(role: UserDomain.UserRole): String {
    return when (role) {
        UserDomain.UserRole.ADMIN -> stringResource(R.string.role_admin)
        UserDomain.UserRole.USER -> stringResource(R.string.role_user)
    }
}

@Preview
@Composable
private fun AddUserScreenPreview() {
    RevibesTheme {
        AddUserScreenContent(
            uiState = AddUserScreenUiState(),
            onEvent = {},
            onNavigateBack = {},
            modifier = Modifier.background(RevibesTheme.colors.background)
        )
    }
}
