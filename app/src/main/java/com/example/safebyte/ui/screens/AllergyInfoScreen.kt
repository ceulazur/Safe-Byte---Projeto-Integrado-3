package com.example.safebyte.ui.screens;

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.R
import com.example.safebyte.ui.components.SecondaryTopBar

@Composable
fun AllergyInfoScreen(navController: NavController) {
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
                // Título
                Text(
                    text = "Intolerância a lactose",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F152B)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Imagem
                Image(
                    painter = painterResource(id = R.drawable.milk),
                    contentDescription = "Imagem explicativa",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(bottom = 16.dp)
                )

                // Texto com Tópicos (Negrito)
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "• Definição:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = " A intolerância à lactose é a incapacidade de digerir a lactose, um açúcar encontrado no leite e em outros produtos lácteos.\n" +
                                "\n" +
                                "  Ocorre pela deficiência da enzima lactase, responsável por quebrar a lactose em glicose e galactose no intestino delgado.",
                        fontSize = 16.sp
                    )

                    Text(
                        text = "• Tipos:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Primária: perda progressiva da capacidade de produzir lactase, comum em adultos.\n" +
                                "\n" +
                                "Secundária: resulta de doenças intestinais, como doença de Crohn, que afetam a produção de lactase.\n" +
                                "\n" +
                                "Congênita: rara, ocorre desde o nascimento devido a uma mutação genética.",
                        fontSize = 16.sp
                    )

                    Text(
                        text = "• Tratamento e Gerenciamento:",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Dieta com restrição de lactose, optando por leites e derivados sem lactose.\n" +
                                "\n" +
                                "Suplementação de lactase em comprimidos ou gotas antes do consumo de alimentos lácteos.\n" +
                                "\n" +
                                "Alimentos alternativos, como leites vegetais (amêndoa, aveia, coco).",
                        fontSize = 16.sp
                    )

                }

            }
        }
    }
}

// Preview
@Preview(showBackground = true)
@Composable
fun AllergyInfoScreenPreview() {
    val navController = rememberNavController()
    AllergyInfoScreen(navController = navController)
}
