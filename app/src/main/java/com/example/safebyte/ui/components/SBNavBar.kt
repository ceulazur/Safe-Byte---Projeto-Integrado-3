import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.safebyte.R
import com.example.safebyte.ui.theme.Typography

data class BottomNavItem(
    val label: String,
    val icon: Int,
    val route: String
)

@Composable
fun SBNavBar(navController: NavController) {
    val items = remember {
        listOf(
            BottomNavItem("Scanner", R.drawable.ic_qr_code, "scanner"),
            BottomNavItem("Home", R.drawable.ic_home, "home"),
            BottomNavItem("Configurações", R.drawable.ic_settings, "settings")
        )
    }

    BottomNavigation(
        backgroundColor = Color(0xFFB3C8CF),
        contentColor = Color(0xFF0F152B),
        elevation = 16.dp,
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
            .height(80.dp)
    ) {
        val currentRoute = navController.currentDestination?.route

        items.forEach { item ->
            BottomNavigationItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = {
                    Icon(
                        painter = painterResource(id = item.icon),
                        contentDescription = item.label,
                        tint = Color(0xFF0F152B)
                    )
                },
                label = {
                    Text(
                        text = item.label,
                        style = Typography.bodySmall.copy(
                            textAlign = TextAlign.Center,
                            color = Color(0xFF0F152B)
                        )
                    )
                },
                alwaysShowLabel = true,
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .align(Alignment.CenterVertically)
            )
        }
    }
}

@Preview
@Composable
fun PreviewSBNavBar() {
    val navController = rememberNavController()
    SBNavBar(navController = navController)
}
