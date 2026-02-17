package love.yuzi.avp.videoplayer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import love.yuzi.avp.videoplayer.state.VideoPlayerUiState
import love.yuzi.compose.ui.VideoPlayerScreenUi
import love.yuzi.video.model.Video
import love.yuzi.video.repo.VideoRepository

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    val uiProvider: VideoPlayerScreenUi,
    repo: VideoRepository
) : ViewModel() {
    val uiState: StateFlow<VideoPlayerUiState> =
        repo.observeFlow().map {
            VideoPlayerUiState(videos = it, isLoading = false)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            VideoPlayerUiState(isLoading = true)
        )

    private val _currentVideo = MutableStateFlow<Video?>(null)
    val currentVideoState = _currentVideo.asStateFlow()

    fun onVideoSelected(video: Video) {
        if (_currentVideo.value?.id != video.id) {
            _currentVideo.value = video
        }
    }
}