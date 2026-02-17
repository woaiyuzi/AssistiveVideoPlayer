package love.yuzi.player

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import timber.log.Timber

@Suppress("UnstableCollections")
@Composable
fun rememberVideoPlayPagerState(
    uris: List<String> = emptyList(),
    initialIndex: Int = 0,
    initialPosition: Long = 0L,
    initialDuration: Long = 0L,
    initialShouldPlay: Boolean = false
): VideoPlayPagerState {
    return remember {
        VideoPlayPagerState(
            initialUris = uris,
            initialIndex = initialIndex,
            initialPosition = initialPosition,
            initialDuration = initialDuration,
            initialShouldPlay = initialShouldPlay
        )
    }
}

class VideoPlayPagerState(
    initialUris: List<String> = emptyList(),
    initialIndex: Int = 0,
    initialPosition: Long = 0L,
    initialDuration: Long = 0L,
    initialShouldPlay: Boolean = false
) {
    internal var uris by mutableStateOf(initialUris)
        private set
    internal var currentIndex by mutableIntStateOf(initialIndex)
        private set
    var currentUri by mutableStateOf(uris.getOrNull(index = initialIndex))
        private set
    var currentPosition by mutableLongStateOf(initialPosition)
        private set
    var duration by mutableLongStateOf(initialDuration)
        private set
    internal var shouldPlay by mutableStateOf(initialShouldPlay)
        private set

    fun updateUris(uris: List<String>) {
        this.uris = uris
    }

    fun play(index: Int) {
        if (index in 0 until uris.size) {
            this.currentIndex = index
            this.currentUri = uris[index]
            Timber.d("Play by index $index with uri $currentUri")
        } else {
            Timber.e("Invalid index $index")
        }
    }

    fun seekTo(position: Long) {
        if (position in 0..duration) {
            this.currentPosition = position
            Timber.d("Seek to $position")
        } else {
            Timber.e("Invalid position $position")
        }
    }

    fun toggleShouldPlay(shouldPlay: Boolean) {
        this.shouldPlay = shouldPlay
        Timber.d("Toggle should play to $shouldPlay")
    }

    internal fun setDuration(duration: Long) {
        this.duration = duration
        Timber.d("Set duration to $duration")
    }
}