package com.example.safebyte.ui.screens

import android.graphics.Bitmap
import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter

// Classe de dados para representar o usuário
data class User(
    val name: String,
    val email: String,
    val allergies: List<String>
)

// Função que gera um Bitmap contendo o QR code a partir do texto informado
fun generateQrCode(text: String, size: Int = 512): Bitmap {
    val writer = QRCodeWriter()
    val bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, size, size)
    val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    for (x in 0 until size) {
        for (y in 0 until size) {
            bmp.setPixel(x, y, if (bitMatrix.get(x, y)) Color.BLACK else Color.WHITE)
        }
    }
    return bmp
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScreen(navController: NavController, user: User) {
    val qrData = "Nome: ${user.name}\nEmail: ${user.email}\nAlergias: ${user.allergies.joinToString(separator = ", ")}"
    val qrBitmap = remember(qrData) { generateQrCode(qrData) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("QR Code do Usuário") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Voltar")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                bitmap = qrBitmap.asImageBitmap(),
                contentDescription = "QR Code",
                modifier = Modifier.size(256.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = qrData, style = MaterialTheme.typography.bodyMedium)
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = { navController.navigate("qr_scanner") }) {
                Text("Ler QR Code")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun QrCodeScreenPreview() {
    val navController = rememberNavController()
    val sampleUser = User(
        name = "João Silva",
        email = "joao.silva@example.com",
        allergies = listOf("Amendoim", "Lactose")
    )
    QrCodeScreen(navController = navController, user = sampleUser)
}
