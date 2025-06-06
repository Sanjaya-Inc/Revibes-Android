/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 #if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import com.carissa.revibes.core.presentation.BaseViewModel
import org.koin.android.annotation.KoinViewModel

data class ${SCREEN_NAME}ScreenUiState(val isLoading: Boolean = false)

sealed interface ${SCREEN_NAME}ScreenUiEvent

@KoinViewModel
class ${SCREEN_NAME}ScreenViewModel :
    BaseViewModel<${SCREEN_NAME}ScreenUiState, ${SCREEN_NAME}ScreenUiEvent>(${SCREEN_NAME}ScreenUiState())
