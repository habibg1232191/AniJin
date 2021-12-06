package component.anijin

import androidx.compose.animation.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup

@Composable
fun AniJinPopup(
    visible: Boolean,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    offset: IntOffset = IntOffset.Zero,
    alignment: Alignment = Alignment.TopStart,
    modifier: Modifier = Modifier,
    content: @Composable AnimatedVisibilityScope.() -> Unit
) {
    Popup(
        offset = offset,
        alignment = alignment
    ) {
        Layout(
            content = {
                AnimatedVisibility(
                    visible = visible,
                    enter = enter,
                    exit = exit,
                    content = content
                )
            },
            modifier = modifier
        ) { measurably, constraints ->
            val placeable = measurably.map { measurable ->
                measurable.measure(Constraints())
            }

            var maxWidth = constraints.maxWidth
            var maxHeight = constraints.maxHeight

            placeable.forEach {
                maxWidth = it.width
                maxHeight = it.height
            }

            layout(0, 0) {
                placeable.forEach { placeable ->
                    placeable.place(0, 0)
                }
            }
        }
    }
}
// Layout(
//        content = content,
//        modifier = modifier
//    ) { measurably, constraints ->
//        val placeable = measurably.map { measurable ->
//            measurable.measure(Constraints())
//        }
//
//        var maxWidth = constraints.maxWidth
//        var maxHeight = constraints.maxHeight
//
//        placeable.forEach {
//            if(constraints.maxWidth < it.width)
//                maxWidth = it.width
//
//            if(constraints.maxHeight < it.height)
//                maxHeight = it.height
//        }
//
//        layout(0, 0) {
//            placeable.forEach { placeable ->
//                val startX = offset.x
//                val startY = offset.y
//
//                val centerX = (maxWidth / 2) - (placeable.width / 2) + offset.x
//                val centerY = (maxHeight / 2) - (placeable.height / 2) + offset.y
//
//                val endX = maxWidth - placeable.width + offset.x
//                val endY = maxHeight - placeable.height + offset.y
//
//                when(alignment){
//                    Alignment.TopStart -> placeable.placeRelative(x = startX, y = startY)
//                    Alignment.TopEnd -> placeable.placeRelative(x = endX, y = 0)
//                    Alignment.TopCenter, Alignment.Top -> placeable.placeRelative(centerX, startY)
//
//                    Alignment.CenterStart -> placeable.placeRelative(x = startX, y = centerY)
//                    Alignment.CenterEnd -> placeable.placeRelative(x = centerX, y = centerY)
//                    Alignment.Center -> placeable.placeRelative(x = centerX, y = centerY)
//
//                    Alignment.Bottom, Alignment.BottomStart -> placeable.placeRelative(x = startX, y = endY)
//                    Alignment.BottomEnd -> placeable.placeRelative(x = endX, y = endY)
//                    Alignment.BottomCenter -> placeable.placeRelative(x = centerX, y = endY)
//                }
//            }
//        }
//    }
@Composable
fun AniJinAnimatedPopup(
    visible: Boolean,
    enter: EnterTransition = fadeIn(),
    exit: ExitTransition = fadeOut(),
    offset: IntOffset = IntOffset.Zero,
    alignment: Alignment = Alignment.TopStart,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {

}