import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.res.stringResource
import com.example.safebyte.R
import com.example.safebyte.ui.components.SecondaryTopBar

@Composable
fun MyAllergiesScreen(navController: NavController) {
    val alergias = remember {
        mutableStateListOf(
            Alergia("Intolerância à lactose"),
            Alergia("Intolerância ao glúten"),
            Alergia("Intolerância à frutose")
        )
    }

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            SecondaryTopBar(
                title = stringResource(R.string.minhas_alergias),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Botão Adicionar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Row(
                        modifier = Modifier
                            .background(
                                color = Color(0xFFB3C8CF),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .clickable { showDialog = true } // Mostrar o modal ao clicar
                            .padding(horizontal = 12.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "+",
                            fontSize = 18.sp,
                            color = Color(0xFF0F152B)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Adicionar",
                            fontSize = 16.sp,
                            color = Color(0xFF0F152B)
                        )
                    }
                }

                // Lista de Alergias
                ListaDeAlergias(
                    alergias = alergias,
                    onEditClick = { /* Implementar edição */ },
                    onDeleteClick = { alergia ->
                        alergias.remove(alergia)
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Barra de Navegação
                SBNavBar(navController = navController)
            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun TelaAlergiasPreview() {
    val navController = rememberNavController()
    MyAllergiesScreen(navController = navController)
}
