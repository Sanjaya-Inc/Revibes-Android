package com.carissa.revibes.home_admin.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.ButtonVariant
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.core.presentation.compose.components.textfield.OutlinedTextField
import com.carissa.revibes.core.presentation.compose.components.textfield.OutlinedTextFieldDefaults
import com.carissa.revibes.home_admin.data.model.StoreData
import com.carissa.revibes.home_admin.presentation.navigation.HomeAdminGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<HomeAdminGraph>
@Composable
fun AddDropOffPointScreen(
    modifier: Modifier = Modifier,
    store: StoreData = StoreData(),
    viewModel: AddDropOffPointScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val navigator = RevibesTheme.navigator

    LaunchedEffect(store.id) {
        if (store.id.isNotBlank()) {
            viewModel.onEvent(AddDropOffPointScreenUiEvent.PrefillStore(store))
        }
    }

    LaunchedEffect(state.successMessage) {
        val message = state.successMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.onEvent(AddDropOffPointScreenUiEvent.ClearMessage)
        navigator.navigateUp()
    }

    LaunchedEffect(state.errorMessage) {
        val message = state.errorMessage ?: return@LaunchedEffect
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        viewModel.onEvent(AddDropOffPointScreenUiEvent.ClearMessage)
    }

    AddDropOffPointScreenContent(
        uiState = state,
        onBackClick = { navigator.navigateUp() },
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AddDropOffPointScreenContent(
    uiState: AddDropOffPointScreenUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<AddDropOffPointScreenUiEvent> = EventReceiver { }
) {
    val context = LocalContext.current
    val fieldColors = OutlinedTextFieldDefaults.colors().copy(
        focusedContainerColor = Color.White,
        unfocusedContainerColor = Color.White,
        focusedOutlineColor = RevibesTheme.colors.primary,
        unfocusedOutlineColor = RevibesTheme.colors.outline,
        focusedTextColor = RevibesTheme.colors.onSurface,
        unfocusedTextColor = RevibesTheme.colors.onSurface
    )

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (uiState.isEditing) {
                            "Edit Drop-off Point"
                        } else {
                            "Add Drop-off Point"
                        },
                        style = RevibesTheme.typography.h2,
                        fontWeight = FontWeight.Bold,
                        color = RevibesTheme.colors.primary,
                        modifier = Modifier.padding(start = 8.dp)
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.name,
                onValueChange = { eventReceiver.onEvent(AddDropOffPointScreenUiEvent.NameChanged(it)) },
                label = { Text("Location name") },
                placeholder = { Text("Revibes BSD") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )
            OutlinedTextField(
                value = uiState.country,
                onValueChange = { eventReceiver.onEvent(AddDropOffPointScreenUiEvent.CountryChanged(it)) },
                label = { Text("Country code") },
                placeholder = { Text("ID") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )
            OutlinedTextField(
                value = uiState.address,
                onValueChange = { eventReceiver.onEvent(AddDropOffPointScreenUiEvent.AddressChanged(it)) },
                label = { Text("Address") },
                placeholder = { Text("Street, city") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 2,
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )
            OutlinedTextField(
                value = uiState.postalCode,
                onValueChange = { eventReceiver.onEvent(AddDropOffPointScreenUiEvent.PostalCodeChanged(it)) },
                label = { Text("Postal code") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                shape = RoundedCornerShape(12.dp),
                colors = fieldColors
            )

            Button(
                text = "Find on Google Maps",
                onClick = { eventReceiver.onEvent(AddDropOffPointScreenUiEvent.ResolveAddress(context)) },
                loading = uiState.isResolvingAddress,
                enabled = !uiState.isResolvingAddress && !uiState.isLoading,
                variant = ButtonVariant.SecondaryOutlined,
                modifier = Modifier.fillMaxWidth()
            )

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = RevibesTheme.colors.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = RevibesTheme.colors.primary
                        )
                        Text(
                            text = "Google Maps point",
                            style = RevibesTheme.typography.h3,
                            color = RevibesTheme.colors.primary,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    OutlinedTextField(
                        value = uiState.mapsInput,
                        onValueChange = {
                            eventReceiver.onEvent(AddDropOffPointScreenUiEvent.MapsInputChanged(it))
                        },
                        label = { Text("Paste Maps URL or share") },
                        placeholder = { Text("Paste a Google Maps share or URL") },
                        modifier = Modifier.fillMaxWidth(),
                        minLines = 2,
                        shape = RoundedCornerShape(12.dp),
                        colors = fieldColors
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedTextField(
                            value = uiState.latitude,
                            onValueChange = {
                                eventReceiver.onEvent(AddDropOffPointScreenUiEvent.LatitudeChanged(it))
                            },
                            label = { Text("Latitude") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors
                        )
                        OutlinedTextField(
                            value = uiState.longitude,
                            onValueChange = {
                                eventReceiver.onEvent(AddDropOffPointScreenUiEvent.LongitudeChanged(it))
                            },
                            label = { Text("Longitude") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors
                        )
                    }

                    val point = uiState.parsedPoint
                    if (point != null) {
                        Text(
                            text = "${point.first}, ${point.second}",
                            style = RevibesTheme.typography.body2,
                            color = RevibesTheme.colors.onSurface
                        )
                        Button(
                            text = "Open in Google Maps",
                            onClick = { eventReceiver.onEvent(AddDropOffPointScreenUiEvent.OpenInGoogleMaps) },
                            variant = ButtonVariant.PrimaryOutlined,
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = "Find from the address, paste a Maps share or URL, or enter coordinates.",
                            style = RevibesTheme.typography.body2,
                            color = RevibesTheme.colors.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Button(
                text = if (uiState.isEditing) "Save Changes" else "Add Drop-off Point",
                onClick = { eventReceiver.onEvent(AddDropOffPointScreenUiEvent.Submit) },
                loading = uiState.isLoading,
                enabled = !uiState.isLoading && !uiState.isResolvingAddress,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@Preview
private fun AddDropOffPointScreenPreview() {
    RevibesTheme {
        AddDropOffPointScreenContent(
            uiState = AddDropOffPointScreenUiState(),
            onBackClick = {}
        )
    }
}
