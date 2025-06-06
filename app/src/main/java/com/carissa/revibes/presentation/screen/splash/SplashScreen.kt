package com.carissa.revibes.presentation.screen.splash

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import coil3.compose.AsyncImage
import com.carissa.revibes.R
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.presentation.navigation.RevibesNavGraph
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.generated.app.navgraphs.RevibesGraph
import com.ramcosta.composedestinations.generated.onboarding.destinations.OnboardingScreenDestination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState
import org.orbitmvi.orbit.compose.collectSideEffect

@Destination<RevibesNavGraph>(start = true)
@Composable
fun SplashScreen(
    modifier: Modifier = Modifier,
    viewModel: SplashScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    val navigator = RevibesTheme.navigator
    viewModel.collectSideEffect {
        when (it) {
            SplashScreenUiEvent.NavigateToOnboarding -> {
                navigator.navigate(OnboardingScreenDestination) {
                    popUpTo(RevibesGraph.startRoute) {
                        inclusive = true
                    }
                }
            }
        }
    }
    SplashScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun SplashScreenContent(
    uiState: SplashScreenUiState,
    modifier: Modifier = Modifier
) {
    Scaffold(
        containerColor = Color.Transparent,
        modifier = modifier
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                R.drawable.main_logo,
                contentDescription = "Main Logo",
            )
        }
    }
}

@Composable
@Preview
private fun SplashScreenPreview() {
    RevibesTheme {
        SplashScreenContent(SplashScreenUiState())
    }
}
