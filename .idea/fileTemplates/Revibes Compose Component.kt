/*
 * Copyright (c) 2025 Sanjaya Inc. All rights reserved.
 */
 
#if (${PACKAGE_NAME} && ${PACKAGE_NAME} != "")package ${PACKAGE_NAME}

#end
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.carissa.revibes.core.presentation.components.RevibesTheme

@Composable
fun ${COMPONENT_NAME}(
    modifier: Modifier = Modifier
) {
    TODO()
}

@Composable
@Preview
private fun ${COMPONENT_NAME}Preview() {
    RevibesTheme {
        ${COMPONENT_NAME}()
    }
}
