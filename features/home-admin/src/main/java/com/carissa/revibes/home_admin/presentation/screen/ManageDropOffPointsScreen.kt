package com.carissa.revibes.home_admin.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.ButtonVariant
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.core.presentation.compose.components.RevibesEmptyState
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.home_admin.data.model.StoreData
import com.carissa.revibes.home_admin.data.model.StorePositionData
import com.carissa.revibes.home_admin.presentation.navigation.HomeAdminGraph
import com.ramcosta.composedestinations.annotation.Destination
import kotlinx.collections.immutable.persistentListOf
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<HomeAdminGraph>
@Composable
fun ManageDropOffPointsScreen(
    modifier: Modifier = Modifier,
    viewModel: ManageDropOffPointsScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val navigator = RevibesTheme.navigator
    val lifecycleOwner = LocalLifecycleOwner.current

    DisposableEffect(lifecycleOwner) {
        val observer = androidx.lifecycle.LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.onEvent(ManageDropOffPointsScreenUiEvent.LoadStores)
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    ManageDropOffPointsScreenContent(
        uiState = state,
        onBackClick = { navigator.navigateUp() },
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageDropOffPointsScreenContent(
    uiState: ManageDropOffPointsScreenUiState,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ManageDropOffPointsScreenUiEvent> = EventReceiver { }
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Drop-off Points",
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
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { eventReceiver.onEvent(ManageDropOffPointsScreenUiEvent.NavigateToAddDropOffPoint) },
                containerColor = RevibesTheme.colors.primary,
                contentColor = RevibesTheme.colors.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add drop-off point"
                )
            }
        }
    ) { paddingValues ->
        ContentStateSwitcher(
            isLoading = uiState.isLoading,
            error = uiState.error,
            actionButton = "Retry" to {
                eventReceiver.onEvent(ManageDropOffPointsScreenUiEvent.LoadStores)
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (uiState.stores.isEmpty()) {
                RevibesEmptyState(
                    title = "No drop-off points",
                    message = "Add a store location with a Google Maps pin.",
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(uiState.stores, key = { it.id }) { store ->
                        DropOffPointItem(
                            store = store,
                            onEdit = {
                                eventReceiver.onEvent(
                                    ManageDropOffPointsScreenUiEvent.NavigateToEditDropOffPoint(
                                        store
                                    )
                                )
                            },
                            onOpenMaps = { latitude, longitude ->
                                eventReceiver.onEvent(
                                    ManageDropOffPointsScreenUiEvent.OpenInGoogleMaps(
                                        latitude,
                                        longitude
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

@Composable
private fun DropOffPointItem(
    store: StoreData,
    onEdit: () -> Unit,
    onOpenMaps: (Double, Double) -> Unit,
    modifier: Modifier = Modifier
) {
    val position = store.position
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = RevibesTheme.colors.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = store.name,
                style = RevibesTheme.typography.h3,
                color = RevibesTheme.colors.primary,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = store.address,
                style = RevibesTheme.typography.body2,
                color = RevibesTheme.colors.onSurface
            )
            Text(
                text = listOf(store.postalCode, store.country, store.status)
                    .filter { it.isNotBlank() }
                    .joinToString(" • "),
                style = RevibesTheme.typography.label3,
                color = RevibesTheme.colors.onSurface.copy(alpha = 0.6f)
            )
            if (position != null) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = RevibesTheme.colors.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "${position.latitude}, ${position.longitude}",
                        style = RevibesTheme.typography.body2,
                        color = RevibesTheme.colors.primary,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    text = "Edit",
                    onClick = onEdit,
                    variant = ButtonVariant.SecondaryOutlined,
                    modifier = Modifier.weight(1f)
                )
                if (position != null) {
                    Button(
                        text = "Open Maps",
                        onClick = { onOpenMaps(position.latitude, position.longitude) },
                        variant = ButtonVariant.PrimaryOutlined,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ManageDropOffPointsScreenPreview() {
    RevibesTheme {
        ManageDropOffPointsScreenContent(
            uiState = ManageDropOffPointsScreenUiState(
                stores = persistentListOf(
                    StoreData(
                        id = "1",
                        name = "Revibes BSD",
                        country = "ID",
                        address = "Green Office Park",
                        postalCode = "15345",
                        position = StorePositionData(-6.3015, 106.6527),
                        status = "active"
                    )
                )
            ),
            onBackClick = {}
        )
    }
}
