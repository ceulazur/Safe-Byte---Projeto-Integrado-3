package com.example.safebyte

import SettingsViewModel
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.navigation.NavGraph
import com.example.safebyte.ui.theme.SafeByteTheme
import com.google.firebase.FirebaseApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        try {
            FirebaseApp.initializeApp(this)
            Log.d("SafeByteApplication", "Firebase initialized successfully")
        } catch (e: Exception) {
            Log.e("SafeByteApplication", "Error initializing Firebase: ${e.message}")
        }

        enableEdgeToEdge()


        setContent {
            val settingsViewModel: SettingsViewModel = viewModel()
            val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

            SafeByteTheme(darkTheme = isDarkTheme) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    MainApp()
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }

    @Composable
    fun MainApp() {
        val navController = rememberNavController()
        val isLoggedIn by remember { mutableStateOf(false) }
        val settingsViewModel: SettingsViewModel = viewModel ()

        NavGraph(
            navController = navController,
            isLoggedIn = isLoggedIn,
            onLoginSuccess = {
                navController.navigate("home")
            },
            settingsViewModel = settingsViewModel
        )
    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {

        SafeByteTheme(
            darkTheme = false
        ) {
            Greeting("Android")
        }
    }
}