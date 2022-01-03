package shared.screen.animePage

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import shared.defaultComponentContext.defaultComponentContext

@Composable
fun AnimePageUi(
    component: AnimePageComponent = AnimePageComponent(defaultComponentContext())
) {
    Box(
        Modifier.fillMaxSize()
    ) {
        Button(
            onClick = { println("Click on AnimePage") }
        ) {
            Text(
                text = "Buuton"
            )
        }
    }
}