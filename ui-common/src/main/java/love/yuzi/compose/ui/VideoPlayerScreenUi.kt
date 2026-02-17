package love.yuzi.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.StateFlow
import love.yuzi.compose.videoplayer.VideoPlayerState
import love.yuzi.video.model.Video

@Suppress("ModifierRequired", "UnstableCollections")
interface VideoPlayerScreenUi {
    @Composable
    fun TopBar(
        videos: List<Video>,
        currentVideoState: StateFlow<Video?>,
        onBack: () -> Unit,
        onRequestVideoManager: () -> Unit
    )

    @Composable
    fun BottomBar(
        videos: List<Video>,
        currentVideoState: StateFlow<Video?>,
        onRequestSwitchVideo: (Video) -> Unit
    )

    @Composable
    fun VideoPlayerItemWrapper(
        videos: List<Video>,
        video: Video,
        playerState: VideoPlayerState,
        onRequestProgramDetail: (String) -> Unit,
        modifier: Modifier = Modifier
    )
}