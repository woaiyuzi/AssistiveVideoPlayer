package love.yuzi.video.resource.utils

import android.icu.text.Collator
import java.util.Locale

private val collator = Collator.getInstance(Locale.CHINA)

// 定义一个通用的比较器工厂
fun <T> chineseNaturalComparator(selector: (T) -> String) = Comparator<T> { a, b ->
    val p1 = splitName(selector(a))
    val p2 = splitName(selector(b))
    val textCompare = collator.compare(p1.text, p2.text)
    if (textCompare != 0) textCompare else (p1.number ?: 0) - (p2.number ?: 0)
}

private val numberRegex = Regex("(\\d+)")

private fun splitName(name: String): NameParts {
    val match = numberRegex.find(name)
    return if (match != null) {
        val num = match.value.toInt()
        val text = name.take(match.range.first)
        NameParts(text, num)
    } else {
        NameParts(name, null)
    }
}

private data class NameParts(
    val text: String,
    val number: Int?
)