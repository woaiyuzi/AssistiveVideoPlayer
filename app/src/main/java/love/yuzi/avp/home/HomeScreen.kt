package love.yuzi.avp.home

import androidx.compose.runtime.Composable
import love.yuzi.avp.videoplayer.VideoPlayerScreen
import love.yuzi.video.model.Video

@Suppress("ModifierRequired")
@Composable
fun HomeScreen(
    initialVideoId: Long?,
    onBack: () -> Unit,
    onStop: (Video?) -> Unit,
    onRequestSwitchVideo: (Video) -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    onRequestVideoManager: () -> Unit
) {
    VideoPlayerScreen(
        onRequestVideoManager = onRequestVideoManager,
        onBack = onBack,
        onStop = onStop,
        onRequestSwitchVideo = onRequestSwitchVideo,
        onRequestProgramDetail = onRequestProgramDetail,
        initialVideoId = initialVideoId,
    )
}