import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import com.example.safebyte.R


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
            .background(Color(0xFFB3C8CF), RoundedCornerShape(8.dp))
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
            // Botão Editar com feedback visual
            val editInteractionSource = remember { MutableInteractionSource() }
            val isEditPressed by editInteractionSource.collectIsPressedAsState()

            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 36.dp)
                    .background(
                        if (isEditPressed) Color(0xFFB0D6F7) else Color(0xFFEFF7FF),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable(
                        onClick = onEditClick,
                        interactionSource = editInteractionSource,
                        indication = null
                    ),
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

            // Botão Excluir com feedback visual
            val deleteInteractionSource = remember { MutableInteractionSource() }
            val isDeletePressed by deleteInteractionSource.collectIsPressedAsState()

            Box(
                modifier = Modifier
                    .size(width = 40.dp, height = 36.dp)
                    .background(
                        if (isDeletePressed) Color(0xFFFFB3B3) else Color(0xFFFF7D61),
                        shape = RoundedCornerShape(6.dp)
                    )
                    .clickable(
                        onClick = onDeleteClick,
                        interactionSource = deleteInteractionSource,
                        indication = null
                    ),
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
