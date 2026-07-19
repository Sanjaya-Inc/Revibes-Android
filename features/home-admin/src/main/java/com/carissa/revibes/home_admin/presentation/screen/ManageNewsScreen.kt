package com.carissa.revibes.home_admin.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.home_admin.presentation.navigation.HomeAdminGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<HomeAdminGraph>
@Composable
fun ManageNewsScreen(
    modifier: Modifier = Modifier,
    viewModel: ManageNewsScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val navigator = RevibesTheme.navigator

    viewModel.collectSideEffect { event ->
        when (event) {
            is ManageNewsScreenUiEvent.NavigateBack -> navigator.navigateUp()
            else -> Unit
        }
    }

    LaunchedEffect(state.successMessage) {
        if (state.successMessage != null) {
            Toast.makeText(context, state.successMessage, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ManageNewsScreenUiEvent.ClearMessage)
        }
    }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage != null) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(ManageNewsScreenUiEvent.ClearMessage)
        }
    }

    ManageNewsScreenContent(
        uiState = state,
        onBackClick = { navigator.navigateUp() },
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ManageNewsScreenContent(
    uiState: ManageNewsScreenUiState,
    onBackClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<ManageNewsScreenUiEvent> = EventReceiver { }
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Manage Daily News",
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
    ) { contentPadding ->
        ContentStateSwitcher(isLoading = uiState.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    if (!uiState.currentNewsTitle.isNullOrBlank()) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = RevibesTheme.colors.surface
                            ),
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Text(
                                    text = "Current Active News",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = RevibesTheme.colors.primary
                                )
                                Text(
                                    text = uiState.currentNewsTitle,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = RevibesTheme.colors.onSurface
                                )
                                Text(
                                    text = uiState.currentNewsContent.orEmpty(),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = RevibesTheme.colors.onSurface.copy(alpha = 0.8f)
                                )
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = RevibesTheme.colors.surface
                        ),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Text(
                                text = "Publish New Check-In News",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = RevibesTheme.colors.primary
                            )

                            OutlinedTextField(
                                value = uiState.titleInput,
                                onValueChange = { eventReceiver.onEvent(ManageNewsScreenUiEvent.OnTitleChange(it)) },
                                label = { Text("News Title / Headline") },
                                placeholder = { Text("e.g. Eskalasi Konflik AS-Iran...") },
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = RevibesTheme.colors.primary,
                                    unfocusedBorderColor = RevibesTheme.colors.outline,
                                    focusedTextColor = RevibesTheme.colors.onSurface,
                                    unfocusedTextColor = RevibesTheme.colors.onSurface,
                                )
                            )

                            OutlinedTextField(
                                value = uiState.contentInput,
                                onValueChange = { eventReceiver.onEvent(ManageNewsScreenUiEvent.OnContentChange(it)) },
                                label = { Text("News Content") },
                                placeholder = { Text("Enter news details to display during user check-in...") },
                                minLines = 4,
                                maxLines = 8,
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedBorderColor = RevibesTheme.colors.primary,
                                    unfocusedBorderColor = RevibesTheme.colors.outline,
                                    focusedTextColor = RevibesTheme.colors.onSurface,
                                    unfocusedTextColor = RevibesTheme.colors.onSurface,
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        text = "PUBLISH DAILY NEWS",
                        onClick = { eventReceiver.onEvent(ManageNewsScreenUiEvent.SubmitNews) },
                        loading = uiState.isSubmitting,
                        enabled = !uiState.isSubmitting,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun ManageNewsScreenPreview() {
    RevibesTheme {
        ManageNewsScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = ManageNewsScreenUiState()
        )
    }
}
