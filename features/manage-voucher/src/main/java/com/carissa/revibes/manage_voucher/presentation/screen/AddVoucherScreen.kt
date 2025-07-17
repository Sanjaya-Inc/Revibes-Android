package com.carissa.revibes.manage_voucher.presentation.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import com.carissa.revibes.core.presentation.components.components.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.carissa.revibes.manage_voucher.domain.model.VoucherDomain
import com.carissa.revibes.manage_voucher.presentation.navigation.ManageVoucherGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Destination<ManageVoucherGraph>
@Composable
fun AddVoucherScreen(
    navigator: DestinationsNavigator,
    modifier: Modifier = Modifier,
    viewModel: AddVoucherScreenViewModel = koinViewModel()
) {
    val state by viewModel.container.stateFlow.collectAsState()
    val scrollState = rememberScrollState()

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Add Voucher",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navigator.navigateUp() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
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
                    .verticalScroll(scrollState)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Basic Information",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = state.code,
                            onValueChange = {
                                viewModel.onEvent(AddVoucherScreenUiEvent.CodeChanged(it))
                            },
                            label = { Text("Voucher Code") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = state.codeError != null,
                            supportingText = state.codeError?.let { { Text(it) } },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = state.name,
                            onValueChange = {
                                viewModel.onEvent(AddVoucherScreenUiEvent.NameChanged(it))
                            },
                            label = { Text("Voucher Name") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = state.nameError != null,
                            supportingText = state.nameError?.let { { Text(it) } },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = state.description,
                            onValueChange = {
                                viewModel.onEvent(AddVoucherScreenUiEvent.DescriptionChanged(it))
                            },
                            label = { Text("Description") },
                            modifier = Modifier.fillMaxWidth(),
                            isError = state.descriptionError != null,
                            supportingText = state.descriptionError?.let { { Text(it) } },
                            minLines = 3,
                            maxLines = 5
                        )
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Discount Configuration",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        var typeExpanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(
                            expanded = typeExpanded,
                            onExpandedChange = { typeExpanded = !typeExpanded }
                        ) {
                            OutlinedTextField(
                                value = when (state.type) {
                                    VoucherDomain.VoucherType.PERCENT_OFF -> "Percentage Off"
                                    VoucherDomain.VoucherType.FIXED_AMOUNT -> "Fixed Amount"
                                },
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Discount Type") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = typeExpanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = typeExpanded,
                                onDismissRequest = { typeExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Percentage Off") },
                                    onClick = {
                                        viewModel.onEvent(
                                            AddVoucherScreenUiEvent.TypeChanged(VoucherDomain.VoucherType.PERCENT_OFF)
                                        )
                                        typeExpanded = false
                                    }
                                )
                                DropdownMenuItem(
                                    text = { Text("Fixed Amount") },
                                    onClick = {
                                        viewModel.onEvent(
                                            AddVoucherScreenUiEvent.TypeChanged(VoucherDomain.VoucherType.FIXED_AMOUNT)
                                        )
                                        typeExpanded = false
                                    }
                                )
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedTextField(
                                value = state.amount,
                                onValueChange = {
                                    viewModel.onEvent(AddVoucherScreenUiEvent.AmountChanged(it))
                                },
                                label = {
                                    Text(
                                        when (state.type) {
                                            VoucherDomain.VoucherType.PERCENT_OFF -> "Percentage (%)"
                                            VoucherDomain.VoucherType.FIXED_AMOUNT -> "Amount"
                                        }
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                isError = state.amountError != null,
                                supportingText = state.amountError?.let { { Text(it) } },
                                singleLine = true
                            )

                            if (state.type == VoucherDomain.VoucherType.FIXED_AMOUNT) {
                                var currencyExpanded by remember { mutableStateOf(false) }
                                ExposedDropdownMenuBox(
                                    expanded = currencyExpanded,
                                    onExpandedChange = { currencyExpanded = !currencyExpanded },
                                    modifier = Modifier.weight(0.6f)
                                ) {
                                    OutlinedTextField(
                                        value = state.currency.name,
                                        onValueChange = {},
                                        readOnly = true,
                                        label = { Text("Currency") },
                                        trailingIcon = {
                                            ExposedDropdownMenuDefaults.TrailingIcon(
                                                expanded = currencyExpanded
                                            )
                                        },
                                        modifier = Modifier.menuAnchor()
                                    )
                                    ExposedDropdownMenu(
                                        expanded = currencyExpanded,
                                        onDismissRequest = { currencyExpanded = false }
                                    ) {
                                        VoucherDomain.Currency.values().forEach { currency ->
                                            DropdownMenuItem(
                                                text = { Text(currency.name) },
                                                onClick = {
                                                    viewModel.onEvent(AddVoucherScreenUiEvent.CurrencyChanged(currency))
                                                    currencyExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Conditions",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )

                            TextButton(
                                onClick = {
                                    viewModel.onEvent(AddVoucherScreenUiEvent.ToggleConditionsSection)
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

                        AnimatedVisibility(visible = state.showConditionsSection) {
                            Column(
                                verticalArrangement = Arrangement.spacedBy(16.dp)
                            ) {
                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    OutlinedTextField(
                                        value = state.maxClaim,
                                        onValueChange = {
                                            viewModel.onEvent(AddVoucherScreenUiEvent.MaxClaimChanged(it))
                                        },
                                        label = { Text("Max Claims") },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        isError = state.maxClaimError != null,
                                        supportingText = state.maxClaimError?.let { { Text(it) } },
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = state.maxUsage,
                                        onValueChange = {
                                            viewModel.onEvent(AddVoucherScreenUiEvent.MaxUsageChanged(it))
                                        },
                                        label = { Text("Max Usage") },
                                        modifier = Modifier.weight(1f),
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                        isError = state.maxUsageError != null,
                                        supportingText = state.maxUsageError?.let { { Text(it) } },
                                        singleLine = true
                                    )
                                }

                                OutlinedTextField(
                                    value = state.minOrderItem,
                                    onValueChange = {
                                        viewModel.onEvent(AddVoucherScreenUiEvent.MinOrderItemChanged(it))
                                    },
                                    label = { Text("Min Order Items") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    isError = state.minOrderItemError != null,
                                    supportingText = state.minOrderItemError?.let { { Text(it) } },
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = state.minOrderAmount,
                                    onValueChange = {
                                        viewModel.onEvent(AddVoucherScreenUiEvent.MinOrderAmountChanged(it))
                                    },
                                    label = { Text("Min Order Amount") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    isError = state.minOrderAmountError != null,
                                    supportingText = state.minOrderAmountError?.let { { Text(it) } },
                                    singleLine = true
                                )

                                OutlinedTextField(
                                    value = state.maxDiscountAmount,
                                    onValueChange = {
                                        viewModel.onEvent(AddVoucherScreenUiEvent.MaxDiscountAmountChanged(it))
                                    },
                                    label = { Text("Max Discount Amount") },
                                    modifier = Modifier.fillMaxWidth(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    isError = state.maxDiscountAmountError != null,
                                    supportingText = state.maxDiscountAmountError?.let { { Text(it) } },
                                    singleLine = true
                                )
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Claim Period",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value = state.claimPeriodStart,
                            onValueChange = {
                                viewModel.onEvent(AddVoucherScreenUiEvent.ClaimPeriodStartChanged(it))
                            },
                            label = { Text("Start Date") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select date"
                                )
                            },
                            isError = state.claimPeriodStartError != null,
                            supportingText = state.claimPeriodStartError?.let { { Text(it) } },
                            placeholder = { Text("YYYY-MM-DD") },
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = state.claimPeriodEnd,
                            onValueChange = {
                                viewModel.onEvent(AddVoucherScreenUiEvent.ClaimPeriodEndChanged(it))
                            },
                            label = { Text("End Date") },
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select date"
                                )
                            },
                            isError = state.claimPeriodEndError != null,
                            supportingText = state.claimPeriodEndError?.let { { Text(it) } },
                            placeholder = { Text("YYYY-MM-DD") },
                            singleLine = true
                        )
                    }
                }

                Spacer(modifier = Modifier.height(80.dp))
            }

            Button(
                text = if (state.isLoading) "Creating..." else "Create Voucher",
                onClick = {
                    viewModel.onEvent(AddVoucherScreenUiEvent.SaveVoucher)
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
