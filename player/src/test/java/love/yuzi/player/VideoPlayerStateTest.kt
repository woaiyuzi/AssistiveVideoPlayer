package love.yuzi.player

import love.yuzi.logger.WithTimberTest
import org.junit.Test

class VideoPlayerStateTest : WithTimberTest() {
    private val duration = 100000L
    private val uris = listOf("uri1", "uri2", "uri3")
    private val state = VideoPlayPagerState(initialUris = uris, initialDuration = duration)

    @Test
    fun getCurrentUri() {
        state.play(1)
        assert(state.currentUri == uris[1])
    }

    @Test
    fun setDuration() {
        state.setDuration(duration)
        assert(state.duration == duration)
    }

    @Test
    fun updatePositionSuccess() {
        val position = 9000L
        state.seekTo(position)
        assert(state.currentPosition == position)
    }

    @Test
    fun updatePositionInvalid() {
        val position = 999999999L
        state.seekTo(position)
        assert(state.currentPosition != position)
    }

    @Test
    fun toggleShouldPlay() {
        state.toggleShouldPlay(true)
        assert(state.shouldPlay)
    }
}