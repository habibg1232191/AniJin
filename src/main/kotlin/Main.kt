import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.*
import shared.root.RootComponent
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import shared.root.RootUi
import androidx.compose.foundation.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.*
import api.anijin.AniJinApi
import com.arkivanov.decompose.ExperimentalDecomposeApi
import component.windowTitleBar
import theme.AniJinTheme
import java.awt.AWTEvent
import java.awt.Toolkit
import javax.swing.SwingUtilities


@OptIn(ExperimentalAnimationApi::class, ExperimentalComposeUiApi::class)
@ExperimentalDecomposeApi
@ExperimentalFoundationApi
fun main(){
    val lifecycle = LifecycleRegistry()
    val root = RootComponent(DefaultComponentContext(lifecycle))

    application {
        val windowState = rememberWindowState(
            position = WindowPosition(Alignment.Center)
        )

        Window(
            onCloseRequest = ::exitApplication,
            state = windowState,
            title = "AniJin",
            undecorated = true
        ) {
            Toolkit.getDefaultToolkit().addAWTEventListener(
                {
                    println(it)
                },
                AWTEvent.MOUSE_MOTION_EVENT_MASK
            )

            AniJinTheme(
                windowState = windowState
            ) {
                Column {
                    windowTitleBar(
                        onCloseEvent = { exitApplication() }
                    )
                    RootUi(root)
                }
            }
        }
    }
}
