package component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import java.awt.Cursor
import kotlin.math.*

@Composable
fun rememberCarouselState(initialValue: Float = 0f): CarouselState {
    return rememberSaveable(saver = CarouselState.Saver) {
        CarouselState(initialValue)
    }
}

@Stable
class CarouselState(val initialValue: Float) {
    var value: Float by mutableStateOf(initialValue, structuralEqualityPolicy())
    var currentScrollIndex: Int by mutableStateOf(0, structuralEqualityPolicy())

    companion object {
        val Saver: Saver<CarouselState, *> = Saver(
            save = { it },
            restore = { CarouselState(it.initialValue) }
        )
    }
}

@ExperimentalComposeUiApi
@Composable
fun Carousel(
    carouselState: CarouselState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
){
    var placeable: List<Placeable> = listOf()
    var maxWidth by remember { mutableStateOf(0) }

    val xScrollForAnimate by animateFloatAsState(
        targetValue = carouselState.value
    )

    Layout(
        content = content,
        modifier = Modifier
            .pointerHoverIcon(PointerIcon(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR)))
            .onPointerEvent(PointerEventType.Scroll) {
                val scrollDelta = it.changes.first().scrollDelta.y
                var consumedScroll = true

                if(scrollDelta < 0) {
                    if (carouselState.currentScrollIndex < placeable.size - 1 && carouselState.value > maxWidth - placeable.getWidth().toFloat())
                        carouselState.currentScrollIndex += 1
                    else
                        consumedScroll = false
                } else {
                    if (carouselState.currentScrollIndex > 0)
                        carouselState.currentScrollIndex -= 1
                    else
                        consumedScroll = false
                }

                var scrollXPos = 0
                for (i in 0 until carouselState.currentScrollIndex)
                    scrollXPos += placeable[i].width

                carouselState.value = -scrollXPos.toFloat()

                if(carouselState.value > 0 || maxWidth > placeable.getWidth().toFloat())
                    carouselState.value = 0f
                else if (carouselState.value < maxWidth - placeable.getWidth().toFloat())
                    carouselState.value = maxWidth - placeable.getWidth().toFloat()

                if(consumedScroll) it.changes.first().consume()
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        val placeableWidth = placeable.getWidth().toFloat()
                        val coeff = 1f

                        val dif = if(carouselState.value > 1) {
                            dragAmount / treeshold(carouselState.value.absoluteValue, coeff)
                        } else if (carouselState.value < maxWidth - placeableWidth) {
                            val coefficient = -(placeableWidth - carouselState.value.absoluteValue - maxWidth)
                            dragAmount / treeshold(coefficient.absoluteValue, coeff)
                        } else {
                            dragAmount
                        }
                        carouselState.value += dif
                        var xPos = 0
                        var index = 0
                        for (element in placeable) {
                            if(xPos + element.width / 2 > carouselState.value.absoluteValue) {
                                carouselState.currentScrollIndex = index
                                break
                            }
                            xPos += element.width
                            index++
                        }
                    },
                    onDragEnd = {
                        val placeableWidth = placeable.getWidth().toFloat()

                        if(carouselState.value > 0 || maxWidth > placeableWidth) {
                            carouselState.value = 0f
                            carouselState.currentScrollIndex = 0
                        } else if (carouselState.value < maxWidth - placeableWidth) {
                            carouselState.value = maxWidth - placeableWidth
                        } else {
                            var xPos = 0
                            placeable.forEach { placeable ->
                                if(xPos + placeable.width / 2 > carouselState.value.absoluteValue)
                                    return@forEach

                                xPos += placeable.width
                            }

                            carouselState.value = -xPos.toFloat()

                            if(carouselState.value > 0)
                                carouselState.value = 0f
                            else if (carouselState.value < maxWidth - placeableWidth)
                                carouselState.value = maxWidth - placeableWidth
                        }
                    }
                )
            }
            .then(modifier)
    ) { measurably, constraints ->
        placeable = measurably.map { measurable ->
            measurable.measure(constraints)
        }

        var maxHeight = 0
        maxWidth = constraints.maxWidth

        placeable.forEach{ placeable ->
            maxHeight = max(placeable.height, maxHeight)
        }

        layout(constraints.maxWidth, maxHeight) {
            var xPosition = xScrollForAnimate.roundToInt()

            placeable.forEachIndexed { _, placeable ->
                placeable.place(x = xPosition, y = 0)
                xPosition += placeable.width
            }
        }
    }
}

fun List<Placeable>.getWidth(): Int {
    var width = 0
    forEach {
        width += it.width
    }
    return width
}

fun treeshold(value: Float, coeff: Float): Float
    = sqrt(value * coeff)