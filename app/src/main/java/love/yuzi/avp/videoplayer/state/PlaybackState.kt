package love.yuzi.avp.videoplayer.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import love.yuzi.compose.videoplayer.VideoPlayerState

@Stable
class PlaybackState internal constructor(
    initialVideoId: Long?,
    initialPosition: Long
) {
    var videoId by mutableStateOf(initialVideoId)
        private set

    private var playerState: VideoPlayerState? = null

    var currentPosition: Long = initialPosition
        get() = playerState?.currentPosition ?: field
        private set

    fun update(videoId: Long, playerState: VideoPlayerState) {
        this.videoId = videoId
        this.playerState = playerState
    }
}

@Suppress("ModifierRequired")
@Composable
fun rememberPlaybackState(
    initialVideoId: Long? = null,
    initialPosition: Long = 0L
) = remember {
    PlaybackState(
        initialVideoId = initialVideoId,
        initialPosition = initialPosition
    )
}