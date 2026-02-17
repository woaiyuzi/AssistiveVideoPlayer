package love.yuzi.compose.foundation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MovieFilter
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerTopBar(
    onBack: () -> Unit,
    title: String,
    modifier: Modifier = Modifier,
    actions: @Composable () -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 32.dp
        )
    ) {
        VideoTitle(
            title = title,
            modifier = Modifier
                .weight(1f),
            onBack = onBack
        )

        Row(
            horizontalArrangement = Arrangement.spacedBy(32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            actions()
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0x000000)
@Composable
private fun VideoPlayerTopBarPreview() {
    VideoPlayerTopBar(
        { /* TODO */ },
        "Preview",
        Modifier,
        {
            ActionIcon(
                imageVector = Icons.Rounded.MovieFilter,
                contentDescription = "视频管理",
                onClick = { /* TODO */ },
            )

            ActionIcon(
                imageVector = Icons.Rounded.Settings,
                contentDescription = "设置",
                onClick = { /* TODO */ },
            )
        }
    )
}
