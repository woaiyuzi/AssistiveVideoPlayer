package love.yuzi.compose.foundation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun WhiteIcon(
    imageVector: ImageVector,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
    size: Dp = 24.dp
) {
    Icon(
        modifier = modifier.size(size),
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = Color.White,
    )
}

@Composable
fun ActionIcon(
    imageVector: ImageVector,
    contentDescription: String?,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    tint: Color = Color.White
) {
    Icon(
        modifier = modifier.clickable(onClick = onClick),
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = tint
    )
}