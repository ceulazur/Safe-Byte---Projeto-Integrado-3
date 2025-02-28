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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.navigation.NavGraph
import com.example.safebyte.ui.theme.SafeByteTheme
import com.example.safebyte.viewmodel.SettingsViewModel
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MainActivity : ComponentActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            if (FirebaseApp.getApps(this).isEmpty()) {
                FirebaseApp.initializeApp(this)
            }
            auth = Firebase.auth
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

        auth.let {
            NavGraph(
                authViewModel = viewModel(),
                navController = navController,
                settingsViewModel = settingsViewModel,
                auth = it
            )
        }

    }

    private fun createNotificationChannel() {
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