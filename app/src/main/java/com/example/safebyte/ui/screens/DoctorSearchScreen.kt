package com.example.safebyte.ui.screens;

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.R
import com.example.safebyte.ui.components.SBLocationSearch
import com.example.safebyte.ui.components.SecondaryTopBar

data class Doctor(
    val name: String,
    val location: String,
    val rating: Double,
)

@Composable
fun DoctorSearchScreen(
    navController: NavController,
    onDoctorSelected: (Doctor) -> Unit,
) {
    val doctors = listOf(
        Doctor(name = "Dr. John Doe", location = "São Paulo, SP", rating = 4.5),
        Doctor(name = "Dra. Maria Clara", location = "Rio de Janeiro, RJ", rating = 4.8),
        Doctor(name = "Dr. Pedro Henrique", location = "Curitiba, PR", rating = 4.3)
    )

    Scaffold(
        topBar = {
            SecondaryTopBar(
                title = stringResource(R.string.medicos),
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            SBLocationSearch()
            Spacer(modifier = Modifier.height(16.dp))

            // Lista de médicos
            doctors.forEach { doctor ->
                DoctorListItem(
                    doctor = doctor,
                    onClick = { onDoctorSelected(doctor) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun DoctorListItem(doctor: Doctor, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Doctor Icon",
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, CircleShape),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = doctor.name,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = doctor.location,
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Text(
                    text = "⭐ ${doctor.rating}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Yellow
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorSearchScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        DoctorSearchScreen(
            navController = navController,
            onDoctorSelected = {}
        )
    }
}
