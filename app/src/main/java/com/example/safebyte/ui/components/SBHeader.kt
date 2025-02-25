import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberAsyncImagePainter
import com.example.safebyte.R
import com.example.safebyte.ui.theme.Typography

@Composable
fun SBHeader(userName: String, navController: NavHostController, profileImageUri: Uri?) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F9FA))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Seção de logo e nome SafeByte
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
            LogoSection()
            Spacer(modifier = Modifier.height(8.dp)) // Espaço entre logo e saudação
            GreetingSection(userName)
        }

        // Seção de perfil
        ProfileSection(navController, profileImageUri)
    }
}

@Composable
fun LogoSection() {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFD8E8ED), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = "Logo",
                tint = Color(0xFF0F152B),
                modifier = Modifier.size(24.dp)
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "SafeByte",
            color = Color(0xFF0F152B),
            modifier = Modifier.align(Alignment.CenterVertically)
        )
    }
}

@Composable
fun GreetingSection(userName: String) {
    Text(
        text = "Seja bem-vindo, $userName",
        color = Color.Black,
        modifier = Modifier.padding(top = 4.dp)
    )
}

@Composable
fun ProfileSection(navController: NavHostController, profileImageUri: Uri?) {
    IconButton(onClick = { navController.navigate("profile_picture") }) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFD8E8ED), shape = CircleShape),
            contentAlignment = Alignment.Center
        ) {
            if (profileImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(profileImageUri),
                    contentDescription = "Foto de Perfil",
                    modifier = Modifier.size(48.dp)
                )
            } else {
                Icon(
                    painter = painterResource(id = R.drawable.ic_profile),
                    contentDescription = "Perfil",
                    tint = Color(0xFF0F152B),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

