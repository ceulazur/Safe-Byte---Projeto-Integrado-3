import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun SBLocationSearch() {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val locations = listOf("Ceará, BR", "Fortaleza, CE", "Quixadá, CE", "Paraipaba, CE")

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            placeholder = { Text("Localização") },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expanded = true },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color(0xFFF5F5F5),
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            locations.forEach { location ->
                DropdownMenuItem(
                    onClick = {
                        searchText = location
                        expanded = false
                    }
                ) {
                    Text(location)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationSearchFieldPreview() {
    MaterialTheme {
        SBLocationSearch()
    }
}
