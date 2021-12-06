package component.anijin.richtext.dsl.engine.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.skia.FontStyle

data class RichStyle(
    var color: Color? = null,
    var background: Color? = null,
    var selectionColor: Color? = null,
    var selectionBackground: Color? = null,
    var fontSize: TextUnit? = null,
    var fontStyle: FontStyle? = null,
    var underLine: Boolean = false,
    var underLineWidth: Float? = null,
    var underLineColor: Color? = null,
    var onClick: (() -> Unit)? = null
) {
    operator fun plusAssign(richStyle: RichStyle) {
        this.color = richStyle.color ?: color
        this.background = richStyle.background ?: background
        this.selectionColor = richStyle.selectionColor ?: selectionColor
        this.selectionBackground = richStyle.selectionBackground ?: selectionBackground
        this.fontSize = richStyle.fontSize ?: fontSize
        this.fontStyle = richStyle.fontStyle ?: fontStyle
        this.underLine = richStyle.underLine
        this.underLineWidth = richStyle.underLineWidth ?: underLineWidth
        this.underLineColor = richStyle.underLineColor ?: underLineColor
        this.onClick = richStyle.onClick ?: onClick
    }
}