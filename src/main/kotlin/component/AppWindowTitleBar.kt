package component

import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.runtime.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowScope
import androidx.compose.ui.window.WindowState
import theme.AniJinTheme
import theme.LocalWindowState

@ExperimentalComposeUiApi
@ExperimentalFoundationApi
@Composable
fun WindowScope.windowTitleBar(onCloseEvent: () -> Unit) = WindowDraggableArea {
    val windowState = LocalWindowState.current

    Box(Modifier.fillMaxWidth().height(28.dp).background(AniJinTheme.colors.titleBarColor)){
        Row(Modifier.align(Alignment.CenterEnd)){
            val spacingIcons = 6.5.dp
            val titleBarButtons by remember {
                mutableStateOf(
                    mutableListOf(
                        TitleBarButtons(
                            onClick = {
                                windowState.isMinimized = true
                            }
                        ) {
                            Box(
                                Modifier
                                    .size(15.dp, 2.dp)
                                    .background(Color(0xffededed), RoundedCornerShape(1.dp))
                            )
                        },
                        TitleBarButtons(
                            onClick = {
                                windowState.placement =
                                    when(windowState.placement)
                                    {
                                        WindowPlacement.Floating -> WindowPlacement.Maximized
                                        else -> WindowPlacement.Floating
                                    }
                            }
                        ) {
                            Box(
                                Modifier
                                    .size(13.dp)
                                    .background(Color.Transparent)
                                    .border(2.dp, Color(0xffededed), RoundedCornerShape(2.dp))
                            )
                        },
                        TitleBarButtons(
                            onClick = {
                                onCloseEvent()
                            },
                            hoveringColor = Color(0xffff2424)
                        ) {
                            Image(
                                painter = painterResource("closeMaterialIcon.svg"),
                                contentDescription = "Exit",
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(Modifier.width(2.dp))
                        }
                    )
                )
            }

            titleBarButtons.forEach {
                Row(
                    Modifier
                        .fillMaxHeight()
                        .clickable { it.onClick() }
                        .hoverColor(
                            defaultColor = AniJinTheme.colors.titleBarColor,
                            hoverColor = it.hoveringColor
                        ),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Spacer(Modifier.width(spacingIcons))
                    it.content()
                    Spacer(Modifier.width(spacingIcons))
                }
            }
        }
    }
}

data class TitleBarButtons(
    val onClick: () -> Unit,
    val hoveringColor: Color = Color(0xff636363),
    val content: @Composable () -> Unit
)