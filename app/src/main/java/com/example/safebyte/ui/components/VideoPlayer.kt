package com.example.safebyte.ui.components

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
    videoUrl: String
) {
    val context = LocalContext.current

    // Use `remember` to hold the ExoPlayer instance
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(videoUrl))
        }
    }

    // Prepare ExoPlayer asynchronously to avoid blocking the main thread
    LaunchedEffect(exoPlayer) {
        exoPlayer.prepare()
    }

    // Ensure proper cleanup of ExoPlayer
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
