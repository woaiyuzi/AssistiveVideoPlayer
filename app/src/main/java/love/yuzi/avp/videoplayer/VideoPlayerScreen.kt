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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.zIndex
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import love.yuzi.avp.videoplayer.state.PlaybackState
import love.yuzi.avp.videoplayer.state.VideoPlayerUiState
import love.yuzi.compose.ui.VideoPlayerScreenUi
import love.yuzi.compose.videoplayer.VideoPlayerState
import love.yuzi.compose.videoplayer.rememberVideoPlayerState
import love.yuzi.video.model.Video
import timber.log.Timber

@Suppress("ModifierRequired")
@Composable
fun VideoPlayerScreen(
    playBackState: PlaybackState,
    isActive: Boolean,
    onRequestVideoManager: () -> Unit,
    onBack: () -> Unit,
    onRequestSwitchVideo: (Video) -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    viewModel: VideoPlayerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // proxy screen
    InternalVideoPlayerScreen(
        ui = viewModel.uiProvider,
        uiState = uiState,
        currentVideoState = viewModel.currentVideoState,
        playBackState = playBackState,
        isActive = isActive,
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
    currentVideoState: StateFlow<Video?>,
    playBackState: PlaybackState,
    isActive: Boolean,
    onVideoSelect: (Video) -> Unit,
    onRequestVideoManager: () -> Unit,
    onRequestSwitchVideo: (Video) -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    onBack: () -> Unit,
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
            currentVideoState = currentVideoState,
            playBackState = playBackState,
            isActive = isActive,
            onVideoSelect = onVideoSelect,
            onRequestVideoManager = onRequestVideoManager,
            onBack = onBack,
            onRequestProgramDetail = onRequestProgramDetail,
            onRequestSwitchVideo = onRequestSwitchVideo,
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
        Text(text = "还没有视频，点击去添加")
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
    currentVideoState: StateFlow<Video?>,
    playBackState: PlaybackState,
    isActive: Boolean,
    onVideoSelect: (Video) -> Unit,
    onRequestVideoManager: () -> Unit,
    onBack: () -> Unit,
    onRequestProgramDetail: (String) -> Unit,
    onRequestSwitchVideo: (Video) -> Unit
) {
    val videos = uiState.videos

    // main content
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // top bar
        Box(
            modifier = Modifier
                .zIndex(1f)
                .fillMaxWidth()
        ) {
            ui.TopBar(
                videos, currentVideoState,
                onBack = onBack,
                onRequestVideoManager = onRequestVideoManager
            )
        }

        // main container
        Box(modifier = Modifier.fillMaxSize()) {
            VideoPlayerPager(
                videos = videos,
                initialVideoId = playBackState.videoId,
                initialPosition = playBackState.currentPosition,
                isActive = isActive,
                onPageSelect = { video, playerState ->
                    onVideoSelect(video)
                    playBackState.update(video.id, playerState)
                }
            ) { video, playerState ->
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // item
                    ui.VideoPlayerItemWrapper(
                        modifier = Modifier.weight(1f),
                        videos = videos,
                        video = video,
                        playerState = playerState,
                        onRequestProgramDetail = onRequestProgramDetail
                    )

                    // bottomBar
                    ui.BottomBar(
                        videos, currentVideoState,
                        onRequestSwitchVideo = onRequestSwitchVideo
                    )
                }
            }
        }
    }
}

@Suppress("UnstableCollections")
@Composable
private fun VideoPlayerPager(
    videos: List<Video>,
    initialVideoId: Long?,
    initialPosition: Long,
    isActive: Boolean,
    onPageSelect: (Video, VideoPlayerState) -> Unit,
    itemContent: @Composable (Video, VideoPlayerState) -> Unit,
) {
    // --- 无限循环配置 ---
    val iterations = 1000

    // 3. 确定起始索引：如果 initialVideoId 为空或找不到，默认从 0 开始
    val realInitialIndex = remember(videos) {
        val index = videos.indexOfFirst { it.id == initialVideoId }
        if (index != -1) index else 0
    }

    val startIndex = (iterations / 2) * videos.size + realInitialIndex
    val isInitialPositionConsumed = remember { mutableStateOf(false) }

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

        // 4. 精准进度消耗：仅在启动时的那个绝对页码位置使用一次 initialPosition
        val positionForThisPage = remember(page) {
            if (page == startIndex && !isInitialPositionConsumed.value) {
                isInitialPositionConsumed.value = true
                initialPosition
            } else {
                0L
            }
        }

        val isCurrentPage = pagerState.settledPage == page

        val playerState = rememberVideoPlayerState(
            uri = video.uri,
            initialPosition = positionForThisPage,
            onPlayComplete = {
                scope.launch {
                    pagerState.animateScrollToPage(page + 1)
                }
            }
        )

        playerState.playWhenReady = isCurrentPage && isActive

        LaunchedEffect(isCurrentPage, onPageSelect) {
            if (isCurrentPage) {
                onPageSelect(video, playerState)
                Timber.d("Video page selected: $page")
            }
        }

        itemContent(video, playerState)
    }
}

@Composable
@Preview(name = "VideoFeedPlayer")
private fun VideoPlayerScreenPreview() {
    // VideoPlayerScreen()
}