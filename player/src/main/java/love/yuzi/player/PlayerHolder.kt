package love.yuzi.player

import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import timber.log.Timber

internal class PlayerHolder(
    context: Context,
    private val uri: String,
    private val initialPosition: Long = 0L,
    private val onPlayComplete: (() -> Unit),
    private val onError: ((String) -> Unit)
) {

    private val listener = object : Player.Listener {
        override fun onPlayerError(error: PlaybackException) {
            onError(error.localizedMessage ?: "Unknown error")
            Timber.e(error, "Player error with uri $uri")
        }

        override fun onPlaybackStateChanged(state: Int) {
            if (state == Player.STATE_ENDED) {
                onPlayComplete()
                Timber.d("Play complete with uri $uri")
            }
        }
    }

    val player: ExoPlayer = ExoPlayer.Builder(context).build().apply {
        repeatMode = Player.REPEAT_MODE_ONE
        addListener(listener)
        setMediaItem(MediaItem.fromUri(uri))
        seekTo(initialPosition)
        prepare()
        Timber.d("Prepare player with uri $uri")
    }

    var playWhenReady: Boolean = false
        set(value) {
            field = value
            player.playWhenReady = value
        }

    fun releasePlayer() {
        player.release()
        Timber.d("Release player with uri $uri")
    }
}