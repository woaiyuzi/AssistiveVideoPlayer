package love.yuzi.compose.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import love.yuzi.compose.videoplayer.VideoPlayerState
import love.yuzi.video.model.Video

@Suppress("ModifierRequired", "UnstableCollections")
interface VideoPlayerScreenUi {
    @Composable
    fun TopBar(
        videos: List<Video>,
        currentVideo: Video?,
        onBack: () -> Unit,
        onRequestVideoManager: () -> Unit
    )

    @Composable
    fun BottomBar(
        videos: List<Video>,
        currentVideo: Video?,
        onRequestSwitchVideo: (Video) -> Unit
    )

    @Composable
    fun VideoPlayerItemWrapper(
        videos: List<Video>,
        currentVideo: Video,
        currentPlayerState: VideoPlayerState,
        onRequestProgramDetail: (String) -> Unit,
        modifier: Modifier = Modifier
    )
}