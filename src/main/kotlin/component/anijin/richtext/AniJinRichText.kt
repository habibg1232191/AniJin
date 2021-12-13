package component.anijin.richtext

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.*
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.*
import component.anijin.richtext.dsl.engine.models.AniJinAnnotatedString
import component.anijin.richtext.dsl.engine.models.MouseRichEvent
import component.anijin.richtext.dsl.engine.models.RichStyleAndRange
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import java.awt.Toolkit
import org.jetbrains.skia.*
import org.jetbrains.skia.impl.withResult
import java.awt.AWTEvent
import java.awt.event.AWTEventListener
import java.awt.event.MouseEvent
import javax.swing.JFrame
import javax.swing.JPopupMenu
import kotlin.math.max
import kotlin.reflect.typeOf

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun AniJinRichText(
    annotatedString: AniJinAnnotatedString,
    onSelection: ((String, IntRange) -> Unit)? = null,
    selectionBackground: Color = Color.Blue,
    fontSize: TextUnit = 16.sp
) {
    val fill by remember { mutableStateOf(Paint().setARGB(1, 250, 250, 250).setAlphaf(1f)) }

    var isSelection by remember { mutableStateOf(false) }
    var isFirstSelection by remember { mutableStateOf(false) }
    var isFirstDrawing by remember { mutableStateOf(true) }

    var density: Density? by remember { mutableStateOf(null) }
    with(LocalDensity.current) {
        density = this
    }

    var dragOffset by remember { mutableStateOf(Offset.Zero) }

    var startSelectionIndex by remember { mutableStateOf(0) }
    var endSelectionIndex by remember { mutableStateOf(0) }

    var sizeTextBlock by remember { mutableStateOf(Size.Zero) }
    var onSizeChanged by remember { mutableStateOf(IntSize.Zero) }

    var richTextDataCollect by remember { mutableStateOf(mutableListOf<RichTextDataCollect>()) }
    var rangesForMouseMoveData by remember { mutableStateOf(mutableListOf<RangeHover>()) }

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
                        startSelectionIndex = 0
                        endSelectionIndex = 0
                        onSelection?.invoke("", IntRange.EMPTY)

                        richTextDataCollect.forEach { dataCollect ->
                            val richLine = dataCollect.richLine
                            val index = dataCollect.index
                            val (xOffsetText, yOffsetText) = dataCollect.offset
                            val (style, ranges) = dataCollect.richStyleAndRange

                            ranges.forEach { (styleRange, range) ->
                                if(it.x > xOffsetText && it.x < xOffsetText + richLine.width && it.y > yOffsetText && it.y < yOffsetText + richLine.capHeight + richLine.xHeight/2 && index in range) {
                                    styleRange.onClick?.invoke(
                                        MouseRichEvent(
                                            richStyle = style,
                                            mousePosition = it,
                                            offsetText = Offset(
                                                x = xOffsetText,
                                                y = yOffsetText
                                            ),
                                            text = annotatedString.text,
                                            range = range,
                                            index = index,
                                            richTextData = richTextDataCollect
                                        )
                                    )
                                }
                            }
                        }
                    }
                )
            }
            .pointerMoveFilter(
                onMove = {
                    if(!isSelection) {
                        val checkedRange = mutableListOf<IntRange>()
                        rangesForMouseMoveData.forEachIndexed { i1, (range, isHover) ->
                            if(range !in checkedRange) {
                                val firstIndex = range.first
                                val lastIndex = range.last
                                val style = richTextDataCollect[range.first].richStyleAndRange.richStyle

                                val richTextDataFirst = richTextDataCollect[firstIndex]
                                val richTextDataLast = richTextDataCollect[lastIndex]

                                val (xOffsetText, yOffsetText) = richTextDataFirst.offset

                                if(it.x > richTextDataFirst.offset.x && it.x < richTextDataLast.offset.x + richTextDataLast.richLine.width && it.y > richTextDataFirst.offset.y && it.y < richTextDataLast.offset.y + richTextDataLast.richLine.capHeight + richTextDataLast.richLine.xHeight/2) {
                                    val mouseRichEvent =
                                        MouseRichEvent(
                                            richStyle = style,
                                            mousePosition = it,
                                            offsetText = Offset(
                                                x = xOffsetText,
                                                y = yOffsetText
                                            ),
                                            text = annotatedString.text,
                                            range = range,
                                            index = range.first,
                                            richTextData = richTextDataCollect
                                        )
                                    if(isHover) style.onMove?.invoke(mouseRichEvent)
                                    else style.onEnter?.invoke(mouseRichEvent)

                                    rangesForMouseMoveData[i1].isHover = true
                                } else if(isHover) {
                                    style.onExit?.invoke(
                                        MouseRichEvent(
                                            richStyle = style,
                                            mousePosition = it,
                                            offsetText = Offset(
                                                x = xOffsetText,
                                                y = yOffsetText
                                            ),
                                            text = annotatedString.text,
                                            range = range,
                                            index = firstIndex,
                                            richTextData = richTextDataCollect
                                        )
                                    )
                                    rangesForMouseMoveData[i1].isHover = false
                                }
                            }
                            checkedRange.add(range)
                        }
                    }
                    false
                }
            )
            .onSizeChanged {
                richTextDataCollect.clear()
                rangesForMouseMoveData.clear()
                onSizeChanged = it

                onSize(
                    annotatedString,
                    fontSize,
                    rangesForMouseMoveData,
                    richTextDataCollect,
                    it
                ) { _, _ -> }
            }
    ) {
        if(isFirstDrawing) {
            richTextDataCollect.clear()
            rangesForMouseMoveData.clear()

            onSize(
                annotatedString,
                fontSize,
                rangesForMouseMoveData,
                richTextDataCollect,
                onSizeChanged
            ) { xOffsetText, yOffsetText ->
                sizeTextBlock = Size(
                    width = max(xOffsetText, sizeTextBlock.width),
                    height = max(yOffsetText, sizeTextBlock.height)
                )
            }
            isFirstDrawing = false
        }

        drawIntoCanvas {
            val nativeCanvas = it.nativeCanvas

            paint.color = selectionBackground
            paint.alpha = 0.4f

            var isFist = true
            richTextDataCollect.forEach { dataCollect ->
                val richLine = dataCollect.richLine
                val xOffsetText = dataCollect.offset.x
                val yOffsetText = dataCollect.offset.y
                val font = dataCollect.font
                val index = dataCollect.index
                val (style, ranges) = dataCollect.richStyleAndRange
                val char = dataCollect.char

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
                    val rangeSelection =
                        if(startSelectionIndex < endSelectionIndex) startSelectionIndex..endSelectionIndex
                        else endSelectionIndex..startSelectionIndex
                    onSelection?.invoke(annotatedString.text.substring(rangeSelection), rangeSelection)
                }

                val isSelectedCurrentChar = (index in startSelectionIndex until endSelectionIndex) || (index in endSelectionIndex until startSelectionIndex)

                if(style.background != null && !isSelectedCurrentChar) {
                    fill.color = style.background?.toArgb()!!
                    val shapeSize = Size(
                        width = richLine.width,
                        height = richLine.xHeight/2
                    )

                    if(ranges[0].two.first == index) {
                        val corners = createCornerRadius(
                            topLeftRadius = style.shape?.topEnd?.toPx(shapeSize, density!!) ?: 0f,
                            bottomLeftRadius = style.shape?.bottomEnd?.toPx(shapeSize, density!!) ?: 0f
                        )
                        nativeCanvas.drawRRect(
                            r = RRect.makeComplexLTRB(
                                l = xOffsetText,
                                r = xOffsetText + richLine.width,
                                t = yOffsetText - richLine.xHeight/2,
                                b = yOffsetText + richLine.capHeight + richLine.xHeight/2,
                                radii = corners
                            ),
                            paint = fill
                        )
                    } else if(ranges[0].two.last == index) {
                        val corners = createCornerRadius(
                            topRightRadius = style.shape?.topStart?.toPx(shapeSize, density!!) ?: 0f,
                            bottomRightRadius = style.shape?.bottomStart?.toPx(shapeSize, density!!) ?: 0f
                        )
                        nativeCanvas.drawRRect(
                            r = RRect.makeComplexLTRB(
                                l = xOffsetText,
                                r = xOffsetText + richLine.width,
                                t = yOffsetText-richLine.xHeight/2,
                                b = yOffsetText + richLine.capHeight + richLine.xHeight/2,
                                radii = corners
                            ),
                            paint = fill
                        )
                    }
                    else {
                        nativeCanvas.drawRect(
                            r = Rect(
                                left = xOffsetText,
                                right = xOffsetText + richLine.width,
                                top = yOffsetText-richLine.xHeight/2,
                                bottom = yOffsetText + richLine.capHeight + richLine.xHeight/2,
                            ),
                            paint = fill
                        )
                    }
                }

                fill.color = style.color?.toArgb() ?: Color.White.toArgb()

                if(isSelectedCurrentChar && char.toString() != "\n") {
                    paint.color = selectionBackground
                    style.let { richStyle ->
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

                if(style.underLine != null) {
                    paint.color = style.underLine?.strokeColor ?: Color.White
                    it.drawRect(
                        left = xOffsetText,
                        right = xOffsetText + richLine.width,
                        top = yOffsetText + richLine.capHeight + 1.5f,
                        bottom = yOffsetText + richLine.capHeight + ((style.underLine?.strokeWidth?.plus(1.5f)) ?: 1f),
                        paint = paint
                    )
                }

                nativeCanvas.drawTextLine(richLine, xOffsetText, yOffsetText + richLine.capHeight, fill)
            }
        }
    }

    LaunchedEffect(Unit) {
        snapshotFlow { onSizeChanged }
            .distinctUntilChanged()
            .collectLatest {
                richTextDataCollect.clear()
                rangesForMouseMoveData.clear()

                onSize(
                    annotatedString,
                    fontSize,
                    rangesForMouseMoveData,
                    richTextDataCollect,
                    it
                ) { xOffsetText, yOffsetText ->
                    sizeTextBlock = Size(
                        width = max(xOffsetText, sizeTextBlock.width),
                        height = max(yOffsetText, sizeTextBlock.height)
                    )
                }
            }

        Toolkit.getDefaultToolkit().addAWTEventListener(
            {
                println(it)
            },
            AWTEvent.MOUSE_MOTION_EVENT_MASK
        )
    }

    LaunchedEffect(annotatedString) {
        richTextDataCollect = mutableListOf()
        rangesForMouseMoveData = mutableListOf()

        onSize(
            annotatedString,
            fontSize,
            rangesForMouseMoveData,
            richTextDataCollect,
            onSizeChanged
        ) { xOffsetText, yOffsetText ->
            sizeTextBlock = Size(
                width = max(xOffsetText, sizeTextBlock.width),
                height = max(yOffsetText, sizeTextBlock.height)
            )
        }
    }
}

