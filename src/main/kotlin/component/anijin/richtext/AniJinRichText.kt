package component.anijin.richtext

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import component.anijin.richtext.dsl.engine.models.AniJinAnnotatedString
import component.anijin.richtext.dsl.engine.models.RichStyle
import org.jetbrains.skia.*
import kotlin.math.max

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AniJinRichText(
    annotatedString: AniJinAnnotatedString,
    onSelection: ((String) -> Unit)? = null,
    selectionBackground: Color = Color.Blue,
    fontSize: TextUnit = 16.sp
) {
    val fill by remember { mutableStateOf(Paint().setARGB(1, 250, 250, 250).setAlphaf(1f)) }

    var isSelection by remember { mutableStateOf(false) }
    var isFirstSelection by remember { mutableStateOf(false) }
    var isTap by remember { mutableStateOf(false) }

    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    var startSelectionIndex by remember { mutableStateOf(0) }
    var endSelectionIndex by remember { mutableStateOf(0) }

    var sizeTextBlock by remember { mutableStateOf(Size.Zero) }

    val paint = androidx.compose.ui.graphics.Paint()
    paint.color = selectionBackground
    paint.isAntiAlias = true
    paint.alpha = 0.3f

    Canvas(
        modifier = Modifier
            .size(sizeTextBlock.width.dp, sizeTextBlock.height.dp)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDragStart = {
                        isSelection = true
                        isFirstSelection = true
                    },
                    onDrag = { change, _ ->
                        dragOffset = change.position
                    }
                )
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = {
                        isSelection = false
                        isTap = true
                        startSelectionIndex = 0
                        endSelectionIndex = 0
                        onSelection?.invoke("")
                    }
                )
            }
    ) {
        drawIntoCanvas {
            var xOffsetText = 0f
            var yOffsetText = 0f
            val nativeCanvas = it.nativeCanvas

            paint.color = selectionBackground
            paint.alpha = 0.4f

            var isContinue: Boolean
            var isFist = true
            var font: Font
            annotatedString.text.forEachIndexed { index, char ->
                isContinue = true
                annotatedString.replaceRange.forEach { range ->
                    if(range.contains(index)){
                        isContinue = false
                        return@forEach
                    }
                }

                if(isContinue) {
                    val style: RichStyle? = annotatedString.getStyleFromIndex(index)
                    font = Font(
                        typeface = Typeface.makeFromName(
                            name = null,
                            style = style?.fontStyle ?: FontStyle.NORMAL
                        ),
                        size = style?.fontSize?.value ?: fontSize.value
                    )
                    font.size = style?.fontSize?.value ?: fontSize.value
                    val richLine = TextLine.make(char.toString(), font)
                    if(isSelection) {
                        val coordinates = xOffsetText + richLine.width
                        if(dragOffset.y > yOffsetText-font.metrics.xHeight/2 && dragOffset.y < yOffsetText + richLine.capHeight + font.metrics.xHeight/2) {
                            if(coordinates - richLine.width/2 > dragOffset.x && isFirstSelection) {
                                startSelectionIndex = index
                                isFirstSelection = false
                            }
                            if(coordinates - richLine.width/2 > dragOffset.x && isFist) {
                                endSelectionIndex = if(index >= annotatedString.text.length-1) annotatedString.text.length else index
                                isFist = false
                            }
                        }
                        val textSelection =
                            if(startSelectionIndex < endSelectionIndex) annotatedString.text.substring(startSelectionIndex, endSelectionIndex)
                            else annotatedString.text.substring(endSelectionIndex, startSelectionIndex)
                        onSelection?.invoke(textSelection)
                    }

                    val isSelectedCurrentChar = (index in startSelectionIndex until endSelectionIndex) || (index in endSelectionIndex until startSelectionIndex)
                    if(style != null) {
                        if(style.background != null && !isSelectedCurrentChar) {
                            paint.color = style.background!!
                            it.drawRect(
                                left = xOffsetText,
                                right = xOffsetText + richLine.width,
                                top = yOffsetText-richLine.xHeight/2,
                                bottom = yOffsetText + richLine.capHeight + richLine.xHeight/2,
                                paint = paint
                            )
                        }

                        fill.color = style.color?.toArgb() ?: Color.White.toArgb()
                    } else {
                        fill.color = Color.White.toArgb()
                    }

                    if(isSelectedCurrentChar && char.toString() != "\n") {
                        paint.color = selectionBackground
                        style?.let { richStyle ->
                            if(richStyle.selectionColor != null)
                                fill.color = richStyle.selectionColor!!.toArgb()
                            if(richStyle.selectionBackground != null)
                                paint.color = richStyle.selectionBackground!!
                        }
                        it.drawRect(
                            left = xOffsetText,
                            right = xOffsetText + richLine.width,
                            top = yOffsetText-richLine.xHeight/2,
                            bottom = yOffsetText + richLine.capHeight + richLine.xHeight/2,
                            paint = paint
                        )
                    }

                    if (style != null) {
                        if(style.underLine) {
                            paint.color = style.underLineColor ?: Color.White
                            it.drawRect(
                                left = xOffsetText,
                                right = xOffsetText + richLine.width,
                                top = yOffsetText + richLine.capHeight + 1.5f,
                                bottom = yOffsetText + richLine.capHeight + ((style.underLineWidth?.plus(1.5f)) ?: 1f),
                                paint = paint
                            )
                        }
                    }

                    if(char.toString() == "\n") {
                        yOffsetText += richLine.capHeight + richLine.xHeight
                        xOffsetText = 0f
                    } else {
                        nativeCanvas.drawTextLine(richLine, xOffsetText, yOffsetText + richLine.capHeight, fill)
                        xOffsetText += richLine.width
                    }
                    sizeTextBlock = Size(
                        width = max(xOffsetText, sizeTextBlock.width),
                        height = max(sizeTextBlock.height, yOffsetText + richLine.capHeight)
                    )
                }
            }
        }
    }
}