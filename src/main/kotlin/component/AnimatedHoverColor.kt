package component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
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
    val interactiveSource = remember { MutableInteractionSource() }

    val isHover by interactiveSource.collectIsHoveredAsState()
    val colorHovered = animateColorAsState(
        targetValue = if(isHover) hoverColor else defaultColor,
        animationSpec = animationSpec
    )

    Box(modifier.hoverable(interactiveSource).then(modifier)){
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
    val interactiveSource = remember { MutableInteractionSource() }

    val isHover by interactiveSource.collectIsHoveredAsState()
    val animateColor = animateColorAsState(
        targetValue = if(isHover) hoverColor else defaultColor,
        animationSpec = animationSpec
    )

    return this.then(
        Modifier.hoverable(interactiveSource).background(animateColor.value)
    )
}
