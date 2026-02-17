package love.yuzi.compose.foundation

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun WhiteText(text: String, modifier: Modifier = Modifier, fontSize: TextUnit = TextUnit.Unspecified) {
    Text(
        text = text,
        color = Color.White,
        fontSize = fontSize,
        modifier = modifier
    )
}