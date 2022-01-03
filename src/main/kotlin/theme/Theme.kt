package theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import component.anijin.richtext.dsl.engine.models.RichStyle

@Composable
fun AniJinTheme(
    windowState: WindowState,
    content: @Composable () -> Unit
){
    val colors = darkThemeColor
    val typography = darkThemeTypography
    val richStyle = RichStyle(
        color = colors.primaryText,
        selectionBackground = Color.Black,
        shape = RoundedCornerShape(2.dp)
    )

    CompositionLocalProvider(
        LocalAniJinThemeColors provides colors,
        LocalRippleTheme provides MaterialRippleTheme,
        LocalAniJinThemeTypography provides typography,
        LocalWindowState provides windowState,
        LocalRichStyle provides richStyle
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