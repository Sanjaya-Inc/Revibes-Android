package com.carissa.revibes.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.carissa.revibes.core.presentation.components.MainBackground
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.presentation.navigation.NavigationEventBusHandler
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.generated.app.AppNavGraphs

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }
}
