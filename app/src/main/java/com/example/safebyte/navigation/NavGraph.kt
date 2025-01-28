package com.example.safebyte.navigation

import AllergyInfoScreen
import Doctor
import DoctorSearchScreen
import HomeScreen
import MyAllergiesScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.safebyte.ui.screens.AllergyHistoryScreen
import com.example.safebyte.ui.screens.DoctorProfileScreen
import com.example.safebyte.ui.screens.LoginScreen
import com.example.safebyte.ui.screens.SettingsScreen
import com.example.safebyte.ui.screens.SignUpScreen
import com.example.safebyte.ui.viewmodel.SettingsViewModel

@Composable
fun NavGraph(
    navController: NavHostController,
    isLoggedIn: Boolean,
    onLoginSuccess: () -> Unit,
    settingsViewModel: SettingsViewModel,
) {
    val context = LocalContext.current
    val isDarkTheme by settingsViewModel.isDarkTheme.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login" // Tela inicial
    ) {
        // Tela de login
        composable("login") {
            LoginScreen(
                onLoginSuccess = onLoginSuccess,
                navController = navController
            )
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

        // Tela de sign up
        composable("sign_up") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("login")
                },
                navigateController = navController
            )
        }

        //Tela de Settings
        composable("settings") {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.navigate("home") }, // Navega de volta para a Home
                onNavigateMyAllergies = { navController.navigate("my_allergies") } // Navega para Minhas Alergias
            )
        }


        //Doctor Search
        composable("doctor_search") {
            DoctorSearchScreen(navController = navController)
        }

        // Doctor Profile
        composable(
            "doctor_profile/{doctor_name}/{doctor_location}/{doctor_rating}",
            arguments = listOf(
                navArgument("doctor_name") { defaultValue = "" },
                navArgument("doctor_location") { defaultValue = "" },
                navArgument("doctor_rating") { defaultValue = 0.0 }
            )
        ) { backStackEntry ->
            val doctorName = backStackEntry.arguments?.getString("doctor_name") ?: ""
            val doctorLocation = backStackEntry.arguments?.getString("doctor_location") ?: ""
            val doctorRating =
                backStackEntry.arguments?.getString("doctor_rating")?.toDoubleOrNull() ?: 0.0

            val doctor = Doctor(
                name = doctorName,
                location = doctorLocation,
                rating = doctorRating
            )
            DoctorProfileScreen(doctor = doctor)

        }
    }
}

