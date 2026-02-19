package love.yuzi.compose.landscape

import androidx.annotation.OptIn
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MovieFilter
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.buttons.PlayPauseButton
import love.yuzi.compose.foundation.ActionIcon
import love.yuzi.compose.foundation.ProgramTitle
import love.yuzi.compose.foundation.ProgressBar
import love.yuzi.compose.foundation.VideoPlayerTopBar
import love.yuzi.compose.foundation.WhiteIcon
import love.yuzi.compose.ui.VideoPlayerScreenUi
import love.yuzi.compose.videoplayer.VideoPlayer
import love.yuzi.compose.videoplayer.VideoPlayerState
import love.yuzi.video.model.Video

@Suppress("UnstableCollections", "ModifierRequired")
class LandscapeVideoPlayerScreenUi : VideoPlayerScreenUi {
    @Composable
    override fun TopBar(
        videos: List<Video>,
        currentVideo: Video?,
        onBack: () -> Unit,
        onRequestVideoManager: () -> Unit
    ) {
        currentVideo?.let {
            VideoPlayerTopBar(
                onBack = onBack, title = currentVideo.title
            ) {
                ActionIcon(
                    imageVector = Icons.Rounded.MovieFilter,
                    contentDescription = "视频管理",
                    onClick = onRequestVideoManager,
                )
            }
        }
    }

    @Composable
    override fun BottomBar(
        videos: List<Video>,
        currentVideo: Video?,
        onRequestSwitchVideo: (Video) -> Unit
    ) {
    }

    @OptIn(UnstableApi::class)
    @Suppress("ModifierDefaultValue")
    @Composable
    override fun VideoPlayerItemWrapper(
        videos: List<Video>,
        currentVideo: Video,
        currentPlayerState: VideoPlayerState,
        onRequestProgramDetail: (String) -> Unit,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = {
                    currentPlayerState.playWhenReady = !(currentPlayerState.playWhenReady)
                }),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                VideoPlayer(
                    state = currentPlayerState,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            VideoPlayerItemOverlay(
                programTitle = currentVideo.programTitle,
                player = currentPlayerState.player,
                onRequestProgramDetail = { onRequestProgramDetail(currentVideo.programTitle) },
            )

            PlayPauseButton(currentPlayerState.player) {
                if (showPlay) {
                    WhiteIcon(
                        imageVector = Icons.Rounded.PlayArrow,
                        size = 72.dp
                    )
                }
            }
        }
    }
}

@Composable
private fun BoxScope.VideoPlayerItemOverlay(
    programTitle: String, player: ExoPlayer, onRequestProgramDetail: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(horizontal = 16.dp, vertical = 4.dp)
            .align(
                Alignment.BottomStart
            ),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(
            space = 4.dp,
            alignment = Alignment.Bottom
        )
    ) {
        ProgramTitle(
            title = programTitle, onClick = onRequestProgramDetail
        )

        ProgressBar(player = player)
    }
}