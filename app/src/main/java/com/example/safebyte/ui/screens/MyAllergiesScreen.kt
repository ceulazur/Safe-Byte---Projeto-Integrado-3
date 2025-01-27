import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
                    },
                    onItemClick = { alergia ->
                        // Navegar para a tela de detalhes da alergia
                        navController.navigate("allergies_info")
                    }
                )

                Spacer(modifier = Modifier.weight(1f))

                // Barra de Navegação
                SBNavBar(navController = navController)
            }
        }
    }
}

// Lista de Alergias com o clique do item
@Composable
fun ListaDeAlergias(
    alergias: List<Alergia>,
    onEditClick: (Alergia) -> Unit,
    onDeleteClick: (Alergia) -> Unit,
    onItemClick: (Alergia) -> Unit // Nova função para navegar ao clicar na alergia
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(alergias.size) { index ->
            val alergia = alergias[index]
            ItemAlergia(
                alergia = alergia,
                onEditClick = { onEditClick(alergia) },
                onDeleteClick = { onDeleteClick(alergia) },
                onItemClick = { onItemClick(alergia) } // Passa a função de navegação
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ItemAlergia(
    alergia: Alergia,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onItemClick: () -> Unit // Função para navegar ao clicar no item
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFB3C8CF), RoundedCornerShape(8.dp))
            .padding(16.dp)
            .clickable { onItemClick() }, // Navegar ao clicar no item
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = alergia.nome,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Row {
            // Botão Editar
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 36.dp)
                    .background(Color(0xFFEFF7FF), shape = RoundedCornerShape(6.dp))
                    .clickable(onClick = onEditClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Botão Excluir
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 36.dp)
                    .background(Color(0xFFFF7D61), shape = RoundedCornerShape(6.dp))
                    .clickable(onClick = onDeleteClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Excluir",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp)
                )
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
