package com.example.safebyte.ui.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.safebyte.ui.screens.AllergyHistoryScreen
import com.example.safebyte.ui.screens.HomeScreen
import com.example.safebyte.ui.screens.LoginScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login" // Tela inicial
    ) {
        // Tela de login
        composable("login") {
            LoginScreen(onLoginSuccess = {
                // Navegar para a HomeScreen após login bem-sucedido
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true } // Remove a tela de login da pilha de navegação
                }
            })
        }

        // Tela inicial (Home)
        composable("home") {
            HomeScreen(
                userName = "Francisco", 
                navController = navController, 
                onButtonClick = { label ->
                    when (label) {
                        "Histórico alérgico" -> navController.navigate("allergy_history")
                        else -> println("Botão clicado: $label")
                    }
                }
            )
        }

        // Tela de histórico alérgico
        composable("allergy_history") {
            AllergyHistoryScreen(navController = navController)
        }
    }
}
