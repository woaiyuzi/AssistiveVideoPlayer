package love.yuzi.compose.foundation

import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun VideoTitle(title: String, onBack: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        WhiteIcon(
            imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
            contentDescription = "Back",
            modifier = Modifier.clickable(onClick = onBack)
        )

        TitleText(title)
    }
}

@Composable
fun ProgramTitle(title: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.clickable(onClick = onClick),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TitleText(title = title, fontSize = 16.sp)

        WhiteIcon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = "节目详情",
        )
    }
}

@Composable
private fun TitleText(title: String, modifier: Modifier = Modifier, fontSize: TextUnit = 18.sp) {
    WhiteText(
        text = title,
        fontSize = fontSize,
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .basicMarquee(
                iterations = Int.MAX_VALUE,
                velocity = 30.dp
            )
    )
}