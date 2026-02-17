package love.yuzi.avp.videoplayer.state

import androidx.compose.runtime.Immutable
import love.yuzi.video.model.Video

@Immutable
data class VideoPlayerUiState(
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = true
)