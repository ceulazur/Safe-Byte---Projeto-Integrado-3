package com.example.safebyte.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Size
import android.view.ViewGroup
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.google.common.util.concurrent.ListenableFuture
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScannerScreen(navController: NavController, onQrCodeScanned: (String) -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraProviderFuture: ListenableFuture<ProcessCameraProvider> =
        ProcessCameraProvider.getInstance(context)
    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }
    var scannedResult by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Scanner de QR Code") },
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
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                AndroidView(
                    factory = { ctx ->
                        PreviewView(ctx).apply {
                            layoutParams = ViewGroup.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            )
                        }
                    },
                    modifier = Modifier.size(300.dp)
                ) { previewView ->
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }
                        val imageAnalyzer = ImageAnalysis.Builder()
                            .setTargetResolution(Size(1280, 720))
                            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                            .build()
                            .also {
                                it.setAnalyzer(cameraExecutor) { imageProxy ->
                                    coroutineScope.launch(Dispatchers.IO) {
                                        val result = scanQrCode(imageProxy)
                                        result?.let {
                                            scannedResult = it
                                            onQrCodeScanned(it)
                                            navController.navigate("qr_report/${Uri.encode(it)}")
                                        }
                                    }
                                }
                            }
                        val cameraSelector = androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA
                        cameraProvider.unbindAll()
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner, cameraSelector, preview, imageAnalyzer
                        )
                    }, ContextCompat.getMainExecutor(context))
                }
            } else {
                Text("Permissão de câmera não concedida.")
            }
            Spacer(modifier = Modifier.height(16.dp))
            scannedResult?.let {
                Text(text = "QR Code: $it", style = MaterialTheme.typography.bodyMedium)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navController.navigate("qr_code_screen") }) {
                Text("Gerar QR Code")
            }
        }
    }
}

private fun scanQrCode(imageProxy: ImageProxy): String? {
    val buffer = imageProxy.planes[0].buffer
    val bytes = ByteArray(buffer.remaining())
    buffer.get(bytes)
    val width = imageProxy.width
    val height = imageProxy.height
    val source = PlanarYUVLuminanceSource(bytes, width, height, 0, 0, width, height, false)
    val binaryBitmap = BinaryBitmap(HybridBinarizer(source))
    return try {
        val result = MultiFormatReader().decode(binaryBitmap)
        imageProxy.close()
        result.text
    } catch (e: Exception) {
        imageProxy.close()
        null
    }
}
@Preview(showBackground = true)
@Composable
fun PreviewQrCodeScannerScreen() {
    QrCodeScannerScreen(
        navController = rememberNavController(),
        onQrCodeScanned = { scannedResult -> }
    )
}
