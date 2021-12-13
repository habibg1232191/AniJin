package component.anijin.richtext.dsl.engine.models

import androidx.compose.ui.graphics.Color

class AniJinAnnotatedStringBuilder(txt: String) {
    private val styles: MutableList<RichStyle> = mutableListOf()
    val text: String = txt
    private val rangeStyles: MutableMap<IntRange, RichStyle> = mutableMapOf()
    private val replaceRange: MutableList<IntRange> = mutableListOf()
    private var styleIndex = 0

    override fun toString(): String = text

    fun appendStyle(richStyle: RichStyle, regex: (String) -> Sequence<MatchResult>) =
        regex(text).forEach { rangeStyles[it.range] = richStyle; styleIndex++ }

    fun appendRangeStyle(richStyle: RichStyle, range: (String) -> IntRange) =
        replaceRange.add(range(text))

    fun appendStyleWithRange(richStyle: RichStyle, range: (String) -> IntRange) {
        rangeStyles[range(text)] = richStyle
    }

    fun appendStyleWithRegex(
        regex: (String) -> RichStyleRanges
    ) {
        val matchResult = regex(text)
        matchResult.ranges.forEach {
            rangeStyles[it] = matchResult.richStyle
        }
    }

    fun hideText(regex: (String) -> Sequence<MatchResult>) =
        regex(text).forEach { replaceRange.add(it.range) }

    fun hideFirstText(regex: (String) -> MatchResult?) =
        regex(text)?.range?.let { replaceRange.add(it) }

    fun hideText(group: Int, regex: (String) -> Sequence<MatchResult>) =
        regex(text).forEach { it.groups[group]?.range?.let { it1 -> replaceRange.add(it1) } }

    fun hideTextWithGroups(groups: (String) -> Sequence<List<MatchGroup?>>) =
        groups(text).forEach {
            for (group in it) group?.range?.let { it1 -> replaceRange.add(it1) }
        }

    fun build(): AniJinAnnotatedString = AniJinAnnotatedString(text, replaceRange, rangeStyles, styles)
}

data class RichStyleRanges(
    val richStyle: RichStyle,
    val ranges: List<IntRange>
)

fun String.tryGetColor(): Color? {
    return try {
        Color(this.removePrefix("#").toInt(16)).copy(1f)
    } catch (e: Exception) {
        null
    }
}