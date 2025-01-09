package com.example.safebyte.navigation

import AllergyInfoScreen
import DoctorSearchScreen
import HomeScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.safebyte.ui.screens.AllergyHistoryScreen
import com.example.safebyte.ui.screens.LoginScreen
import MyAllergiesScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login" // Tela inicial
    ) {
        // Tela de login
        composable("login") {
            LoginScreen(onLoginSuccess = onLoginSuccess)
        }

        // Tela inicial (Home)
        composable("home") {
            HomeScreen(
                userName = "Francisco",
                navController = navController,
                onButtonClick = { label ->
                    when (label) {
                        "Histórico alérgico" -> navController.navigate("allergy_history")
                        "Minhas alergias" -> navController.navigate("my_allergies")
                        "Intolerância a lactose" -> navController.navigate("allergies_info")
                        "Médico" -> navController.navigate("doctor_search")
                        else -> println("Botão clicado: $label")
                    }
                }
            )
        }

        // Tela de histórico alérgico
        composable("allergy_history") {
            AllergyHistoryScreen(navController = navController)
        }

        // Tela de Minhas Alergias
        composable("my_allergies") {
            MyAllergiesScreen(navController = navController)
        }

        // Info Alergias
        composable("allergies_info") {
            AllergyInfoScreen(navController = navController)
        }

        //Doctor Search
        composable("doctor_search"){
            DoctorSearchScreen(navController = navController)
        }
    }
}

