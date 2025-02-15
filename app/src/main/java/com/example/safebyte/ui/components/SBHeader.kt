import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
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
import com.example.safebyte.ui.theme.Typography
import com.example.safebyte.R

@Composable
fun SBHeader(userName: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F9FA))
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
            Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {
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
                    text = "Allergic Care",
                    style = Typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = Color(0xFF0F152B)
                    )
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Seja bem-vindo,",
                style = Typography.bodySmall.copy(
                    fontSize = 14.sp,
                    color = Color(0xFF576675)
                )
            )
            Text(
                text = userName,
                style = Typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0F152B)
                )
            )
        }


        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFFD8E8ED), shape = CircleShape), 
            contentAlignment = Alignment.Center
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_profile),
                contentDescription = "Perfil",
                tint = Color(0xFF0F152B),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewHeader() {
    SBHeader(userName = "Francisco")
}
