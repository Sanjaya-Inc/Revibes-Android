package com.carissa.revibes.core.domain.usecase

import android.content.Context
import android.widget.Toast
import com.carissa.revibes.core.presentation.navigation.KickUserToLogin
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.core.presentation.util.AppDispatchers
import com.carissa.revibes.core.presentation.util.AppScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.core.annotation.Single

@Single
class TokenExpiredUseCase(
    private val clearUserDataUseCase: ClearUserDataUseCase,
    private val context: Context,
    private val appDispatchers: AppDispatchers,
    private val appScope: AppScope,
    private val navigationEventBus: NavigationEventBus
) {
    private var kickingJob: Job? = null

    operator fun invoke() {
        if (kickingJob == null || kickingJob?.isCompleted == true) {
            appScope.launch(appDispatchers.main) {
                clearUserDataUseCase()
                navigationEventBus.post(KickUserToLogin)
                Toast.makeText(
                    context,
                    "Session expired, please login again",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
