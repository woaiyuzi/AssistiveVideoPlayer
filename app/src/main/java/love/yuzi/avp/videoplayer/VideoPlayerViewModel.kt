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
import kotlinx.coroutines.launch
import love.yuzi.avp.videoplayer.state.VideoPlayerUiState
import love.yuzi.compose.ui.VideoPlayerScreenUi
import love.yuzi.compose.videoplayer.VideoPlayerState
import love.yuzi.video.model.Video
import love.yuzi.video.repo.VideoRepository

@HiltViewModel
class VideoPlayerViewModel @Inject constructor(
    val uiProvider: VideoPlayerScreenUi,
    private val repo: VideoRepository
) : ViewModel() {
    val uiState: StateFlow<VideoPlayerUiState> =
        repo.observeFlow().map {
            VideoPlayerUiState(videos = it, isLoading = false)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            VideoPlayerUiState(isLoading = true)
        )

    private val _currentVideoState = MutableStateFlow<Video?>(null)
    val currentVideoState: StateFlow<Video?> = _currentVideoState.asStateFlow()

    private var _currentPlayerState: VideoPlayerState? = null

    fun init(initialVideoId: Long?) {
        viewModelScope.launch {
            val video = repo.getVideoById(initialVideoId ?: 1L)
            if (video != null && video.id != _currentVideoState.value?.id) {
                _currentVideoState.value = video
            }
        }
    }

    fun onVideoSelected(video: Video, playerState: VideoPlayerState?) {
        saveLastVideoState()
        init(video.id)
        _currentPlayerState = playerState
    }

    fun saveLastVideoState() {
        viewModelScope.launch {
            if (_currentVideoState.value != null && _currentPlayerState != null) {
                repo.updatePosition(
                    _currentVideoState.value!!.id,
                    _currentPlayerState!!.currentPosition
                )
            }
        }
    }
}