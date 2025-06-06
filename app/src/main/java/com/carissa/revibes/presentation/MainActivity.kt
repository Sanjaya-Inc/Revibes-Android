package com.carissa.revibes.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.carissa.revibes.auth.data.AuthRepository
import com.carissa.revibes.core.presentation.components.RevibesTheme
import com.carissa.revibes.core.presentation.components.components.Button
import com.carissa.revibes.core.presentation.components.components.ButtonVariant
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RevibesTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(
    name: String,
    modifier: Modifier = Modifier,
    authRepo: AuthRepository = koinInject()
) {
    Column(modifier = modifier.padding(16.dp)) {
        Text(
            text = "Hello $name!",
        )
        val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
        Button(
            text = "Test",
            modifier = Modifier.fillMaxWidth(),
            variant = ButtonVariant.Primary,
            onClick = {
                lifecycleScope.launch {
                    runCatching {
                        authRepo.loginWithEmail("ghanbudi@gmail.com", "Giri123#")
                    }.onSuccess {
                        Log.d("ketai", "Success: $it")
                    }.onFailure {
                        Log.d("ketai", "Failure: $it")
                    }
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RevibesTheme {
        Greeting("Android")
    }
}
