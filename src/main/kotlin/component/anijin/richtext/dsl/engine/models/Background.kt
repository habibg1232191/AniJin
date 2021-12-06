package component.anijin.richtext.dsl.engine.models

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape

data class Background(
    val backgroundUnder: Color,
    val backgroundOver: Color,
    val shape: Shape
)
