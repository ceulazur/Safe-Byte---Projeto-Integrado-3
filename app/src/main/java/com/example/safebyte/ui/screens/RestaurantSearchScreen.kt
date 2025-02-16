package com.example.safebyte.ui.screens

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
import com.example.safebyte.ui.components.SecondaryTopBar

@Composable
fun RestaurantSearchScreen(
    navController: NavController,
    onRestaurantSelected: (Restaurant) -> Unit
) {
    val restaurants = listOf(
        Restaurant(name = "Gourmet Delight", location = "São Paulo, SP", rating = 4.7),
        Restaurant(name = "Sabor Carioca", location = "Rio de Janeiro, RJ", rating = 4.6),
        Restaurant(name = "Delícias do Sul", location = "Curitiba, PR", rating = 4.5)
    )

    Scaffold(
        topBar = {
            SecondaryTopBar(
                title = stringResource(R.string.restaurantes),
                onBackClick = { navController.popBackStack() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            restaurants.forEach { restaurant ->
                RestaurantListItem(
                    restaurant = restaurant,
                    onClick = { onRestaurantSelected(restaurant) }
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
fun RestaurantListItem(restaurant: Restaurant, onClick: () -> Unit) {
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
                painter = painterResource(id = R.drawable.ic_restaurant),
                contentDescription = "Restaurant Icon",
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.LightGray, CircleShape),
                tint = Color.White
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = restaurant.name,
                    style = MaterialTheme.typography.subtitle1,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = restaurant.location,
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
                Text(
                    text = "⭐ ${restaurant.rating}",
                    style = MaterialTheme.typography.body2,
                    color = Color.Yellow
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RestaurantSearchScreenPreview() {
    val navController = rememberNavController()
    MaterialTheme {
        RestaurantSearchScreen(
            navController = navController,
            onRestaurantSelected = {}
        )
    }
}
