package com.carissa.revibes.manage_voucher.presentation.screen

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import com.carissa.revibes.manage_voucher.presentation.navigation.ManageVoucherGraph
import com.carissa.revibes.manage_voucher.presentation.screen.components.DatePicker
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import kotlinx.collections.immutable.ImmutableList
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ManageVoucherGraph>
@Composable
fun EditVoucherScreen(
    voucher: VoucherDomain,
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: EditVoucherScreenViewModel = koinViewModel()
) {
    val state by viewModel.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(voucher) {
        viewModel.onEvent(EditVoucherScreenUiEvent.Initialize(voucher))
    }

    viewModel.collectSideEffect {
        when (it) {
            is EditVoucherScreenUiEvent.OnUpdateVoucherFailed -> {
                Toast.makeText(
                    context,
                    "Failed to update voucher: ${it.message}",
                    Toast.LENGTH_LONG
                ).show()
            }

            is EditVoucherScreenUiEvent.OnVoucherUpdatedSuccessfully -> {
                Toast.makeText(
                    context,
                    "Voucher updated successfully",
                    Toast.LENGTH_SHORT
                ).show()
                navigator.navigateUp()
            }

            else -> Unit
        }
    }

    EditVoucherContent(
        state = state,
        onBackClick = { navigator.navigateUp() },
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditVoucherContent(
    state: EditVoucherScreenUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<EditVoucherScreenUiEvent> = EventReceiver {}
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Edit Voucher",
                        style = RevibesTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(start = 16.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
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
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                if (state.isInClaimPeriod) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = RevibesTheme.colors.error.copy(alpha = 0.1f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = null,
                                tint = RevibesTheme.colors.error
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "This voucher is in claim period. Only name and description can be edited.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = RevibesTheme.colors.error
                            )
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = RevibesTheme.colors.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Basic Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RevibesTheme.colors.primary
                        )

                        OutlinedTextField(
                            value = state.code,
                            onValueChange = {
                                eventReceiver.onEvent(EditVoucherScreenUiEvent.CodeChanged(it))
                            },
                            label = { Text("Voucher Code") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = state.codeError != null,
                            supportingText = state.codeError?.let { { Text(it) } },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            enabled = !state.isInClaimPeriod,
                            trailingIcon = if (state.isInClaimPeriod) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = "Locked",
                                        tint = RevibesTheme.colors.onSurface
                                    )
                                }
                            } else {
                                null
                            }
                        )

                        OutlinedTextField(
                            value = state.name,
                            onValueChange = {
                                eventReceiver.onEvent(EditVoucherScreenUiEvent.NameChanged(it))
                            },
                            label = { Text("Voucher Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = state.nameError != null,
                            supportingText = state.nameError?.let { { Text(it) } },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value = state.description,
                            onValueChange = {
                                eventReceiver.onEvent(EditVoucherScreenUiEvent.DescriptionChanged(it))
                            },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = state.descriptionError != null,
                            supportingText = state.descriptionError?.let { { Text(it) } },
                            minLines = 3,
                            maxLines = 5,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                ImageDisplayCard(imageUrl = state.imageUrl)

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = RevibesTheme.colors.surface
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Discount Configuration",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = RevibesTheme.colors.primary
                        )

                        OutlinedTextField(
                            value = when (state.type) {
                                VoucherDomain.VoucherType.PERCENT_OFF -> "Percentage Off"
                                VoucherDomain.VoucherType.FIXED_AMOUNT -> "Fixed Amount"
                            },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Discount Type") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            enabled = false
                        )

                        OutlinedTextField(
                            value = state.amount,
                            onValueChange = {},
                            label = {
                                Text(
                                    when (state.type) {
                                        VoucherDomain.VoucherType.PERCENT_OFF -> "Percentage (%)"
                                        VoucherDomain.VoucherType.FIXED_AMOUNT -> "Amount"
                                    }
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            enabled = false
                        )
                    }
                }

                EditableConditionsCard(
                    state = state,
                    eventReceiver = eventReceiver
                )

                EditableClaimPeriodCard(
                    state = state,
                    eventReceiver = eventReceiver
                )

                EditableTermConditionsCard(
                    termConditions = state.termConditions,
                    showSection = state.showTermConditionsSection,
                    error = state.termConditionsError,
                    isLocked = state.isInClaimPeriod,
                    onToggleSection = {
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.ToggleTermConditionsSection)
                    },
                    onAddCondition = { condition ->
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.AddTermCondition(condition))
                    },
                    onRemoveCondition = { index ->
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.RemoveTermCondition(index))
                    },
                    onUpdateCondition = { index, condition ->
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.UpdateTermCondition(index, condition))
                    }
                )

                EditableGuidesCard(
                    guides = state.guides,
                    showSection = state.showGuidesSection,
                    error = state.guidesError,
                    isLocked = state.isInClaimPeriod,
                    onToggleSection = {
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.ToggleGuidesSection)
                    },
                    onAddGuide = { guide ->
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.AddGuide(guide))
                    },
                    onRemoveGuide = { index ->
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.RemoveGuide(index))
                    },
                    onUpdateGuide = { index, guide ->
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.UpdateGuide(index, guide))
                    }
                )

                Spacer(modifier = Modifier.height(100.dp))
            }

            Button(
                text = if (state.isLoading) "Updating..." else "Update Voucher",
                onClick = {
                    eventReceiver.onEvent(EditVoucherScreenUiEvent.SaveVoucher)
                },
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(16.dp),
                enabled = !state.isLoading,
                loading = state.isLoading
            )
        }
    }
}

