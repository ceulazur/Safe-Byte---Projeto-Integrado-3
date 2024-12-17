package com.example.safebyte.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import com.example.safebyte.ui.components.SBButtonPrimary

@Composable
fun HomeScreen(
    navController: NavHostController
) {
    Box {
        Text("Home Screen")

        SBButtonPrimary(
            label = "Allergy History",
            onClick = { navController.navigate("allergy_history") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = NavHostController(LocalContext.current))
}