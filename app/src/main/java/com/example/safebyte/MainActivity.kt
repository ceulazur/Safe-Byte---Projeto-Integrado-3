package com.example.safebyte

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.ui.navigation.NavGraph
import com.example.safebyte.ui.theme.SafeByteTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SafeByteTheme {
                // Controlador de navegação
                val navController = rememberNavController()

                // Navegação via NavHost
                NavGraph(navController = navController)
            }
        }
    }
}
