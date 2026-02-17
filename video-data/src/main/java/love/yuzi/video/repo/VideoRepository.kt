package love.yuzi.video.repo

import love.yuzi.video.model.Video
import love.yuzi.video.source.VideoDataSource

class VideoRepository(
    private val dataSource: VideoDataSource
) {
    suspend fun insert(videos: List<Video>) {
        dataSource.insert(videos)
        videos.forEach {
            // Timber.d("Insert video: $it")
        }
    }

    fun observeFlow() = dataSource.observeFlow()

    suspend fun getByProgramTitle(programTitle: String) = dataSource.getByProgramTitle(programTitle)
}