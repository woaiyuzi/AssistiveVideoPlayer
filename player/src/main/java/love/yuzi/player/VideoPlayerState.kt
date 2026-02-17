package love.yuzi.player

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

@Composable
fun rememberVideoPlayerState(
    uri: String,
    onPlayComplete: (String) -> Unit,
    onError: (String) -> Unit,
    initialPosition: Long = 0L,
    playWhenReady: Boolean = true
): VideoPlayerState {
    val context = LocalContext.current
    return remember {
        VideoPlayerState(
            context = context,
            uri = uri,
            initialPosition = initialPosition,
            initialPlayWhenReady = playWhenReady,
            onPlayComplete = onPlayComplete,
            onError = onError
        )
    }
}

@Stable
class VideoPlayerState(
    val context: Context,
    val uri: String,
    val initialPosition: Long = 0L,
    val initialPlayWhenReady: Boolean = true,
    val onPlayComplete: (String) -> Unit = {},
    val onError: (String) -> Unit = {}
) {
    internal var holder: PlayerHolder = PlayerHolder(
        uri = uri,
        initialPosition = initialPosition,
        onPlayComplete = { onPlayComplete(uri) },
        onError = onError,
        context = context
    )


    val playWhenReady = holder?.player?.playWhenReady

    internal fun setup(holder: PlayerHolder) {
        this.holder = holder

    }
}

data class ProgressState(
    val currentPosition: Long,
    val duration: Long
)