fun onSize(
    annotatedString: AniJinAnnotatedString,
    fontSize: TextUnit,
    rangesForMouseMoveData: MutableList<RangeHover>,
    richTextDataCollect: MutableList<RichTextDataCollect>,
    size: IntSize,
    onSizeTextBlock: (Float, Float) -> Unit
) {
    var xOffsetText = 0f
    var yOffsetText = 0f
    var lineCount = 0

    var width = 0f
    var height = 0f

    var isContinue: Boolean
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
            val richStyleAndRange = annotatedString.getStyleFromIndex(index)
            val style = richStyleAndRange.richStyle
            var fontBytes = ByteArray(0)

            useResource("fonts/Montserrat-Medium.ttf") {
                fontBytes = it.readAllBytes()
            }

            font = Font(
                typeface = Typeface.makeFromData(
                    data = Data.makeFromBytes(fontBytes),
                    index = FontStyle.Normal.value
                ),
                size = style.fontSize?.value ?: fontSize.value
            )
            font.size = style.fontSize?.value ?: fontSize.value
            val richLine = TextLine.make(char.toString(), font)
            if(char == '\n') {
                yOffsetText += richLine.capHeight + richLine.xHeight
                xOffsetText = 0f
                lineCount++
            } else {
                val richTextData =
                    RichTextDataCollect(
                        offset = Offset(
                            x = xOffsetText,
                            y = yOffsetText
                        ),
                        char = char,
                        richLine = richLine,
                        richStyleAndRange = richStyleAndRange,
                        index = index,
                        font = font,
                        currentLine = lineCount
                    )

                richTextDataCollect.add(richTextData)
                xOffsetText += richLine.width
            }
            width = max(xOffsetText, width)
            height = yOffsetText + richLine.capHeight + richLine.xHeight
        }
    }
    richTextDataCollect.forEach {
        it.richStyleAndRange.ranges.forEach { range ->
            rangesForMouseMoveData.add(RangeHover(range.two))
        }
    }
    onSizeTextBlock(width, height)
}

data class RichTextDataCollect(
    val offset: Offset,
    val char: Char,
    val richLine: TextLine,
    val richStyleAndRange: RichStyleAndRange,
    val index: Int,
    val font: Font,
    val currentLine: Int
)

data class RangeHover(
    val range: IntRange,
    var isHover: Boolean = false,
)

fun createCornerRadius(
    topLeftRadius: Float = 0f,
    topRightRadius: Float = 0f,
    bottomLeftRadius: Float = 0f,
    bottomRightRadius: Float = 0f
) : FloatArray {
    return floatArrayOf(
        topLeftRadius, topLeftRadius,
        topRightRadius, topRightRadius,
        bottomRightRadius, bottomRightRadius,
        bottomLeftRadius, bottomLeftRadius
    )
}