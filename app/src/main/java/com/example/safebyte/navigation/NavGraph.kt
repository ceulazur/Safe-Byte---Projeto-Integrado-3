package com.example.safebyte.navigation

import HomeScreen
import MyAllergiesScreen
import ProductDetailsScreen
import ProductSearchScreen
import ProfilePictureScreen
import android.net.Uri
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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.safebyte.ui.screens.AllergyHistoryScreen
import com.example.safebyte.ui.screens.AllergyInfoScreen
import com.example.safebyte.ui.screens.Doctor
import com.example.safebyte.ui.screens.DoctorProfileScreen
import com.example.safebyte.ui.screens.DoctorSearchScreen
import com.example.safebyte.ui.screens.FAQScreen
import com.example.safebyte.ui.screens.LoginScreen
import com.example.safebyte.ui.screens.QrCodeScannerScreen
import com.example.safebyte.ui.screens.QrCodeScreen
import com.example.safebyte.ui.screens.Restaurant
import com.example.safebyte.ui.screens.RestaurantScreen
import com.example.safebyte.ui.screens.RestaurantSearchScreen
import com.example.safebyte.ui.screens.SettingsScreen
import com.example.safebyte.ui.screens.SignUpScreen
import com.example.safebyte.ui.screens.User
import com.example.safebyte.viewmodel.AuthViewModel
import com.example.safebyte.viewmodel.SettingsViewModel
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    settingsViewModel: SettingsViewModel,
    auth: FirebaseAuth
) {
    val isAnimationsEnabled by settingsViewModel.isAnimationsEnabled.collectAsState()
    val context = navController.context
    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "home" else "login",
        enterTransition = {
            if (isAnimationsEnabled) {
                fadeIn(animationSpec = tween(800))
            } else {
                EnterTransition.None
            }
        },
        exitTransition = {
            if (isAnimationsEnabled) {
                fadeOut(animationSpec = tween(300))
            } else {
                ExitTransition.None
            }
        }
    ) {
        composable("login") {
            LoginScreen(
                navController = navController
            )
        }

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
                        "Restaurantes" -> navController.navigate("restaurant_search")
                        "Gerar QR-Code" -> navController.navigate("qr_code")
                        "Verificar Produto" -> navController.navigate("product_search")
                        else -> println("Botão clicado: $label")
                    }
                }
            )
        }

        composable("product_search") {
            ProductSearchScreen(navController = navController)
        }

        composable("allergy_history") {
            AllergyHistoryScreen(navController = navController)
        }

        composable("my_allergies") {
            MyAllergiesScreen(navController = navController)
        }

        composable("faq") {
            FAQScreen(onNavigateBack = { navController.popBackStack() })
        }

        composable("allergies_info") {
            AllergyInfoScreen(navController = navController)
        }

        composable("sign_up") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("login")
                },
                navigateController = navController,
            )
        }

        composable("settings") {
            SettingsScreen(
                viewModel = settingsViewModel,
                onNavigateBack = { navController.popBackStack() },
                onNavigateMyAllergies = { navController.navigate("my_allergies") },
                onNavigateFAQ = { navController.navigate("faq") }
            )
        }

        composable("qr_code") {
            val user = User("João Silva", "joao.silva@example.com", listOf("Amendoim", "Lactose"))
            QrCodeScreen(navController = navController, user = user)
        }

        composable("qr_scanner") {
            QrCodeScannerScreen(navController = navController, onQrCodeScanned = { qrData ->
                navController.navigate("qr_report/${Uri.encode(qrData)}")
            })
        }

        composable("profile_picture") {
            ProfilePictureScreen(onImageSelected = { uri -> })
        }

        composable("doctor_search") {
            DoctorSearchScreen(
                navController = navController,
                onDoctorSelected = { doctor ->
                    val encodedName = Uri.encode(doctor.name)
                    val encodedLocation = Uri.encode(doctor.location)
                    val doctorRating = doctor.rating.toString()

                    navController.navigate("doctor_profile/$encodedName/$encodedLocation/$doctorRating")
                }
            )
        }

        composable(
            "doctor_profile/{doctor_name}/{doctor_location}/{doctor_rating}",
            arguments = listOf(
                navArgument("doctor_name") { type = NavType.StringType },
                navArgument("doctor_location") { type = NavType.StringType },
                navArgument("doctor_rating") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val doctorName = backStackEntry.arguments?.getString("doctor_name") ?: ""
            val doctorLocation = backStackEntry.arguments?.getString("doctor_location") ?: ""
            val doctorRating = backStackEntry.arguments?.getFloat("doctor_rating") ?: 0.0f

            val doctor = Doctor(
                name = doctorName,
                location = doctorLocation,
                rating = doctorRating.toDouble()
            )

            DoctorProfileScreen(doctor = doctor)
        }

        composable("restaurant_search") {
            RestaurantSearchScreen(
                navController = navController,
                onRestaurantSelected = { restaurant ->
                    val encodedName = Uri.encode(restaurant.name)
                    val encodedLocation = Uri.encode(restaurant.location)
                    val restaurantRating = restaurant.rating.toString()

                    navController.navigate("restaurant/${encodedName}/${encodedLocation}/${restaurantRating}")
                }
            )
        }

        composable(
            "restaurant/{restaurant_name}/{restaurant_location}/{restaurant_rating}",
            arguments = listOf(
                navArgument("restaurant_name") { type = NavType.StringType },
                navArgument("restaurant_location") { type = NavType.StringType },
                navArgument("restaurant_rating") { type = NavType.FloatType }
            )
        ) { backStackEntry ->
            val restaurantName = backStackEntry.arguments?.getString("restaurant_name") ?: ""
            val restaurantLocation = backStackEntry.arguments?.getString("restaurant_location") ?: ""
            val restaurantRating = backStackEntry.arguments?.getFloat("restaurant_rating") ?: 0.0f

            val restaurant = Restaurant(
                name = restaurantName,
                location = restaurantLocation,
                rating = restaurantRating.toDouble()
            )

            RestaurantScreen(restaurant = restaurant)
        }


        composable(
            "product_details/{barcode}",
            arguments = listOf(navArgument("barcode") { type = NavType.StringType })
        ) { backStackEntry ->
            val barcode = backStackEntry.arguments?.getString("barcode") ?: ""
            ProductDetailsScreen(navController = navController, barcode = barcode)
        }
    }
}
