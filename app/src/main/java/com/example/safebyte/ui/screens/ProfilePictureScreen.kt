import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.graphics.Color
import coil.compose.rememberAsyncImagePainter
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilePictureScreen(onImageSelected: (Uri) -> Unit) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf(getSavedProfilePicture(context)) }
    var isImageSelected by remember { mutableStateOf(false) }

    // Lançador para selecionar a imagem
    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            imageUri = it
            isImageSelected = true
            onImageSelected(it) // Notifica a mudança da imagem
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Cabeçalho (Header)
        TopAppBar(
            title = { Text("Alterar Foto de Perfil") },
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Exibe a imagem de perfil (caso exista)
        imageUri?.let {
            Image(
                painter = rememberAsyncImagePainter(it),
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .align(Alignment.CenterHorizontally)
            )
        } ?: run {
            // Caso não tenha imagem salva, exibe um texto informando
            Text(
                text = "Nenhuma imagem selecionada",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                color = Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para escolher uma nova imagem
        Button(onClick = { launcher.launch("image/*") }, modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Escolher Foto")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botão para salvar a imagem (apenas aparece quando uma imagem é selecionada)
        if (isImageSelected) {
            Button(
                onClick = {
                    imageUri?.let { saveProfilePicture(context, it) }
                },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(text = "Salvar Foto")
            }
        }
    }
}


fun saveProfilePicture(context: Context, uri: Uri) {
    val sharedPreferences = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().putString("profile_picture", uri.toString()).apply()
}

fun getSavedProfilePicture(context: Context): Uri? {
    val sharedPreferences = context.getSharedPreferences("profile_prefs", Context.MODE_PRIVATE)
    return sharedPreferences.getString("profile_picture", null)?.let { Uri.parse(it) }
}

@Preview(showBackground = true)
@Composable
fun PreviewProfilePictureScreen() {
    ProfilePictureScreen(onImageSelected = { uri -> println("Imagem selecionada: $uri") })
}
