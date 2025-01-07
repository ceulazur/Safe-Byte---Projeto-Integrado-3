import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.R
import com.example.safebyte.ui.components.SecondaryTopBar
import SBDoctorList
import SBLocationSearch

data class Doctor(
    val name: String,
    val location: String,
    val rating: Double
)

@Composable
fun DoctorSearchScreen(navController: NavController) {

    Scaffold(
        topBar = {
            SecondaryTopBar(
                title = stringResource(R.string.historico_de_alergias),
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
            SBDoctorList()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DoctorSearchScreenPreview() {
    MaterialTheme {
        DoctorSearchScreen(navController = rememberNavController())
    }
}
