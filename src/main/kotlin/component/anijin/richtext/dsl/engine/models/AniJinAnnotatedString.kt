package component.anijin.richtext.dsl.engine.models

data class AniJinAnnotatedString(
    val text: String,
    val replaceRange: List<IntRange>,
    val rangeStyles: Map<IntRange, RichStyle>,
    val styles: List<RichStyle>
) {
    fun getStyleFromIndex(index: Int): RichStyle {
        val resStyle = RichStyle()
        rangeStyles.forEach { (range, style) ->
            if(range.contains(index)) {
                resStyle += style
            }
        }
        return resStyle
    }
}