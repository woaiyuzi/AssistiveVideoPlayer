package love.yuzi.video.repo

import love.yuzi.video.model.Video
import love.yuzi.video.source.VideoDataSource

class VideoRepository(
    private val dataSource: VideoDataSource
) {
    suspend fun insert(videos: List<Video>) {
        dataSource.insert(videos)
    }

    fun observeFlow() = dataSource.observeFlow()

    suspend fun getByProgramTitle(programTitle: String) = dataSource.getByProgramTitle(programTitle)

    suspend fun updatePosition(videoId: Long, position: Long) =
        dataSource.updatePosition(videoId, position)

    suspend fun getVideoById(id: Long) = dataSource.getVideoById(id)
}