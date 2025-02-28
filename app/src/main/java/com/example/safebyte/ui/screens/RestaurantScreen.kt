package com.example.safebyte.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// Modelo de dados para restaurante
data class Restaurant(
    val name: String,
    val location: String,
    var rating: Double,
)

@Composable
fun RestaurantScreen(restaurant: Restaurant) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = restaurant.name,
            style = MaterialTheme.typography.h4,
            fontWeight = FontWeight.Bold
        )
        Text(text = restaurant.location, style = MaterialTheme.typography.body1, color = Color.Gray)
        Text(
            text = "⭐ ${restaurant.rating}",
            style = MaterialTheme.typography.body1,
            color = Color.Yellow
        )
        Spacer(modifier = Modifier.height(16.dp))
        MenuSection()
        Spacer(modifier = Modifier.height(16.dp))
        ReviewsSection()
        Spacer(modifier = Modifier.height(16.dp))
        AddReviewButton()
    }
}

@Composable
fun MenuSection() {
    Column {
        Text(text = "Menu", style = MaterialTheme.typography.h6)
        Text(text = "• Prato Especial da Casa")
        Text(text = "• Sobremesas Variadas")
        Text(text = "• Bebidas Artesanais")
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantScreenPreview() {
    val restaurant = Restaurant(
        name = "Gourmet Delight",
        location = "São Paulo, SP",
        rating = 4.7
    )
    RestaurantScreen(restaurant = restaurant)
}
