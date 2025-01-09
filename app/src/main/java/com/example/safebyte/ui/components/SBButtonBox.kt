import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.safebyte.R
import com.example.safebyte.ui.theme.Typography

data class SBButtonBoxButtons (
    val iconRes: Int,
    val label: String,
    val onClick: () -> Unit
)

@Composable
fun SBButtonBox(
    title: String,
    description: String,
    buttons: List<SBButtonBoxButtons>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(
                color = Color(0xFFB3C8CF),
                shape = RoundedCornerShape(16.dp)
            )
            .padding(16.dp)
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_bell),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color(0xFF0F152B)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = title,
                style = Typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF0F152B)
                )
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = description,
            style = Typography.bodySmall.copy(
                fontSize = 14.sp,
                color = Color(0xFF0F152B)
            )
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botões com SBButton
        val rows = buttons.chunked(2) // Divide os botões em linhas de 2
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            rows.forEach { rowButtons ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterHorizontally)
                ) {
                    rowButtons.forEach { (iconRes, label, action) ->
                        SBButton(iconRes = iconRes, label = label, onClick = action)
                    }
                }
            }
        }
    }
}

@Composable
 fun SBButton(
    iconRes: Int,
    label: String,
    backgroundColor: Color = Color(0xFFEFF7FF),
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(120.dp, 100.dp)
            .background(color = backgroundColor, shape = RoundedCornerShape(16.dp))
            .padding(8.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(32.dp),
                tint = Color(0xFF0F152B)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = label,
                style = Typography.bodySmall.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    color = Color(0xFF0F152B)
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFeatureSection() {
    SBButtonBox(
        title = "Principais serviços",
        description = "As principais funcionalidades do nosso app",
        buttons = listOf(

            SBButtonBoxButtons(
                iconRes = R.drawable.ic_heart,
                label = "Allergy History",
                onClick = { /* Lógica do clique do botão 1 */ }
            ),
            SBButtonBoxButtons(
                iconRes = R.drawable.ic_heart,
                label = "Allergy History",
                onClick = { /* Lógica do clique do botão 1 */ }
            ),
            SBButtonBoxButtons(
                iconRes = R.drawable.ic_heart,
                label = "Allergy History",
                onClick = { /* Lógica do clique do botão 1 */ }
            ),
            SBButtonBoxButtons(
                iconRes = R.drawable.ic_heart,
                label = "Allergy History",
                onClick = { /* Lógica do clique do botão 1 */ }
            )
        )
    )
}
