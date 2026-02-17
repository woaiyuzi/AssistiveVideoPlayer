package love.yuzi.video.resource

import love.yuzi.logger.WithTimberTest
import love.yuzi.video.resource.provider.VideoResourceProvider
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.io.File

class VideoRepositoryTest : WithTimberTest() {
    private val videosDir = File("videos")
    private val videoResourceProvider = FakeVideoResourceProvider()

    private val itemCount = 11

    @Before
    fun setUp() {
        createPrograms()
    }

    private fun createPrograms() {
        for (i in 0 until itemCount) {
            val programName = "Program $i"
            val programDir = File(videosDir, programName)
            programDir.mkdirs()

            File(programDir, "cover.jpg").createNewFile()

            createEpisodes(programName)
        }
    }

    private fun createEpisodes(programName: String) {
        for (i in 0 until itemCount) {
            File(videosDir, "$programName/第${i}集.mp4").createNewFile()
        }
    }

    @After
    fun tearDown() {
        videosDir.deleteRecursively()
        assert(!videosDir.exists())
    }

    @Test
    fun getPrograms() {
        val programs = videoResourceProvider.getPrograms()
        assert(programs.size == itemCount)
    }

    @Test
    fun getEpisodesByProgramName() {
        val episodes = videoResourceProvider.getEpisodesByProgramName("Program 0")
        assert(episodes.size == itemCount)
    }

}

class FakeVideoResourceProvider : VideoResourceProvider() {
    override val videosDir: File = File("videos")
}