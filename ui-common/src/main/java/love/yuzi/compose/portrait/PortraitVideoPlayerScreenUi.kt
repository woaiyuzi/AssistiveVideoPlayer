package love.yuzi.compose.portrait

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material.icons.rounded.MovieFilter
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.buttons.PlayPauseButton
import kotlinx.coroutines.flow.StateFlow
import love.yuzi.compose.foundation.ActionIcon
import love.yuzi.compose.foundation.ProgramTitle
import love.yuzi.compose.foundation.ProgressBar
import love.yuzi.compose.foundation.VideoPlayerTopBar
import love.yuzi.compose.foundation.WhiteIcon
import love.yuzi.compose.foundation.WhiteText
import love.yuzi.compose.ui.VideoPlayerScreenUi
import love.yuzi.compose.videoplayer.VideoPlayer
import love.yuzi.compose.videoplayer.VideoPlayerState
import love.yuzi.video.model.Video

@Suppress("UnstableCollections", "ModifierRequired")
class PortraitVideoPlayerScreenUi : VideoPlayerScreenUi {
    @Composable
    override fun TopBar(
        videos: List<Video>,
        currentVideoState: StateFlow<Video?>,
        onBack: () -> Unit,
        onRequestVideoManager: () -> Unit
    ) {
        val video = currentVideoState.collectAsStateWithLifecycle().value

        video?.let {
            VideoPlayerTopBar(
                onBack = onBack, title = video.title
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
        currentVideoState: StateFlow<Video?>,
        onRequestSwitchVideo: (Video) -> Unit
    ) {
        val video = currentVideoState.collectAsStateWithLifecycle().value

        if (video != null) {
            VideoSwitch(
                videos = videos, video = video, onRequestSwitchVideo = onRequestSwitchVideo
            )
        }
    }

    @OptIn(UnstableApi::class)
    @Suppress("ModifierDefaultValue")
    @Composable
    override fun VideoPlayerItemWrapper(
        videos: List<Video>,
        video: Video,
        playerState: VideoPlayerState,
        onRequestProgramDetail: (String) -> Unit,
        modifier: Modifier
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .clickable(onClick = { playerState.playWhenReady = !(playerState.playWhenReady) }),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.TopStart
            ) {
                VideoPlayer(
                    state = playerState,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                )
            }

            VideoPlayerItemOverlay(
                programTitle = video.programTitle,
                player = playerState.player,
                onRequestProgramDetail = { onRequestProgramDetail(video.programTitle) },
            )

            PlayPauseButton(playerState.player) {
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
            ), horizontalAlignment = Alignment.Start, verticalArrangement = Arrangement.spacedBy(
            space = 16.dp, alignment = Alignment.Bottom
        )
    ) {
        ProgramTitle(
            title = programTitle, onClick = onRequestProgramDetail
        )

        ProgressBar(player = player)
    }
}

@Suppress("UnstableCollections")
@Composable
private fun VideoSwitch(
    videos: List<Video>, video: Video?, onRequestSwitchVideo: (Video) -> Unit
) {

    val currentProgramVideoCount =
        remember(videos, video) { videos.filter { it.programTitle == video?.programTitle }.size }

    if (video != null) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .clickable(onClick = { onRequestSwitchVideo(video) })
                .padding(vertical = 4.dp, horizontal = 16.dp)
                .padding(bottom = 4.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.Gray)
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                WhiteText(
                    text = "选集·全${currentProgramVideoCount}集"
                )

                WhiteIcon(
                    imageVector = Icons.Rounded.KeyboardArrowUp
                )
            }
        }
    }
}