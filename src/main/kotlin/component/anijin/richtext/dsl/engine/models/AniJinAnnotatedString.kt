package component.anijin.richtext.dsl.engine.models

data class AniJinAnnotatedString(
    val text: String,
    val replaceRange: List<IntRange>,
    val rangeStyles: Map<IntRange, RichStyle>,
    val styles: MutableList<RichStyle>
) {
    fun getStyleFromIndex(index: Int): RichStyleAndRange {
        val resStyle = RichStyle()
        val resRange = mutableListOf<TripleVar<RichStyle, IntRange, Boolean>>()
        rangeStyles.forEach { (range, style) ->
            if(range.contains(index)) {
                resStyle += style
                resRange += TripleVar(style, range, false)
            }
        }
        return RichStyleAndRange(
            richStyle = resStyle,
            ranges = resRange
        )
    }

    fun appendStyle(style: RichStyle) {
        styles.add(0, style)
    }
}

data class RichStyleAndRange(
    val richStyle: RichStyle,
    val ranges: List<TripleVar<RichStyle, IntRange, Boolean>>,
)

data class TripleVar<A, B, C>(
    var first: A,
    var two: B,
    var third: C
)