package love.yuzi.video.repo

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import love.yuzi.logger.WithTimberTest
import love.yuzi.video.model.Video
import love.yuzi.video.source.VideoDataSource
import org.junit.Before
import org.junit.Test

class VideoRepositoryTest : WithTimberTest() {
    private val repo = VideoRepository(FakeVideoDataSource())

    @Before
    fun setup() {
        insert()
    }

    fun insert() = runTest {
        val videos = mutableListOf<Video>()
        var baseId = 0
        for (i in 0 until 4) {
            val programName = "Program_$i"

            for (j in 0 until 10) {
                val video = Video(
                    id = baseId++.toLong(),
                    title = "Video_$j",
                    uri = "uri_$j",
                    programTitle = programName
                )
                videos += video
            }
        }
        repo.insert(videos)
    }

    @Test
    fun testGetByProgramTitle() = runTest {
        val videos = repo.getByProgramTitle("Program_0")
        assert(videos.size == 10)
    }
}

class FakeVideoDataSource() : VideoDataSource {
    private val videos = mutableListOf<Video>()

    override suspend fun insert(videos: List<Video>) {
        this.videos += videos
    }

    override fun observeFlow(): Flow<List<Video>> {
        return flowOf(videos)
    }

    override suspend fun getByProgramTitle(programTitle: String): List<Video> {
        return videos.filter { it.programTitle == programTitle }
    }
}