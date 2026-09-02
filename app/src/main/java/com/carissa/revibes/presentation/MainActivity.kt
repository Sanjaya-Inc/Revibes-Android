package com.carissa.revibes.presentation

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.lifecycleScope
import com.carissa.revibes.core.presentation.compose.RevibesTheme
import com.carissa.revibes.core.presentation.compose.components.MainBackground
import com.carissa.revibes.core.presentation.navigation.KickUserToLogin
import com.carissa.revibes.core.presentation.navigation.NavigationEventBus
import com.carissa.revibes.home_admin.domain.GoogleMapsPoint
import com.carissa.revibes.home_admin.domain.PendingGoogleMapsShare
import com.carissa.revibes.home_admin.presentation.screen.ManageDropOffPointsScreenUiEvent
import com.carissa.revibes.presentation.navigation.NavigationEventBusHandler
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.app.AppNavGraphs
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {

    private val navigationBus: NavigationEventBus by inject()
    private val pendingGoogleMapsShare: PendingGoogleMapsShare by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        lifecycleScope.launch {
            navigationBus.filter { it is KickUserToLogin }.collect {
                val intent = Intent(this@MainActivity, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                }
                startActivity(intent)
                finish()
            }
        }
        enableEdgeToEdge()
        setContent {
            MainBackground()
            RevibesTheme { navController ->
                NavigationEventBusHandler()
                DestinationsNavHost(
                    navGraph = AppNavGraphs.revibes,
                    navController = navController
                )
            }
        }
        handleIncomingMapsShare(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        handleIncomingMapsShare(intent)
    }

    private fun handleIncomingMapsShare(intent: Intent?) {
        val text = when (intent?.action) {
            Intent.ACTION_SEND -> intent.getStringExtra(Intent.EXTRA_TEXT)
            Intent.ACTION_VIEW -> intent.dataString
            else -> null
        }?.trim().orEmpty()
        if (text.isBlank() || !GoogleMapsPoint.looksLikeMapsShare(text)) return
        pendingGoogleMapsShare.offer(text)
        navigationBus.post(ManageDropOffPointsScreenUiEvent.NavigateToAddDropOffPoint)
    }
}
