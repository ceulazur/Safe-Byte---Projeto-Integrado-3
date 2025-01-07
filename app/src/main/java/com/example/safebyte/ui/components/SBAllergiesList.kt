import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.tooling.preview.Preview
import com.example.safebyte.R
import com.example.safebyte.ui.theme.SafeByteTheme

data class Alergia(val nome: String)

@Composable
fun ListaDeAlergias(
    alergias: List<Alergia>,
    onEditClick: (Alergia) -> Unit,
    onDeleteClick: (Alergia) -> Unit
) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        items(alergias.size) { index ->
            val alergia = alergias[index]
            ItemAlergia(
                alergia = alergia,
                onEditClick = { onEditClick(alergia) },
                onDeleteClick = { onDeleteClick(alergia) }
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ItemAlergia(
    alergia: Alergia,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFE0E0E0), RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = alergia.nome,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )
        Row {
            // Botão Editar
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 36.dp)
                    .background(Color(0xFFEFF7FF), shape = RoundedCornerShape(6.dp))
                    .clickable(onClick = onEditClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_edit),
                    contentDescription = "Editar",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp),
                    colorFilter = ColorFilter.tint(Color.Black)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            // Botão Excluir
            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 36.dp)
                    .background(Color(0xFFFF7D61), shape = RoundedCornerShape(6.dp))
                    .clickable(onClick = onDeleteClick),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Excluir",
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTelaDeAlergias() {
    val alergias = listOf(
        Alergia("Intolerância à lactose"),
        Alergia("Intolerância ao glúten"),
        Alergia("Intolerância à frutose")
    )
    ListaDeAlergias(
        alergias = alergias,
        onEditClick = { alergia -> println("Editar: ${alergia.nome}") },
        onDeleteClick = { alergia -> println("Excluir: ${alergia.nome}") }
    )
}
