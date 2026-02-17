package love.yuzi.player

import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.compose.ContentFrame
import androidx.media3.ui.compose.SURFACE_TYPE_SURFACE_VIEW
import androidx.media3.ui.compose.state.rememberProgressStateWithTickInterval
import kotlinx.coroutines.flow.distinctUntilChanged
import timber.log.Timber

@Composable
fun VideoPlayPager(
    modifier: Modifier = Modifier,
    state: VideoPlayPagerState = rememberVideoPlayPagerState(),
    onAllPlayComplete: () -> Unit = {},
    onPlayError: (String) -> Unit = {}
) {
    val uris = state.uris

    val pagerState = rememberPagerState(
        initialPage = state.currentIndex,
        pageCount = { uris.size }
    )
    // 滚动稳定后的Pager索引
    var stablePageIndex by remember { mutableIntStateOf(pagerState.currentPage) }
    LaunchedEffect(pagerState, onAllPlayComplete) {
        snapshotFlow { pagerState.isScrollInProgress to pagerState.targetPage }
            .distinctUntilChanged()
            .collect { (inProgress, target) ->
                if (!inProgress) {
                    stablePageIndex = target
                    Timber.d("Scroll finished, stablePageIndex: $stablePageIndex")
                    if (stablePageIndex == pagerState.pageCount) {
                        onAllPlayComplete()
                    }
                }
            }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
    }

    // 滚动到VideoPlayPagerState中保存的currentIndex
    LaunchedEffect(state.currentIndex) {
        val targetPageIndex = state.currentIndex
        if (targetPageIndex in 0 until pagerState.pageCount
            && targetPageIndex != pagerState.currentPage
        ) {
            pagerState.animateScrollToPage(targetPageIndex)
        }
    }
}

@OptIn(UnstableApi::class)
@Composable
private fun VideoPage(
    uri: String,
    shouldPlay: Boolean,
    onProgressChange: (Long, Long) -> Unit,
    onPlayComplete: () -> Unit,
    onError: (String) -> Unit,
) {
    val context = LocalContext.current

    val playerHolder = remember(uri) {
        PlayerHolder(
            context = context,
            uri = uri,
            onPlayComplete = onPlayComplete,
            onError = onError
        )
    }

    val progressState = rememberProgressStateWithTickInterval(
        player = playerHolder.player,
        tickIntervalMs = 300
    )

    LaunchedEffect(progressState, onProgressChange) {
//        onProgressChange(progressState.currentPositionMs, progressState.durationMs)
    }

    DisposableEffect(playerHolder) {
        onDispose {
            playerHolder.releasePlayer()
        }
    }

    LaunchedEffect(shouldPlay) {
        playerHolder.shouldPlay = shouldPlay
        Timber.d("Should play: $shouldPlay with uri $uri")
    }

    ContentFrame(
        modifier = Modifier.fillMaxSize(),
        player = playerHolder.player,
        surfaceType = SURFACE_TYPE_SURFACE_VIEW,
        contentScale = ContentScale.Crop,
        keepContentOnReset = true
    )
}