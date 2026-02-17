package love.yuzi.avp.home

import androidx.compose.runtime.Composable
import love.yuzi.avp.videoplayer.VideoPlayerScreen
import love.yuzi.avp.videoplayer.state.PlaybackState
import love.yuzi.video.model.Video

@Suppress("ModifierRequired")
@Composable
fun HomeScreen(
    playbackState: PlaybackState,
    isActive: Boolean,
    onBack: () -> Unit,
    onRequestSwitchVideo: (Video) -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    onRequestVideoManager: () -> Unit
) {
    VideoPlayerScreen(
        playBackState = playbackState,
        isActive = isActive,
        onRequestVideoManager = onRequestVideoManager,
        onBack = onBack,
        onRequestSwitchVideo = onRequestSwitchVideo,
        onRequestProgramDetail = onRequestProgramDetail,
    )
}