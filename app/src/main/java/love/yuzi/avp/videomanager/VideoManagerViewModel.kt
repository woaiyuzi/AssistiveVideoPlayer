package love.yuzi.avp.videomanager

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import love.yuzi.video.model.Video
import love.yuzi.video.repo.VideoRepository
import love.yuzi.video.resource.provider.VideoResourceProvider
import javax.inject.Inject

@HiltViewModel
class VideoManagerViewModel @Inject constructor(
    private val repo: VideoRepository,
    private val resProvider: VideoResourceProvider
) : ViewModel() {

    private val _videosFlow = repo.observeFlow()

    val uiState: StateFlow<VideoManagerUiState> =
        _videosFlow.map {
            VideoManagerUiState(videos = it, isLoading = false)
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            VideoManagerUiState(isLoading = true)
        )

    fun onReflushVideoList() {
        val programs = resProvider.getPrograms()
        programs.forEach { program ->
            val episodes = resProvider.getEpisodesByProgramName(program.name)
            val videos = episodes.map { episode ->
                Video(
                    title = episode.title,
                    uri = episode.uri,
                    programTitle = program.name
                )
            }
            viewModelScope.launch {
                repo.insert(videos)
            }
        }
    }
}