@Composable
private fun ImageDisplayCard(
    imageUrl: String?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = RevibesTheme.colors.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Voucher Image",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RevibesTheme.colors.primary
                )

                Text(
                    text = "Read-only",
                    style = MaterialTheme.typography.bodySmall,
                    color = RevibesTheme.colors.onSurface
                )
            }

            if (!imageUrl.isNullOrBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(RevibesTheme.colors.surface)
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Voucher image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f / 9f)
                        .clip(RoundedCornerShape(12.dp))
                        .border(
                            width = 2.dp,
                            color = RevibesTheme.colors.outline,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .background(RevibesTheme.colors.primary.copy(alpha = 0.1f)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No image available",
                        style = MaterialTheme.typography.bodyMedium,
                        color = RevibesTheme.colors.onSurface
                    )
                }
            }
        }
    }
}

@Composable
private fun EditableConditionsCard(
    state: EditVoucherScreenUiState,
    eventReceiver: EventReceiver<EditVoucherScreenUiEvent>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = RevibesTheme.colors.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Conditions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RevibesTheme.colors.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (state.isInClaimPeriod) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Locked",
                            tint = RevibesTheme.colors.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    TextButton(
                        onClick = {
                            eventReceiver.onEvent(EditVoucherScreenUiEvent.ToggleConditionsSection)
                        }
                    ) {
                        Text(if (state.showConditionsSection) "Hide" else "Show")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (state.showConditionsSection) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = null
                        )
                    }
                }
            }

            AnimatedVisibility(visible = state.showConditionsSection) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = state.maxClaim,
                        onValueChange = {
                            eventReceiver.onEvent(EditVoucherScreenUiEvent.MaxClaimChanged(it))
                        },
                        label = { Text("Max Claims") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.maxClaimError != null,
                        supportingText = state.maxClaimError?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isInClaimPeriod,
                        trailingIcon = if (state.isInClaimPeriod) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Locked",
                                    tint = RevibesTheme.colors.onSurface
                                )
                            }
                        } else {
                            null
                        }
                    )

                    OutlinedTextField(
                        value = state.maxUsage,
                        onValueChange = {
                            eventReceiver.onEvent(EditVoucherScreenUiEvent.MaxUsageChanged(it))
                        },
                        label = { Text("Max Usage per User") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.maxUsageError != null,
                        supportingText = state.maxUsageError?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isInClaimPeriod,
                        trailingIcon = if (state.isInClaimPeriod) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Locked",
                                    tint = RevibesTheme.colors.onSurface
                                )
                            }
                        } else {
                            null
                        }
                    )

                    OutlinedTextField(
                        value = state.minOrderItem,
                        onValueChange = {
                            eventReceiver.onEvent(EditVoucherScreenUiEvent.MinOrderItemChanged(it))
                        },
                        label = { Text("Min Order Item") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.minOrderItemError != null,
                        supportingText = state.minOrderItemError?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isInClaimPeriod,
                        trailingIcon = if (state.isInClaimPeriod) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Locked",
                                    tint = RevibesTheme.colors.onSurface
                                )
                            }
                        } else {
                            null
                        }
                    )

                    OutlinedTextField(
                        value = state.minOrderAmount,
                        onValueChange = {
                            eventReceiver.onEvent(EditVoucherScreenUiEvent.MinOrderAmountChanged(it))
                        },
                        label = { Text("Min Order Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.minOrderAmountError != null,
                        supportingText = state.minOrderAmountError?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isInClaimPeriod,
                        trailingIcon = if (state.isInClaimPeriod) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Locked",
                                    tint = RevibesTheme.colors.onSurface
                                )
                            }
                        } else {
                            null
                        }
                    )

                    OutlinedTextField(
                        value = state.maxDiscountAmount,
                        onValueChange = {
                            eventReceiver.onEvent(EditVoucherScreenUiEvent.MaxDiscountAmountChanged(it))
                        },
                        label = { Text("Max Discount Amount") },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = state.maxDiscountAmountError != null,
                        supportingText = state.maxDiscountAmountError?.let { { Text(it) } },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        enabled = !state.isInClaimPeriod,
                        trailingIcon = if (state.isInClaimPeriod) {
                            {
                                Icon(
                                    imageVector = Icons.Default.Info,
                                    contentDescription = "Locked",
                                    tint = RevibesTheme.colors.onSurface
                                )
                            }
                        } else {
                            null
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun EditableClaimPeriodCard(
    state: EditVoucherScreenUiState,
    eventReceiver: EventReceiver<EditVoucherScreenUiEvent>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = RevibesTheme.colors.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Claim Period",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RevibesTheme.colors.primary
                )

                if (state.isInClaimPeriod) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Locked",
                            tint = RevibesTheme.colors.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Locked",
                            style = MaterialTheme.typography.bodySmall,
                            color = RevibesTheme.colors.onSurface
                        )
                    }
                }
            }

            var showStartDatePicker by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = state.claimPeriodStart,
                onValueChange = {},
                readOnly = true,
                label = { Text("Start Date") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (state.isInClaimPeriod) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Locked",
                            tint = RevibesTheme.colors.onSurface
                        )
                    } else {
                        IconButton(onClick = { showStartDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    }
                },
                isError = state.claimPeriodStartError != null,
                supportingText = state.claimPeriodStartError?.let { { Text(it) } },
                placeholder = { Text("YYYY-MM-DD") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isInClaimPeriod
            )

            if (showStartDatePicker && !state.isInClaimPeriod) {
                DatePicker(
                    onDateSelected = {
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.ClaimPeriodStartChanged(it))
                        showStartDatePicker = false
                    },
                    onDismiss = { showStartDatePicker = false }
                )
            }

            var showEndDatePicker by remember { mutableStateOf(false) }
            OutlinedTextField(
                value = state.claimPeriodEnd,
                onValueChange = {},
                readOnly = true,
                label = { Text("End Date") },
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    if (state.isInClaimPeriod) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Locked",
                            tint = RevibesTheme.colors.onSurface
                        )
                    } else {
                        IconButton(onClick = { showEndDatePicker = true }) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Select date"
                            )
                        }
                    }
                },
                isError = state.claimPeriodEndError != null,
                supportingText = state.claimPeriodEndError?.let { { Text(it) } },
                placeholder = { Text("YYYY-MM-DD") },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isInClaimPeriod
            )

            if (showEndDatePicker && !state.isInClaimPeriod) {
                DatePicker(
                    onDateSelected = {
                        eventReceiver.onEvent(EditVoucherScreenUiEvent.ClaimPeriodEndChanged(it))
                        showEndDatePicker = false
                    },
                    onDismiss = { showEndDatePicker = false }
                )
            }
        }
    }
}

