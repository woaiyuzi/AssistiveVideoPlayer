package love.yuzi.compose.videoplayer

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW

@Composable
fun VideoPlayer(
    state: VideoPlayerState,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        ContentFrame(
            player = state.player,
            modifier = Modifier.fillMaxSize(),
            surfaceType = SURFACE_TYPE_SURFACE_VIEW,
            contentScale = contentScale,
            keepContentOnReset = false
        )
    }
}

@Preview(name = "VideoPlayer")
@Composable
private fun PreviewVideoPlayer() {
    val uri =
        "https://sf1-cdn-tos.huoshanstatic.com/obj/media-fe/xgplayer_doc_video/mp4/xgplayer-demo-360p.mp4"
    val initialPosition = 0L

    val state = rememberVideoPlayerState(
        uri = uri,
        initialPosition = initialPosition
    )

    LifecycleStartEffect(state) {
        state.player.playWhenReady = true

        onStopOrDispose {
            state.player.playWhenReady = false
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                onClick = {
                    state.player.playWhenReady = !state.player.playWhenReady
                }
            )
    ) {
        VideoPlayer(
            state = state,
            contentScale = ContentScale.Fit
        )
    }
}