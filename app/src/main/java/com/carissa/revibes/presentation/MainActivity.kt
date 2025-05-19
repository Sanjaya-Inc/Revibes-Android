package com.carissa.revibes.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.carissa.revibes.core.data.remote.TestRemoteApi
import com.carissa.revibes.core.presentation.theme.RevibesTheme
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
    testRemoteApi: TestRemoteApi = koinInject()
) {
    Column(modifier = modifier) {
        Text(
            text = "Hello $name!",
        )
        val lifecycleScope = LocalLifecycleOwner.current.lifecycleScope
        Button(onClick = {
            lifecycleScope.launch {
                runCatching {
                    testRemoteApi.getPerson()
                }.onSuccess {
                    Log.d("ketai", "Success: $it")
                }.onFailure {
                    Log.d("ketai", "Failure: $it")
                }
            }
        }) {
            Text("Test API")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    RevibesTheme {
        Greeting("Android")
    }
}
