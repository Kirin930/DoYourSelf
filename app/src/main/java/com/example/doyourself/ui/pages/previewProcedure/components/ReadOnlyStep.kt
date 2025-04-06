package com.example.doyourself.ui.pages.previewProcedure.components

import android.widget.FrameLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import coil.compose.rememberAsyncImagePainter

@Composable
fun ReadOnlyStep(
    stepNumber: Int,
    blocks: List<Pair<String, String>> // e.g. [("text", "Hello"), ("image", "contentUri"), ...]
) {
    Column {
        Text("Step $stepNumber", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(8.dp))
        blocks.forEach { (type, content) ->
            when (type) {
                "text" -> ReadOnlyTextBlock(content)
                "image" -> ReadOnlyImageBlock(content)
                "video" -> ReadOnlyVideoBlock(content)
                else -> Text("[Unsupported block]")
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
fun ReadOnlyTextBlock(text: String) {
    // Just show text in a normal Text composable
    Text(text, style = MaterialTheme.typography.bodyLarge)
}

@Composable
fun ReadOnlyImageBlock(uriString: String) {
    // If you want, you can load the image with Coil:
    // (same approach as your ImageBlockView, but no "Pick image" button)
    // For simplicity:
    Text("Image block (URI: $uriString)")
    Image(
        painter = rememberAsyncImagePainter(uriString),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
    )
}

@Composable
fun ReadOnlyVideoBlock(uriString: String) {
    // If you want a video player, you can embed a read-only ExoPlayer
    // For simplicity:
    val context = LocalContext.current
    Text("Video block (URI: $uriString)")
    val exoPlayer = remember(uriString) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(uriString))
            prepare()
            playWhenReady = false
        }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    600
                )
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
