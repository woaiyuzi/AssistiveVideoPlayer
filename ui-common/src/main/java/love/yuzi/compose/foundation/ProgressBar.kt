package love.yuzi.compose.foundation

import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.compose.indicators.TimeText

@OptIn(UnstableApi::class)
@Composable
fun ProgressBar(player: ExoPlayer, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.wrapContentWidth()
    ) {
        TimeText(player) {
            val currentPosition = Util.getStringForTime(this.currentPositionMs)
            val duration = Util.getStringForTime(this.durationMs)
            WhiteText("$currentPosition / $duration")
        }
    }
}