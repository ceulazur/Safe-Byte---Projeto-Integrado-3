package com.example.safebyte.ui.components

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun VideoPlayer(
    modifier: Modifier = Modifier,
    videoUrl: String?
) {
    Log.d("VideoPlayer", "Video URL: $videoUrl")
    val context = LocalContext.current

    // Use `remember` para criar o ExoPlayer
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build()
    }

    // Atualiza o media item sempre que a URL muda
    LaunchedEffect(videoUrl) {
        if (videoUrl != null) {
            val mediaItem = MediaItem.fromUri(videoUrl)
            exoPlayer.setMediaItem(mediaItem)
            exoPlayer.prepare()
        }
    }

    // Liberar recursos ao sair da tela
    DisposableEffect(exoPlayer) {
        onDispose {
            exoPlayer.release()
        }
    }

    AndroidView(
        factory = { PlayerView(context).apply { player = exoPlayer } },
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 200.dp)
    )
}

