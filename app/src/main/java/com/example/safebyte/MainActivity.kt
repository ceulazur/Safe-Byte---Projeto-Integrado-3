package com.example.safebyte

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.navigation.NavGraph
import com.example.safebyte.ui.theme.SafeByteTheme
import com.example.safebyte.ui.viewmodel.SettingsViewModel
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
                }
            }
        }
    }

    @Composable
    fun MainApp() {
        val context = LocalContext.current
        val navController = rememberNavController()
        val isLoggedIn by remember { mutableStateOf(false) }
        val settingsViewModel: SettingsViewModel = viewModel()

        // Verificação de permissão
        LaunchedEffect(Unit) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val notificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                if (!notificationManager.areNotificationsEnabled()) {
                    val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                        putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
                    }
                    context.startActivity(intent)
                }
            }
        }

        createNotificationChannel()

        NavGraph(
            navController = navController,
            isLoggedIn = isLoggedIn,
            onLoginSuccess = {
                navController.navigate("home")
            },
            settingsViewModel = settingsViewModel
        )
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d("NotificationChannel", "Creating notification channel")

            val channel = NotificationChannel(
                "daily_tips_channel",
                "Dicas Diárias",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notificações com dicas de segurança para alérgicos"
                enableLights(true)
                lightColor = android.graphics.Color.RED
                enableVibration(true)
            }

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}