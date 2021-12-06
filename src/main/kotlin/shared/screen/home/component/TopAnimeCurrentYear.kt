package shared.screen.home.component

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.anijin.models.TopAnimeSeason
import component.AsyncImage
import component.Carousel
import component.rememberCarouselState
import theme.AniJinTheme

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun TopAnimeCurrentYear(
    topAnimeCurrentYear: List<TopAnimeSeason>,
    parentBoxWidth: Float
){
    Carousel(rememberCarouselState()){
        topAnimeCurrentYear.forEach {
            Box(
                Modifier.width(parentBoxWidth.dp).fillMaxHeight().clip(RoundedCornerShape(4.dp))
                    .clipToBounds()){
                AsyncImage(
                    url = it.ImageUrl,
                    saveToFile = true,
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(4.dp)).clipToBounds()
                )
                Box(
                    Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                        .background(brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(0.5f), Color.Black)
                        ))){
                    SelectionContainer {
                        Text(
                            text = it.Name,
                            fontSize = 24.sp,
                            style = AniJinTheme.typography.header,
                            modifier = Modifier.padding(start = 3.dp, bottom = 5.dp)
                        )
                    }
                }
            }
        }
    }
}