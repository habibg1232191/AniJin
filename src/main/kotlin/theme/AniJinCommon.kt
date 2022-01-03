package theme

import androidx.compose.material.LocalTextStyle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.window.WindowState
import com.arkivanov.decompose.router.Router
import component.anijin.richtext.dsl.engine.models.RichStyle
import shared.root.RootComponent

data class AniJinColor(
    val background: Color,
    val titleBarColor: Color,
    val sideBarColor: Color,
    val primaryText: Color
)

data class AniJinTypography(
    val default: TextStyle,
    val header: TextStyle
)

object AniJinTheme{
    val colors: AniJinColor
        @Composable
        get() = LocalAniJinThemeColors.current

    val typography: AniJinTypography
        @Composable
        get() = LocalAniJinThemeTypography.current
}

val LocalAniJinThemeColors = staticCompositionLocalOf<AniJinColor> {
    error("No colors provided")
}

val LocalAniJinThemeTypography = staticCompositionLocalOf<AniJinTypography> {
    error("No font provided")
}

val LocalWindowState = staticCompositionLocalOf<WindowState> {
    error("No window state provided")
}

val LocalRichStyle = staticCompositionLocalOf<RichStyle> {
    error("No rich style provided")
}
