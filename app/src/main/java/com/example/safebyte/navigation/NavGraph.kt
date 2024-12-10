package com.example.safebyte.ui.navigation

import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.safebyte.ui.screens.LoginScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "login" // Tela inicial
    ) {
        composable("login") {
            LoginScreen(onLoginSuccess = {
                // Navegar para a HomeScreen após login bem-sucedido
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true } // Remove a tela de login da pilha de navegação
                }
            })
        }

        composable("home") {
            HomeScreen(userName = "Francisco", onButtonClick = { label ->
                println("Botão clicado: $label")
            })
        }
    }
}
