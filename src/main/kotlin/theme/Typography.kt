package theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.sp

val fontFamily = FontFamily(
    Font(
        resource = "fonts/Montserrat-Thin.ttf",
        weight = FontWeight.W100,
        style = FontStyle.Normal
    ),
    Font(
        resource = "fonts/Montserrat-ThinItalic.ttf",
        weight = FontWeight.W100,
        style = FontStyle.Italic
    ),
    Font(
        resource = "fonts/Montserrat-ExtraLight.ttf",
        weight = FontWeight.W200,
        style = FontStyle.Normal
    ),
    Font(
        resource = "fonts/Montserrat-ExtraLightItalic.ttf",
        weight = FontWeight.W200,
        style = FontStyle.Italic
    ),
    Font(
        resource = "fonts/Montserrat-SemiBold.ttf",
        weight = FontWeight.W600,
        style = FontStyle.Normal
    )
)

val darkThemeTypography = AniJinTypography(
    header = TextStyle(
        color = darkThemeColor.primaryText,
        fontSize = 28.sp,
        fontWeight = FontWeight.W600
    ),
    default = TextStyle(
        color = darkThemeColor.primaryText,
    )
)