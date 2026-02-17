package love.yuzi.video.source

import kotlinx.coroutines.flow.Flow
import love.yuzi.video.model.Video

interface VideoDataSource {
    suspend fun insert(videos: List<Video>)

    fun observeFlow(): Flow<List<Video>>

    suspend fun getByProgramTitle(programTitle: String): List<Video>
}