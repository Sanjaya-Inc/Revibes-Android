/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.ramcosta.composedestinations.annotation.Destination
import org.koin.androidx.compose.koinViewModel
import org.orbitmvi.orbit.compose.collectAsState

@Destination<${NAV_GRAPH}>()
@Composable
fun ${SCREEN_NAME}Screen(
    modifier: Modifier = Modifier,
    viewModel: ${SCREEN_NAME}ScreenViewModel = koinViewModel()
) {
    val state = viewModel.collectAsState().value
    ${SCREEN_NAME}ScreenContent(uiState = state, modifier = modifier)
}

@Composable
private fun ${SCREEN_NAME}ScreenContent(
    uiState: ${SCREEN_NAME}ScreenUiState,
    modifier: Modifier = Modifier
) {
    TODO()
}

@Composable
@Preview
private fun ${SCREEN_NAME}ScreenPreview() {
    RevibesTheme {
        ${SCREEN_NAME}ScreenContent(
            modifier = Modifier.background(Color.White),
            uiState = ${SCREEN_NAME}ScreenUiState()
        )
    }
}
