package com.example.safebyte.ui.components

import Doctor
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safebyte.R

@Composable
fun SBDoctorList() {
    val doctors = listOf(
        Doctor(name = "Dr. Virgulino Batista", location = "Morada nova - CE", rating = 4.5),
        Doctor(name = "Dr. Carlinhos", location = "Solonópole - CE", rating = 5.0),
        Doctor(name = "Dr. Daniel", location = "Paraipaba - CE", rating = 4.5)
    )

    Column(modifier = Modifier.fillMaxSize()) {
        DoctorList(doctors)
    }
}

@Composable
fun DoctorList(doctors: List<Doctor>) {
    Column(modifier = Modifier.padding(16.dp)) {
        doctors.forEach { doctor ->
            DoctorItem(doctor)
            Divider(
                color = Color.LightGray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun DoctorItem(doctor: Doctor) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            modifier = Modifier.size(50.dp),
            color = Color.LightGray
        ) {
            // Placeholder for doctor's image
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = doctor.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black
            )
            Text(
                text = doctor.location,
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.ic_star),
                contentDescription = "Star Icon",
                tint = Color(0xFFFFD700),
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = doctor.rating.toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorListPreview() {
    MaterialTheme {
        SBDoctorList()
    }
}

