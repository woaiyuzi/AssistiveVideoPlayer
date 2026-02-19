package love.yuzi.compose.videoplayer

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import timber.log.Timber

@Stable
class VideoPlayerState(
    context: Context,
    val uri: String,
    initialPosition: Long
) {
    val player: ExoPlayer = ExoPlayer.Builder(context).build()

    init {
        player.setMediaItem(MediaItem.fromUri(uri))
        Timber.d("Player Prepared: $uri at $initialPosition")
    }

    val currentPosition get() = player.currentPosition

    var playWhenReady: Boolean
        get() = player.playWhenReady
        set(value) {
            player.playWhenReady = value
        }

    fun seekTo(position: Long) {
        player.seekTo(position)
    }

    fun prepare() {
        player.prepare()
    }
}

@Composable
fun rememberVideoPlayerState(
    uri: String,
    initialPosition: Long = 0L,
    onPlayComplete: () -> Unit = { }
): VideoPlayerState {
    val context = LocalContext.current.applicationContext

    // 使用 uri 作为 key 确保换视频时重建 state
    val state = remember(uri) {
        VideoPlayerState(context, uri, initialPosition)
    }

    // 使用 state 作为 key，确保监听器和释放逻辑与当前的 player 实例绑定
    DisposableEffect(state, onPlayComplete) {
        val listener = object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_ENDED) {
                    onPlayComplete()
                    state.player.seekTo(0L)
                    state.player.playWhenReady = true
                }
            }
        }
        state.player.addListener(listener)

        onDispose {
            state.player.removeListener(listener)
            state.player.release()
            Timber.d("Video player released, uri=${state.uri}")
        }
    }

    return state
}