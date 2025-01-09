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
        SBHeader(userName = userName)

        Spacer(modifier = Modifier.height(16.dp))

        // Seção: Principais Serviços
        SBButtonBox(
            title = "Principais serviços",
            description = "As principais funcionalidades do nosso app",
            buttons = listOf(
                SBButtonBoxButtons(
                    iconRes = R.drawable.ic_qr_code,
                    label = "Verificar produto",
                    onClick = {
                        // Lógica para abrir a tela de verificar produto
                    }
                ),
                SBButtonBoxButtons(
                    iconRes = R.drawable.ic_history,
                    label = "Histórico alérgico",
                    onClick = { navController.navigate("allergy_history") }
                ),
                SBButtonBoxButtons(
                    iconRes = R.drawable.ic_info,
                    label = "Minhas alergias",
                    onClick = {
//                        navController.navigate("my_allergies")
                    }
                ),
                SBButtonBoxButtons(
                    iconRes = R.drawable.ic_qr_code,
                    label = "Gerar QR-Code",
                    onClick = {
                        // Lógica para abrir a tela de gerar QR-Code
                    }
                )
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Seção: Comunidade Recomenda
        SBButtonBox(
            title = "Comunidade recomenda",
            description = "As recomendações da nossa comunidade",
            buttons = listOf(
                SBButtonBoxButtons(
                    iconRes = R.drawable.ic_doctor,
                    label = "Médico",
                    onClick = {
                        navController.navigate("doctor_search")
                    }
                ),
                SBButtonBoxButtons(
                    iconRes = R.drawable.ic_restaurant,
                    label = "Restaurante",
                    onClick = {
                        // Lógica para abrir a tela de restaurantes
                    }
                )
            )
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
    HomeScreen(userName = "Francisco", navController = navController) { label ->
        println("Botão clicado: $label")
    }
}
