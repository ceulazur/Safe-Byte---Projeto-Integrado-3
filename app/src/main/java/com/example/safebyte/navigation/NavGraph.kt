package com.example.safebyte.navigation

import AllergyInfoScreen
import Doctor
import DoctorSearchScreen
import HomeScreen
import MyAllergiesScreen
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.safebyte.ui.screens.AllergyHistoryScreen
import com.example.safebyte.ui.screens.DoctorProfileScreen
import com.example.safebyte.ui.screens.LoginScreen
import com.example.safebyte.ui.screens.SettingsScreen
import com.example.safebyte.ui.screens.SignUpScreen
import com.example.safebyte.ui.viewmodel.AuthViewModel
import com.example.safebyte.ui.viewmodel.SettingsViewModel

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel,
) {
    val isAnimationsEnabled by settingsViewModel.isAnimationsEnabled.collectAsState()
    val context = navController.context
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login",
        enterTransition = {
            if (isAnimationsEnabled) {
                fadeIn(animationSpec = tween(300))
            } else {
                EnterTransition.None
            }
        },
        exitTransition = {
            if (isAnimationsEnabled) {
                fadeOut(
                    animationSpec = tween(300)
                )
            } else {
                ExitTransition.None
            }
        }
    ) {
        // Tela de login
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                navController = navController
            )
        }

        // Tela inicial (Home)
        composable(route = "home") {
            LaunchedEffect(Unit) {
                settingsViewModel.loadThemeState(context)

                settingsViewModel.loadNotificationState(context)

                settingsViewModel.loadAnimationState(context)
            }

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
                onNavigateBack = { navController.popBackStack() },
                onNavigateMyAllergies = { navController.navigate("my_allergies") }
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

