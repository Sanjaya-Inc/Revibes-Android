package com.carissa.revibes.point.presentation.screen

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.carissa.revibes.core.R
import com.carissa.revibes.core.presentation.EventReceiver
import com.carissa.revibes.core.presentation.compose.PointModalBg
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.Button
import com.carissa.revibes.core.presentation.compose.components.CommonHeader
import com.carissa.revibes.core.presentation.compose.components.ContentStateSwitcher
import com.carissa.revibes.core.presentation.compose.components.SearchConfig
import com.carissa.revibes.core.presentation.compose.components.Text
import com.carissa.revibes.point.presentation.navigation.PointGraph
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<PointGraph>
@Composable
fun DailyCheckInNewsScreen(
    modifier: Modifier = Modifier,
    viewModel: DailyCheckInNewsScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val context = LocalContext.current
    val navigator = RevibesTheme.navigator

    viewModel.collectSideEffect { event ->
        when (event) {
            is DailyCheckInNewsScreenUiEvent.NavigateBack -> navigator.navigateUp()
            else -> Unit
        }
    }

    LaunchedEffect(state.errorMessage) {
        if (state.errorMessage != null) {
            Toast.makeText(context, state.errorMessage, Toast.LENGTH_SHORT).show()
            viewModel.onEvent(DailyCheckInNewsScreenUiEvent.ClearError)
        }
    }

    DailyCheckInNewsScreenContent(
        uiState = state,
        modifier = modifier,
        eventReceiver = viewModel
    )
}

@Composable
private fun DailyCheckInNewsScreenContent(
    uiState: DailyCheckInNewsScreenUiState,
    modifier: Modifier = Modifier,
    eventReceiver: EventReceiver<DailyCheckInNewsScreenUiEvent> = EventReceiver { }
) {
    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            CommonHeader(
                title = "DAILY CHECK-IN NEWS",
                backgroundDrawRes = R.drawable.bg_point,
                searchConfig = SearchConfig.None,
            )
        }
    ) { contentPadding ->
        ContentStateSwitcher(
            isLoading = uiState.isLoading,
            error = uiState.error,
            actionButton = "Retry" to { eventReceiver.onEvent(DailyCheckInNewsScreenUiEvent.LoadDailyNews) }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(vertical = 12.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.main_logo),
                            contentDescription = "Revibes Logo",
                            modifier = Modifier.height(32.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(
                                width = 4.dp,
                                color = RevibesTheme.colors.primary,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .background(
                                color = PointModalBg,
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(24.dp)
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = uiState.title,
                                style = RevibesTheme.typography.h2,
                                fontWeight = FontWeight.Bold,
                                color = RevibesTheme.colors.primary,
                                textAlign = TextAlign.Center
                            )

                            Text(
                                text = uiState.content,
                                style = RevibesTheme.typography.body1,
                                color = RevibesTheme.colors.onSurface,
                                textAlign = TextAlign.Center,
                                lineHeight = 22.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.girl_character),
                        contentDescription = "Revibes Mascot",
                        modifier = Modifier
                            .height(160.dp)
                            .padding(horizontal = 16.dp),
                        contentScale = ContentScale.Fit
                    )
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 8.dp)
                ) {
                    val buttonText = when {
                        uiState.isRewardClaimed -> "CHECKED IN"
                        !uiState.isTimerFinished -> "CLAIM COIN (${uiState.timerSeconds}s)"
                        else -> "CLAIM COIN"
                    }

                    Button(
                        text = buttonText,
                        onClick = { eventReceiver.onEvent(DailyCheckInNewsScreenUiEvent.ClaimReward) },
                        enabled = uiState.isTimerFinished && !uiState.isClaimingReward && !uiState.isRewardClaimed,
                        loading = uiState.isClaimingReward,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
@Preview
private fun DailyCheckInNewsScreenPreview() {
    RevibesTheme {
        DailyCheckInNewsScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = DailyCheckInNewsScreenUiState(
                isLoading = false,
                title = "Eskalasi Konflik AS-Iran",
                content = "Eskalasi Konflik AS-Iran membuka risiko volatilitas minyak lanjutan. Harga Minyak kembali Naik +9,6% ke ~US$83/barrel",
                timerSeconds = 3,
                isTimerFinished = false
            )
        )
    }
}
