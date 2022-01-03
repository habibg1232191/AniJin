package shared.root

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.DefaultWindowExceptionHandlerFactory
import androidx.compose.ui.window.LocalWindowExceptionHandlerFactory
import androidx.compose.ui.window.WindowExceptionHandler
import androidx.compose.ui.window.WindowExceptionHandlerFactory
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.animation.child.crossfadeScale
import shared.component.sideBar.SideBar
import shared.screen.animePage.AnimePageUi
import shared.screen.home.HomeScreenUi
import theme.AniJinTheme
import java.awt.Window
import java.awt.event.WindowEvent
import javax.swing.JOptionPane
import javax.swing.SwingUtilities

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
                is Root.Child.AnimePage -> AnimePageUi(child.component)
            }.let {}
        }
    }
}