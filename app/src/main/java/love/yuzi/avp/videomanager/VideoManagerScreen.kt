package love.yuzi.avp.videomanager

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import love.yuzi.avp.ui.theme.AssistiveVideoPlayerTheme

@Suppress("ModifierRequired")
@Composable
fun VideoManagerScreen(
    onBack: () -> Unit,
    viewModel: VideoManagerViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    VideoManagerScreen(
        uiState = uiState,
        onBack = onBack,
        onReflushVideoList = viewModel::onReflushVideoList
    )
}

@Composable
private fun VideoManagerScreen(
    uiState: VideoManagerUiState,
    onBack: () -> Unit,
    onReflushVideoList: () -> Unit
) {
    Scaffold(
        topBar = {
            TopBar(
                onBack = onBack,
                onReflushVideoList = onReflushVideoList
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "共${uiState.videos.size}个视频"
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    onBack: () -> Unit,
    onReflushVideoList: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = "视频管理",
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onReflushVideoList) {
                Icon(
                    imageVector = Icons.Filled.Refresh,
                    contentDescription = "Refresh"
                )
            }
        }
    )
}

@Composable
@Preview(name = "VideoManager")
private fun VideoManagerScreenPreview() {
    AssistiveVideoPlayerTheme {
        VideoManagerScreen(
            uiState = VideoManagerUiState(),
            onBack = {},
            onReflushVideoList = {}
        )
    }
}