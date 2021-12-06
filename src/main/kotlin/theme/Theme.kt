package theme

import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.WindowState

@Composable
fun AniJinTheme(
    windowState: WindowState,
    content: @Composable () -> Unit
){
    val colors = darkThemeColor
    val typography = darkThemeTypography

    CompositionLocalProvider(
        LocalAniJinThemeColors provides colors,
        LocalRippleTheme provides MaterialRippleTheme,
        LocalAniJinThemeTypography provides typography,
        LocalWindowState provides windowState
    ){
        ProvideTextStyle(
            value = AniJinTheme.typography.default,
            content = content
        )
    }
}

@Immutable
private object MaterialRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = RippleTheme.defaultRippleColor(
        contentColor = Color.White,
        lightTheme = MaterialTheme.colors.isLight
    )

    @Composable
    override fun rippleAlpha() = RippleTheme.defaultRippleAlpha(
        contentColor = LocalContentColor.current,
        lightTheme = MaterialTheme.colors.isLight
    )
}