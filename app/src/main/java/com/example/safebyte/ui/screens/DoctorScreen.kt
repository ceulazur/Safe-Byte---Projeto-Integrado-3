package com.example.safebyte.ui.screens

import Doctor
import com.example.safebyte.ui.components.DoctorItem
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
//noinspection UsingMaterialAndMaterial3Libraries
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import com.example.safebyte.R

@Composable
fun DoctorProfileScreen(doctor: Doctor) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top section with com.example.safebyte.ui.components.DoctorItem
        DoctorItem(doctor = doctor)

        Spacer(modifier = Modifier.height(16.dp))

        // Specialties section
        SpecialtiesSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Contact section
        ContactSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Reviews section
        ReviewsSection()

        Spacer(modifier = Modifier.height(16.dp))

        // Add review button
        AddReviewButton()
    }
}

@Composable
fun SpecialtiesSection() {
    Column {
        Text(text = "Especialidades", style = MaterialTheme.typography.h6)
        Text(text = "• Cardiologia")
        Text(text = "• Medicina Interna")
        Text(text = "• Terapia Intensiva")
    }
}

@Composable
fun ContactSection() {
    Column {
        Text(text = "Contato", style = MaterialTheme.typography.h6)
        Text(text = "Telefone: (11) 1234-5678")
        Text(text = "Email: dr.johndoe@example.com")
        Text(text = "Endereço: Rua Exemplo, 123, São Paulo, SP")
    }
}

@Composable
fun ReviewsSection() {
    Column {
        Text(
            text = "Avaliações",
            style = MaterialTheme.typography.h6,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        val reviews = listOf(
            ReviewData(
                author = "Ana Paula",
                stars = 5,
                date = "22/01/2025",
                review = "Excelente médico, muito atencioso e profissional."
            ),
            ReviewData(
                author = "Carlos Eduardo",
                stars = 4,
                date = "18/01/2025",
                review = "Ótimo atendimento, recomendo a todos."
            ),
            ReviewData(
                author = "Mariana Silva",
                stars = 5,
                date = "15/01/2025",
                review = "Dr. John Doe foi muito cuidadoso e explicou tudo claramente."
            )
        )

        reviews.forEach { review ->
            ReviewItem(review = review)
        }
    }
}

@Composable
fun ReviewItem(review: ReviewData) {
    Card(
        shape = MaterialTheme.shapes.medium,
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = review.author,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = review.date,
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(review.stars) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_star), // Ícone de estrela
                        contentDescription = "Estrela",
                        tint = Color.Yellow,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = review.review,
                style = MaterialTheme.typography.body1
            )
        }
    }
}

@Composable
fun AddReviewButton() {
    var showDialog by remember { mutableStateOf(false) }

    Column {
        Button(
            onClick = { showDialog = true },
            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB3C8CF))
        ) {
            Text(text = "Adicionar Avaliação", color = Color.Black)
        }

        if (showDialog) {
            AddReviewDialog(onDismiss = { showDialog = false })
        }
    }
}


@Composable
fun AddReviewDialog(onDismiss: () -> Unit) {
    var reviewText by remember { mutableStateOf(TextFieldValue("")) }
    var selectedStars by remember { mutableStateOf(0) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Adicionando o título dentro do conjunto e espaçamento adequado
                Text(
                    text = "Deixe sua avaliação!",
                    style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                // Espaçamento entre o título e as estrelas
                Spacer(modifier = Modifier.height(16.dp))

                // Linha de estrelas logo abaixo do título
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    repeat(5) { index ->
                        Icon(
                            painter = painterResource(id = R.drawable.ic_star),
                            contentDescription = null,
                            tint = if (index < selectedStars) Color.Yellow else Color.Gray,
                            modifier = Modifier
                                .size(40.dp)
                                .padding(8.dp)
                                .clickable {
                                    selectedStars = index + 1
                                }
                        )
                    }
                }

                // Espaço entre as estrelas e o campo de texto
                Spacer(modifier = Modifier.height(16.dp))

                // Campo de texto para a avaliação
                BasicTextField(
                    value = reviewText,
                    onValueChange = { reviewText = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp)
                        .background(Color(0xFFF5F5F5), RoundedCornerShape(12.dp))
                        .padding(16.dp),
                    textStyle = MaterialTheme.typography.body1.copy(color = Color.Black)
                )
            }
        },
        confirmButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFB3C8CF)),
                onClick = {
                    onDismiss()
                },
                modifier = Modifier
                    .padding(end = 4.dp)
                    .width(140.dp)
            ) {
                Text("Adicionar", color = Color.Black)
            }
        },
        dismissButton = {
            Button(
                colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFEFF7FF)),
                onClick = {
                    onDismiss()
                },
                modifier = Modifier
                    .padding(start = 4.dp)
                    .width(140.dp)
            ) {
                Text("Cancelar", color = Color.Black)
            }
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.heightIn(min = 300.dp)
    )
}


data class ReviewData(
    val author: String,
    val stars: Int,
    val date: String,
    val review: String
)

@Preview(showBackground = true)
@Composable
fun PreviewDoctorProfileScreen() {
    val doctor = Doctor(
        name = "Dr. John Doe",
        location = "São Paulo, SP",
        rating = 4.5
    )

    DoctorProfileScreen(doctor = doctor)
}
