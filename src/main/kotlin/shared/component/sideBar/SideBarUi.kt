package shared.component.sideBar

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.forEachGesture
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.*
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.*
import component.anijin.AniJinPopup
import shared.defaultComponentContext.defaultComponentContext
import shared.root.RootComponent
import theme.AniJinTheme
import java.awt.event.MouseEvent

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun SideBar(
    component: SideBarComponent = SideBarComponent(defaultComponentContext()),
    onNavigate: RootComponent.Config.() -> Unit
){
    Box(Modifier.fillMaxHeight().width(38.dp).background(AniJinTheme.colors.sideBarColor).clip(RoundedCornerShape(bottomEnd = 8.dp)).clipToBounds()){
        Column(
            Modifier
                .align(Alignment.TopCenter)
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val spaceIcons = 0.dp
            val model = component.models.value
            val tabs = model.tabs

            var selectedTabIndex by remember { mutableStateOf(0) }
            var coordinatesPopup by remember { mutableStateOf(0f) }
            var isHover by remember { mutableStateOf(false) }

            tabs.forEachIndexed { index, tab ->
                var coordinates by remember { mutableStateOf(0f) }
                Column(
                    modifier = Modifier
                        .onGloballyPositioned {
                            coordinates = -it.parentCoordinates?.let { it1 -> it.localPositionOf(it1, Offset(0f, 0f)) }?.y!!
                        }
                ) {
                    Spacer(Modifier.height(spaceIcons))
                    IconButton(
                        onClick = { onNavigate(tab.configRoute) },
                        modifier = Modifier
                            .padding(0.dp)
                            .pointerMoveFilter(
                                onEnter = {
                                    coordinatesPopup = coordinates
                                    selectedTabIndex = index
                                    isHover = true
                                    false
                                },
                                 onExit = {
                                     isHover = false
                                     false
                                 }
                            )
                    ){
                        Image(
                            painterResource("sideBarIcons/${tab.name}Icon.svg"),
                            tab.name,
                            Modifier.requiredSize(24.dp).then(tab.modifier)
                        )
                    }

                    Spacer(Modifier.height(spaceIcons))
                }
            }

            val animateOffsetY by animateDpAsState(
                targetValue = coordinatesPopup.dp + 12.dp
            )

            AniJinPopup(
                visible = isHover,
                offset = IntOffset(
                    x = 42, y = 0
                ),
                modifier = Modifier.offset(y = animateOffsetY)
            ) {
                Box(
                    Modifier
                        .background(color = Color(0xff313131), shape = RoundedCornerShape(4.dp))
                        .clipToBounds()
                ) {
                    AnimatedContent(
                        targetState = selectedTabIndex,
                        transitionSpec = {
                            if(targetState > initialState) {
                                slideInVertically(initialOffsetY = { it }) + fadeIn() with
                                        slideOutVertically(targetOffsetY = { -it }) + fadeOut()
                            } else {
                                slideInVertically(initialOffsetY = { -it }) + fadeIn() with
                                        slideOutVertically(targetOffsetY = { it }) + fadeOut()
                            }.using(SizeTransform(clip = false))
                        },
                        modifier = Modifier.padding(8.dp, 5.dp)
                    ) { index ->
                        Text(
                            text = tabs[index].tooltipText,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Modifier.onClick(
    onPress: (mouseEvent: MouseEvent?, pointerButtons: PointerButtons) -> Unit = { _, _ -> },
    onRelease: (mouseEvent: MouseEvent?, pointerButtons: PointerButtons) -> Unit = {_, _ -> }
): Modifier {
    val modifier = Modifier.pointerInput(Unit) {
        forEachGesture {
            awaitPointerEventScope {
                while (true) {
                    val event = awaitPointerEvent()
                    when(event.type) {
                        PointerEventType.Press -> {
                            onPress(event.mouseEvent, event.buttons)
                        }
                        PointerEventType.Release -> {
                            onRelease(event.mouseEvent, event.buttons)
                        }
                    }
                }
            }
        }
    }
    return then(modifier)
}