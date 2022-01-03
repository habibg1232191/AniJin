package component.anijin.richtext.dsl.engine.models

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.unit.TextUnit
import component.anijin.richtext.RichTextDataCollect
import org.jetbrains.skia.FontStyle

data class RichStyle(
    var color: Color? = null,
    var background: Color? = null,
    var selectionColor: Color? = null,
    var selectionBackground: Color? = null,
    var shape: RoundedCornerShape? = null,
    var fontSize: TextUnit? = null,
    var fontStyle: FontStyle? = null,
    var underLine: UnderLine? = null,
    var onClick: ((MouseRichEvent) -> Unit)? = null,
    var onEnter: ((MouseRichEvent) -> Unit)? = null,
    var onExit: ((MouseRichEvent) -> Unit)? = null,
    var onMove: ((MouseRichEvent) -> Unit)? = null
) {
    operator fun plusAssign(richStyle: RichStyle) {
        this.color = richStyle.color ?: color
        this.background = richStyle.background ?: background
        this.selectionColor = richStyle.selectionColor ?: selectionColor
        this.selectionBackground = richStyle.selectionBackground ?: selectionBackground
        this.shape = richStyle.shape ?: shape
        this.fontSize = richStyle.fontSize ?: fontSize
        this.fontStyle = richStyle.fontStyle ?: fontStyle
        this.underLine = richStyle.underLine ?: underLine
        this.onClick = richStyle.onClick ?: onClick
        this.onEnter = richStyle.onEnter ?: onEnter
        this.onExit = richStyle.onExit ?: onExit
        this.onMove = richStyle.onMove ?: onMove
    }
}

data class UnderLine(
    val strokeWidth: Float = 1f,
    val strokeColor: Color? = null
)

data class MouseRichEvent(
    val richStyle: RichStyle,
    val mouseEvent: PointerEvent,
    val offsetText: Offset,
    val text: String,
    val range: IntRange,
    val index: Int,
    val richTextData: List<RichTextDataCollect?>
)