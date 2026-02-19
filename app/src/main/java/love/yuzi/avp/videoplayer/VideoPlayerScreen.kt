package love.yuzi.avp.videoplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.LifecycleStartEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import love.yuzi.avp.videoplayer.state.VideoPlayerUiState
import love.yuzi.compose.foundation.WhiteText
import love.yuzi.compose.ui.VideoPlayerScreenUi
import love.yuzi.compose.videoplayer.VideoPlayerState
import love.yuzi.compose.videoplayer.rememberVideoPlayerState
import love.yuzi.video.model.Video
import timber.log.Timber

@Suppress("ModifierRequired")
@Composable
fun VideoPlayerScreen(
    onRequestVideoManager: () -> Unit,
    onBack: () -> Unit,
    onStop: (Video?) -> Unit,
    onRequestSwitchVideo: (Video) -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    initialVideoId: Long? = null,
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    LaunchedEffect(viewModel) {
        viewModel.init(initialVideoId)
    }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currentVideo by viewModel.currentVideoState.collectAsStateWithLifecycle()

    var shouldPlay by remember { mutableStateOf(false) }

    LifecycleStartEffect(Unit) {
        shouldPlay = true
        onStopOrDispose {
            viewModel.saveLastVideoState()
            onStop(currentVideo)
            shouldPlay = false
        }
    }

    // proxy screen
    InternalVideoPlayerScreen(
        ui = viewModel.uiProvider,
        uiState = uiState,
        currentVideo = currentVideo,
        shouldPlay = shouldPlay,
        onVideoSelect = viewModel::onVideoSelected,
        onRequestVideoManager = onRequestVideoManager,
        onRequestSwitchVideo = onRequestSwitchVideo,
        onRequestProgramDetail = onRequestProgramDetail,
        onBack = onBack
    )
}

// proxy screen for preview
@Composable
private fun InternalVideoPlayerScreen(
    ui: VideoPlayerScreenUi,
    uiState: VideoPlayerUiState,
    currentVideo: Video?,
    onVideoSelect: (Video, VideoPlayerState?) -> Unit,
    onRequestVideoManager: () -> Unit,
    onRequestSwitchVideo: (Video) -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    onBack: () -> Unit,
    shouldPlay: Boolean,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (uiState.isLoading) {
            LoadingContent()
            return
        }

        if (uiState.videos.isEmpty()) {
            EmptyVideoContent(onRequestVideoManager)
            return
        }

        MainContent(
            ui = ui,
            uiState = uiState,
            currentVideo = currentVideo,
            shouldPlay = shouldPlay,
            onVideoSelect = onVideoSelect,
            onRequestVideoManager = onRequestVideoManager,
            onBack = onBack,
            onRequestProgramDetail = onRequestProgramDetail,
            onRequestSwitchVideo = onRequestSwitchVideo
        )
    }
}

@Composable
private fun EmptyVideoContent(onRequestVideoManager: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .clickable(
                onClick = onRequestVideoManager
            ),
        contentAlignment = Alignment.Center
    ) {
        WhiteText(text = "还没有视频，点击去添加")
    }
}

@Composable
private fun LoadingContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
private fun MainContent(
    ui: VideoPlayerScreenUi,
    uiState: VideoPlayerUiState,
    currentVideo: Video?,
    onVideoSelect: (Video, VideoPlayerState?) -> Unit,
    onRequestVideoManager: () -> Unit,
    onBack: () -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    onRequestSwitchVideo: (Video) -> Unit,
    shouldPlay: Boolean,
) {
    val videos = uiState.videos

    // main content
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // main container
        Box(modifier = Modifier.fillMaxSize()) {
            VideoPlayerPager(
                videos = videos,
                currentVideo = currentVideo,
                shouldPlay = shouldPlay,
                onPageSelect = { video, playerState ->
                    onVideoSelect(video, playerState)
                }
            ) { video, playerState ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // item
                    ui.VideoPlayerItemWrapper(
                        modifier = Modifier.weight(1f),
                        videos = videos,
                        currentVideo = video,
                        currentPlayerState = playerState,
                        onRequestProgramDetail = onRequestProgramDetail
                    )

                    // bottomBar
                    ui.BottomBar(
                        videos = videos,
                        currentVideo = currentVideo,
                        onRequestSwitchVideo = onRequestSwitchVideo
                    )
                }
            }
        }

        // top bar
        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
        ) {
            ui.TopBar(
                videos = videos,
                currentVideo = currentVideo,
                onBack = onBack,
                onRequestVideoManager = onRequestVideoManager
            )
        }
    }
}

@Suppress("UnstableCollections")
@Composable
private fun VideoPlayerPager(
    videos: List<Video>,
    currentVideo: Video?,
    onPageSelect: (Video, VideoPlayerState?) -> Unit,
    shouldPlay: Boolean,
    itemContent: @Composable (Video, VideoPlayerState) -> Unit
) {

    // --- 无限循环配置 ---
    val iterations = 1000

    // 3. 确定起始索引：如果 initialVideoId 为空或找不到，默认从 0 开始
    val realInitialIndex = remember(videos) {
        val index = videos.indexOfFirst { it.id == currentVideo?.id }
        if (index != -1) index else 0
    }

    val startIndex = (iterations / 2) * videos.size + realInitialIndex

    val pagerState = rememberPagerState(
        initialPage = startIndex,
        pageCount = { videos.size * iterations }
    )

    val scope = rememberCoroutineScope()

    VerticalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxSize()
            .zIndex(0f),
        beyondViewportPageCount = 1,
        // 使用 page 确保 key 的全局唯一性
        key = { page -> "${videos[page % videos.size].id}_$page" }
    ) { page ->
        val realIndex = page % videos.size
        val video = videos[realIndex]

        val isCurrentPage = pagerState.settledPage == page

        val playerState = rememberVideoPlayerState(
            uri = video.uri,
            initialPosition = currentVideo?.position ?: 0L,
            onPlayComplete = {
                scope.launch {
                    pagerState.animateScrollToPage(page + 1)
                }
            }
        )

        LaunchedEffect(isCurrentPage, onPageSelect) {
            if (isCurrentPage) {
                onPageSelect(video, playerState)
                playerState.seekTo(currentVideo?.position ?: 0L)
                playerState.prepare()
                Timber.d("Video page selected: $page")
            }
        }

        playerState.playWhenReady = isCurrentPage && shouldPlay

        itemContent(video, playerState)
    }
}

@Composable
@Preview(name = "VideoFeedPlayer")
private fun VideoPlayerScreenPreview() {
    // VideoPlayerScreen()
}