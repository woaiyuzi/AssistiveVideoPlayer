package love.yuzi.video.source

import kotlinx.coroutines.flow.map
import love.yuzi.video.dao.VideoDao
import love.yuzi.video.model.Video
import love.yuzi.video.model.toEntities
import love.yuzi.video.model.toModels
import love.yuzi.video.model.toVideo

class LocalVideoDataSource(
    private val dao: VideoDao
) : VideoDataSource {
    override suspend fun insert(videos: List<Video>) = dao.insert(videos.toEntities())

    override fun observeFlow() = dao.observeFlow().map { it.toModels() }

    override suspend fun getByProgramTitle(programTitle: String) =
        dao.getByProgramTitle(programTitle).toModels()

    override suspend fun updatePosition(videoId: Long, position: Long) =
        dao.updatePosition(videoId, position)

    override suspend fun getVideoById(id: Long) = dao.getVideoById(id)?.toVideo()
}