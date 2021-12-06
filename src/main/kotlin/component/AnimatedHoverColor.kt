package component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.input.pointer.pointerMoveFilter

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AnimatedColor(
    hoverColor: Color,
    defaultColor: Color,
    modifier: Modifier = Modifier,
    animationSpec: AnimationSpec<Color> = tween(100),
    content: @Composable Color.() -> Unit
){
    var isHover by remember { mutableStateOf(false) }
    val colorHovered = animateColorAsState(
        targetValue = if(isHover) hoverColor else defaultColor,
        animationSpec = animationSpec
    )

    Box(modifier.pointerMoveFilter(
        onEnter = {
            isHover = true
            false
        },
        onExit = {
            isHover = false
            false
        }
    ).then(modifier)){
        content(colorHovered.value)
    }
}

@Composable
@ExperimentalComposeUiApi
fun Modifier.hoverColor(
    defaultColor: Color,
    hoverColor: Color,
    animationSpec: AnimationSpec<Color> = tween()
): Modifier {
    var isHovering by remember { mutableStateOf(false) }
    val animateColor by animateColorAsState(
        targetValue = if(isHovering) hoverColor else defaultColor,
        animationSpec = animationSpec
    )

    return this.then(
        Modifier
            .background(animateColor)
            .onPointerEvent(PointerEventType.Enter) {
                isHovering = true
            }
            .onPointerEvent(PointerEventType.Exit) {
                isHovering = false
            }
    )
}