@Composable
private fun EditableTermConditionsCard(
    termConditions: ImmutableList<String>,
    showSection: Boolean,
    error: String?,
    isLocked: Boolean,
    onToggleSection: () -> Unit,
    onAddCondition: (String) -> Unit,
    onRemoveCondition: (Int) -> Unit,
    onUpdateCondition: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newConditionText by remember { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = RevibesTheme.colors.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Terms & Conditions",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RevibesTheme.colors.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLocked) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Locked",
                            tint = RevibesTheme.colors.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    TextButton(
                        onClick = onToggleSection
                    ) {
                        Text(if (showSection) "Hide" else "Show")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (showSection) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = null
                        )
                    }
                }
            }

            AnimatedVisibility(visible = showSection) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    if (!isLocked) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            OutlinedTextField(
                                value = newConditionText,
                                onValueChange = { newConditionText = it },
                                label = { Text("Add new condition") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                minLines = 2,
                                maxLines = 3
                            )

                            IconButton(
                                onClick = {
                                    if (newConditionText.isNotBlank()) {
                                        onAddCondition(newConditionText.trim())
                                        newConditionText = ""
                                    }
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add condition",
                                    tint = RevibesTheme.colors.primary
                                )
                            }
                        }
                    }

                    termConditions.forEachIndexed { index, condition ->
                        var editingCondition by remember { mutableStateOf(condition) }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            OutlinedTextField(
                                value = editingCondition,
                                onValueChange = {
                                    if (!isLocked) {
                                        editingCondition = it
                                        onUpdateCondition(index, it)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                minLines = 2,
                                maxLines = 3,
                                enabled = !isLocked,
                                trailingIcon = if (isLocked) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Locked",
                                            tint = RevibesTheme.colors.onSurface
                                        )
                                    }
                                } else {
                                    null
                                }
                            )

                            if (!isLocked) {
                                IconButton(
                                    onClick = { onRemoveCondition(index) },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove condition",
                                        tint = RevibesTheme.colors.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (!showSection && error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = RevibesTheme.colors.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}

@Composable
private fun EditableGuidesCard(
    guides: ImmutableList<String>,
    showSection: Boolean,
    error: String?,
    isLocked: Boolean,
    onToggleSection: () -> Unit,
    onAddGuide: (String) -> Unit,
    onRemoveGuide: (Int) -> Unit,
    onUpdateGuide: (Int, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var newGuideText by remember { mutableStateOf("") }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(
            containerColor = RevibesTheme.colors.surface
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Usage Guides",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = RevibesTheme.colors.primary
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (isLocked) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Locked",
                            tint = RevibesTheme.colors.onSurface,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    TextButton(
                        onClick = onToggleSection
                    ) {
                        Text(if (showSection) "Hide" else "Show")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (showSection) {
                                Icons.Default.KeyboardArrowUp
                            } else {
                                Icons.Default.KeyboardArrowDown
                            },
                            contentDescription = null
                        )
                    }
                }
            }

            AnimatedVisibility(visible = showSection) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Spacer(modifier = Modifier.height(8.dp))

                    if (!isLocked) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            OutlinedTextField(
                                value = newGuideText,
                                onValueChange = { newGuideText = it },
                                label = { Text("Add new guide") },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                minLines = 2,
                                maxLines = 3
                            )

                            IconButton(
                                onClick = {
                                    if (newGuideText.isNotBlank()) {
                                        onAddGuide(newGuideText.trim())
                                        newGuideText = ""
                                    }
                                },
                                modifier = Modifier.padding(top = 8.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add guide",
                                    tint = RevibesTheme.colors.primary
                                )
                            }
                        }
                    }

                    guides.forEachIndexed { index, guide ->
                        var editingGuide by remember { mutableStateOf(guide) }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.Top
                        ) {
                            OutlinedTextField(
                                value = editingGuide,
                                onValueChange = {
                                    if (!isLocked) {
                                        editingGuide = it
                                        onUpdateGuide(index, it)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                minLines = 2,
                                maxLines = 3,
                                enabled = !isLocked,
                                trailingIcon = if (isLocked) {
                                    {
                                        Icon(
                                            imageVector = Icons.Default.Info,
                                            contentDescription = "Locked",
                                            tint = RevibesTheme.colors.onSurface
                                        )
                                    }
                                } else {
                                    null
                                }
                            )

                            if (!isLocked) {
                                IconButton(
                                    onClick = { onRemoveGuide(index) },
                                    modifier = Modifier.padding(top = 8.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove guide",
                                        tint = RevibesTheme.colors.error
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (!showSection && error != null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = error,
                    color = RevibesTheme.colors.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
