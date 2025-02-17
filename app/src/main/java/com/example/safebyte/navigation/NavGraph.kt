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
import com.example.safebyte.ui.screens.QrCodeScannerScreen
import com.example.safebyte.ui.screens.QrCodeScreen
import com.example.safebyte.ui.screens.User
import com.example.safebyte.ui.screens.LoginScreen
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
                fadeOut(animationSpec = tween(300))
            } else {
                ExitTransition.None
            }
        }
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
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
                        "QR Code" -> navController.navigate("qr_code")
                        else -> println("Botão clicado: $label")
                    }
                }
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

        composable(
            "qr_report/{qr_data}",
            arguments = listOf(navArgument("qr_data") { type = NavType.StringType })
        ) { backStackEntry ->
            val qrData = backStackEntry.arguments?.getString("qr_data") ?: ""
            QrReportScreen(navController = navController, scannedData = qrData)
        }
    }
}
