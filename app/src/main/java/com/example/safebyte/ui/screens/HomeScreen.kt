import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.R

@Composable
fun HomeScreen(
    userName: String,
    navController: NavHostController,
    onButtonClick: (String) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho
        SBHeader(
            userName = userName,
            navController = navController
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Seção: Principais Serviços
        SBButtonBox(
            title = "Principais serviços",
            description = "As principais funcionalidades do nosso app",
            buttons = listOf(
                Pair(R.drawable.ic_qr_code, "Verificar produto"),
                Pair(R.drawable.ic_history, "Histórico alérgico"),
                Pair(R.drawable.ic_info, "Minhas alergias"),
                Pair(R.drawable.ic_qr_code, "Gerar QR-Code")
            ),
            onButtonClick = { buttonLabel ->
                when (buttonLabel) {
                    "Histórico alérgico" -> navController.navigate("allergy_history")
                    "Minhas alergias" -> navController.navigate("my_allergies")
                    "Gerar QR-Code" -> navController.navigate("qr_code")
                    "Verificar produto" -> navController.navigate("product_search")
                    else -> onButtonClick(buttonLabel)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Seção: Comunidade Recomenda
        SBButtonBox(
            title = "Comunidade recomenda",
            description = "As recomendações da nossa comunidade",
            buttons = listOf(
                Pair(R.drawable.ic_doctor, "Médico"),
                Pair(R.drawable.ic_restaurant, "Restaurantes")
            ),
            onButtonClick = { buttonLabel ->
                when (buttonLabel) {
                    "Médico" -> navController.navigate("doctor_search")
                    else -> onButtonClick(buttonLabel)
                }
            }
        )

        Spacer(modifier = Modifier.weight(1f))

        // Barra de Navegação
        SBNavBar(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(
        userName = "Francisco",
        navController = navController
    ) { label ->
        println("Botão clicado: $label")
    }
}
