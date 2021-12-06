package component.anijin.richtext.dsl.engine.models

data class AniJinAnnotatedString(
    val text: String,
    val replaceRange: List<IntRange>,
    val rangeStyles: Map<IntRange, RichStyle>,
    val styles: List<RichStyle>
) {
    fun getStyleFromIndex(index: Int): RichStyle? {
        var resStyle: RichStyle? = null
        rangeStyles.forEach { (range, i) ->
            if(range.contains(index)) {
                resStyle = i
            }
        }
        return resStyle
    }
}