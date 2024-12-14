package com.example.safebyte.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.ui.components.CircleParameters
import com.example.safebyte.ui.components.LineParameters
import com.example.safebyte.ui.components.MediaContent
import com.example.safebyte.ui.components.MessageBubble
import com.example.safebyte.ui.components.SecondaryTopBar
import com.example.safebyte.ui.components.TimelineNode
import com.example.safebyte.ui.components.TimelineNodePosition
import com.example.safebyte.ui.theme.SafeByteTheme
import java.util.Date

@Composable
fun HomeScreen() {
    Box {
        Text("Home Screen")

        val listState = rememberLazyListState()
        val itemsList = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val mediaContent = MediaContent(
            date = Date(),
            videoUrl = "https://firebasestorage.googleapis.com/v0/b/aluno-d3928.appspot.com/o/profileImages%2F10240187-sd_360_640_30fps.mp4?alt=media&token=b956caff-2380-43df-9e9f-283b52219a6d",
            activitiesList = listOf(
                "Activity 1",
                "Activity 2",
                "Activity 3",
                "Activity 4",
                "Activity 5",
            )
        )

        SafeByteTheme(
            darkTheme = false
        ) {
            val navController = rememberNavController()

            Scaffold(
                topBar = {
                    SecondaryTopBar(
                        title = "Home",
                        onBackClick = {
                            navController.navigate("home") {
                                popUpTo("home") { inclusive = true }
                            }
                        }
                    )
                }
            ) { paddingValues ->
                LazyColumn(
                    state = listState,
                    contentPadding = paddingValues
                ) {
                    items(
                        count = itemsList.size,
                        key = { itemsList[it] }
                    ) {
                        TimelineNode(
                            pos = TimelineNodePosition.FIRST,
                            circleParameters = CircleParameters(6.dp),
                            lineParameters = LineParameters(3.dp)
                        ) { modifier ->
                            MessageBubble(
                                mediaContent,
                                modifier
                            )
                        }
                    }

                }
            }
        }
    }

    // TODO: download google font: Montserrat
    // TODO: refactor button to use left icon
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}