package com.carissa.revibes.core.presentation.compose.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.carissa.revibes.core.data.main.model.FeatureName
import com.carissa.revibes.core.data.main.remote.config.ConfigRepository
import org.koin.compose.koinInject

@Composable
fun MaintenanceChecker(
    featureName: FeatureName,
    modifier: Modifier = Modifier,
    configRepository: ConfigRepository = koinInject(),
    onFeatureEnabled: @Composable () -> Unit = {},
    onBackAction: () -> Unit = {}
) {
    val isFeatureEnabled = remember { configRepository.getFeatureFlagKey(featureName) }

    StateSwitcherAnimator(isFeatureEnabled, modifier) { enabled ->
        if (enabled) {
            onFeatureEnabled()
        } else {
            ComingSoon(featureName.title, onClick = onBackAction)
        }
    }
}
