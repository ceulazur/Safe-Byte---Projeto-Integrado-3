import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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

@Composable
fun SBButtonBox(
    title: String,
    description: String,
    buttons: List<Pair<Int, String>>,
    onButtonClick: (String) -> Unit
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
        // Cabeçalho com ícone e título
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

        // Descrição
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
                    rowButtons.forEach { (iconRes, label) ->
                        SBButton(iconRes = iconRes, label = label) {
                            onButtonClick(label)
                        }
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
            .clickable(onClick = onClick), // Torna o botão clicável
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
            Pair(R.drawable.ic_qr_code, "Verificar produto"),
            Pair(R.drawable.ic_history, "Histórico alérgico"),
            Pair(R.drawable.ic_info, "Minhas alergias"),
            Pair(R.drawable.ic_qr_code, "Gerar QR-Code")
        )
    ) { label ->
        println("Botão clicado: $label") // Exibe o rótulo do botão clicado no console
    }
}
