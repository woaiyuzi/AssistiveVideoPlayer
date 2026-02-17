package love.yuzi.avp.videomanager

import androidx.compose.runtime.Immutable
import love.yuzi.video.model.Video

@Immutable
data class VideoManagerUiState(
    val videos: List<Video> = emptyList(),
    val isLoading: Boolean = false
)