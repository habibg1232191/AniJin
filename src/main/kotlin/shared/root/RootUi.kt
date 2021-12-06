package shared.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import shared.component.sideBar.SideBar
import shared.screen.home.HomeScreenUi
import theme.AniJinTheme

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@ExperimentalDecomposeApi
@Composable
fun RootUi(component: Root) {
    Row(modifier = Modifier.background(AniJinTheme.colors.background)){
        SideBar(
            onNavigate = {
                component.navigate(this)
            }
        )
        Children(
            routerState = component.routerState,
            animation = crossfadeScale()
        ){
            when (val child = it.instance) {
                is Root.Child.HomeScreen -> HomeScreenUi(child.component)
            }.let {}
        }
    }
}