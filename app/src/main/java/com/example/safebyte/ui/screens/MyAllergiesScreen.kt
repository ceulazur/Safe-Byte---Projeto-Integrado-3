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
    var showEditDialog by remember { mutableStateOf(false) }
    var selectedAlergia by remember { mutableStateOf<Alergia?>(null) }
    var editingAlergia by remember { mutableStateOf<Alergia?>(null) }
    var expanded by remember { mutableStateOf(false) }

    val alergiasDisponiveis = listOf(
        Alergia("Intolerância à lactose"),
        Alergia("Intolerância ao glúten"),
        Alergia("Intolerância à frutose"),
        Alergia("Alergia ao amendoim"),
        Alergia("Alergia ao ovo")
    )

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
                    onEditClick = { alergia ->
                        editingAlergia = alergia
                        showEditDialog = true
                    },
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

            // Modal de Adicionar Alergia
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Text(text = "Nova Alergia")
                    },
                    text = {
                        Column {
                            Text(text = "Selecione sua alergia:")
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .clickable { expanded = true }
                                    .padding(12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = selectedAlergia?.nome
                                            ?: "Selecione sua alergia",
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_dropdown),
                                        contentDescription = "Dropdown Icon",
                                        tint = Color.Gray
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    alergiasDisponiveis.forEach { alergia ->
                                        DropdownMenuItem(onClick = {
                                            selectedAlergia = alergia
                                            expanded = false
                                        }) {
                                            Text(text = alergia.nome)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB3C8CF)),
                            onClick = {
                                selectedAlergia?.let { alergias.add(it) }
                                showDialog = false
                                selectedAlergia = null
                            }
                        ) {
                            Text(text = "Confirmar", color = Color.Black)
                        }
                    },
                    dismissButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEFF7FF)),
                            onClick = {
                                showDialog = false
                                selectedAlergia = null
                            }
                        ) {
                            Text(text = "Descartar", color = Color.Black)
                        }
                    },
                    shape = RoundedCornerShape(16.dp) // Adicionando bordas arredondadas ao modal
                )
            }

            // Modal de Editar Alergia
            if (showEditDialog) {
                AlertDialog(
                    onDismissRequest = { showEditDialog = false },
                    title = {
                        Text(text = "Editar Alergia")
                    },
                    text = {
                        Column {
                            Text(text = "Selecione sua alergia:")
                            Spacer(modifier = Modifier.height(16.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFF5F5F5), RoundedCornerShape(8.dp))
                                    .clickable { expanded = true }
                                    .padding(12.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = editingAlergia?.nome
                                            ?: "Selecione sua alergia",
                                        color = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.weight(1f))
                                    Icon(
                                        painter = painterResource(id = R.drawable.ic_dropdown),
                                        contentDescription = "Dropdown Icon",
                                        tint = Color.Gray
                                    )
                                }

                                DropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false }
                                ) {
                                    alergiasDisponiveis.forEach { alergia ->
                                        DropdownMenuItem(onClick = {
                                            editingAlergia = alergia
                                            expanded = false
                                        }) {
                                            Text(text = alergia.nome)
                                        }
                                    }
                                }
                            }
                        }
                    },
                    confirmButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF0F152B)),
                            onClick = {
                                editingAlergia?.let { updatedAlergia ->
                                    val index = alergias.indexOfFirst { it == editingAlergia }
                                    if (index != -1) {
                                        alergias[index] = updatedAlergia
                                    }
                                }
                                showEditDialog = false
                                editingAlergia = null
                            }
                        ) {
                            Text(text = "Confirmar", color = Color.White)
                        }
                    },
                    dismissButton = {
                        Button(
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEFF7FF)),
                            onClick = {
                                showEditDialog = false
                                editingAlergia = null
                            }
                        ) {
                            Text(text = "Descartar", color = Color.Black)
                        }
                    },
                    shape = RoundedCornerShape(16.dp) // Adicionando bordas arredondadas ao modal
                )
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

