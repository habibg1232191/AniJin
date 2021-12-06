package component.anijin.richtext.dsl.engine.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.TextUnit
import org.jetbrains.skia.FontStyle

data class RichStyle(
    var color: Color? = null,
    var background: Color? = null,
    val selectionColor: Color? = null,
    val selectionBackground: Color? = null,
    val fontSize: TextUnit? = null,
    val fontStyle: FontStyle? = null,
    val underLine: Boolean = false,
    val underLineWidth: Float? = null,
    val underLineColor: Color? = null,
)