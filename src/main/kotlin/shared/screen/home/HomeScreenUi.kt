package shared.screen.home

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFontLoader
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.useResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import api.anijin.AniJinApi
import component.anijin.richtext.AniJinRichText
import component.anijin.richtext.dsl.engine.buildAniJinAnnotatedString
import component.anijin.richtext.dsl.engine.models.RichStyle
import component.anijin.richtext.dsl.engine.models.RichStyleRanges
import component.anijin.richtext.dsl.engine.models.UnderLine
import component.anijin.richtext.dsl.engine.models.tryGetColor
import component.anijin.richtext.fontLoader
import kotlinx.coroutines.delay
import org.jetbrains.skia.Data
import org.jetbrains.skia.FontStyle
import shared.defaultComponentContext.defaultComponentContext
import shared.screen.home.component.TopAnimeCurrentYear
import theme.AniJinTheme
import theme.LocalWindowState

@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@Composable
fun HomeScreenUi(
    component: HomeScreenComponent = HomeScreenComponent(defaultComponentContext())
){
    val windowState = LocalWindowState.current
    var parentBoxWidth by remember { mutableStateOf(windowState.size.height.value-38) }
    val verticalScrollState = rememberScrollState(0)

    Column(
        Modifier
            .fillMaxSize()
            .padding(start = 10.dp, top = 10.dp, end = 10.dp)
            .onGloballyPositioned {
                parentBoxWidth = it.size.width.toFloat()
            }
            .clipToBounds()
            .verticalScroll(verticalScrollState, reverseScrolling = true)
    ){
        val models = component.models.value
        var topAnimeCurrentYearStateLoading = models.topAnimeCurrentYearStateLoading
        var topAnimeCurrentYear = models.topAnimeCurrentYear
        val textInputService = LocalTextInputService.current

        SelectionContainer {
            Box(Modifier.fillMaxWidth()) {
//                AniJinRichText(
//                    text = "Лучшее за сезон",
//                    fontSize = 20.sp
//                )
                Text(
                    text = "Лучшее за сезон",
                    style = AniJinTheme.typography.header,
                    modifier = Modifier.padding(bottom = 10.dp),
                    fontSize = 20.sp
                )
            }
        }

//        LaunchedEffect(Unit) {
//            topAnimeCurrentYearStateLoading = HomeScreen.TopAnimeCurrentYearStateLoading.LOADING
//            AniJinApi.topAnimeSeason.get()
//                .onSuccess {
//                    topAnimeCurrentYear = it
//                    topAnimeCurrentYearStateLoading = HomeScreen.TopAnimeCurrentYearStateLoading.LOADED
//                }
//                .onFailure {
//                    topAnimeCurrentYearStateLoading = HomeScreen.TopAnimeCurrentYearStateLoading.ERROR
//                }
//        }

//        Box(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height((parentBoxWidth/4.75).dp)
//                .padding(end = 10.dp)
//                .clip(RoundedCornerShape(5.dp))
//                .clipToBounds()
//        ){
//            Crossfade(targetState = topAnimeCurrentYearStateLoading){ state ->
//                when(state){
//                    HomeScreen.TopAnimeCurrentYearStateLoading.LOADED -> {
//                        TopAnimeCurrentYear(
//                            topAnimeCurrentYear = topAnimeCurrentYear,
//                            parentBoxWidth = parentBoxWidth
//                        )
//                    }
//                    HomeScreen.TopAnimeCurrentYearStateLoading.LOADING -> {
//                        Box(Modifier.fillMaxSize()){
//                            Text(
//                                text = "Загрузка...",
//                                fontSize = 26.sp,
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//                    }
//                    HomeScreen.TopAnimeCurrentYearStateLoading.ERROR -> {
//                        Box(Modifier.fillMaxSize()){
//                            Text(
//                                text = "Произошла Ошибка",
//                                fontSize = 26.sp,
//                                modifier = Modifier.align(Alignment.Center)
//                            )
//                        }
//                    }
//                }
//            }
//        }

//        var animes by remember { mutableStateOf<List<Anime>>(listOf()) }
//
//        LaunchedEffect(Unit) {
//            ShikimoriApi.anime.getAnimes(
//                AnimeQuery(
//                    order = AnimeOrder.POPULARITY,
//                    season = "fall_2021",
//                    limit = 50
//                )
//            ).onSuccess {
//                animes = it
//            }.onFailure {
//                println(it)
//            }
//        }
//
//        Text(
//            text = "Лучшее за месяц",
//            fontSize = 16.sp,
//            modifier = Modifier.padding(top = 5.dp, bottom = 2.dp)
//        )

//        Box(
//            Modifier.clip(RoundedCornerShape(4.dp)).clipToBounds()
//        ) {
//            Carousel(rememberCarouselState()) {
//                animes.forEach {
//                    Column(
//                        Modifier
//                            .padding(10.dp)
//                            .width(220.dp)
//                    ) {
//                        AsyncImage(
//                            url = it.image.original,
//                            modifier = Modifier.clip(RoundedCornerShape(4.dp))
//                        )
//                        Text(
//                            text = it.russian,
//                            textAlign = TextAlign.Center,
//                            fontSize = 16.sp,
//                            modifier = Modifier.fillMaxWidth().padding(top = 5.dp)
//                        )
//                    }
//                }
//            }
//        }

        var fontFamilyData by remember { mutableStateOf<Data?>(null) }
        LaunchedEffect(Unit) {
            useResource("fonts/Montserrat-Regular.ttf") {
                fontFamilyData = Data.makeFromBytes(it.readAllBytes())
            }
        }
        AniJinRichText(
            annotatedString = buildAniJinAnnotatedString(
                text = "これは日本人です; Это русский;😁😀😎 backText in 992 y. 2H\nThis ᴴᴰ is English [span color=\"#0000\" background=\"#424242\"]Text[/span]\nText On Placed Yes"
            ) {
                appendStyle(richStyle = RichStyle(color = Color.Gray)) { Regex("[0-9]").findAll(it) }
                appendStyle(richStyle = RichStyle(color = Color.Gray)) { Regex("<(.*?)>").findAll(it) }
                appendStyle(richStyle = RichStyle(fontStyle = FontStyle.BOLD_ITALIC)) { Regex("This ᴴᴰ is English").findAll(it) }

                appendStyle(
                    richStyle = RichStyle(
                        fontStyle = FontStyle.ITALIC
                    )
                ) {
                    Regex("Text").findAll(it)
                }

                appendStyle(
                    richStyle = RichStyle(
                        underLine = UnderLine()
                    )
                ) {
                    Regex("Это русский").findAll(it)
                }

                appendStyle(
                    richStyle = RichStyle(
                        background = Color.DarkGray,
                        selectionBackground = Color.Black,
                        selectionColor = Color.Gray,
                        shape = CircleShape
                    )
                ) { Regex("background").findAll(it) }

                appendStyleWithRegex { text ->
                    val resRangeList = mutableListOf<IntRange>()
                    val parametersString = listOf(
                        "color",
                        "background",
                        "selectionColor"
                    )
                    val parameters = mutableMapOf<String, String?>()
                    val regexParameters: () -> String = {
                        var resParameters = ""
                        for (parameter in parametersString) {
                            resParameters += " *($parameter=\"(?<$parameter>.*?)\")?"
                        }
                        resParameters
                    }
                    val pattern = "\\[span${regexParameters()} *\\](?<content>[\\s\\S]*?)\\[\\/span\\]"
                    val regex = Regex(pattern)
                    regex.findAll(text).forEach {
                        for (parameter in parametersString)
                            if(it.groups[parameter]?.range != null)
                                parameters[parameter] = it.groups[parameter]?.value

                        if(it.groups["content"]?.range != null)
                            resRangeList.add(it.groups["content"]?.range!!)
                    }
                    RichStyleRanges(
                        richStyle = RichStyle(
                            color = parameters[parametersString[0]]?.tryGetColor(),
                            background = parameters[parametersString[1]]?.tryGetColor(),
                            selectionColor = parameters[parametersString[2]]?.tryGetColor()
                        ),
                        ranges = resRangeList
                    )
                }
            },
            selectionBackground = Color.Blue,
            fontSize = 26.sp,
            fontFamilyData = fontFamilyData
        )
        var isHidden by remember { mutableStateOf(false) }

        Button(
            onClick = { isHidden = !isHidden }
        ) {
            AnimatedContent(
                targetState = isHidden,
                transitionSpec = {
                    scaleIn() + fadeIn() with scaleOut(targetScale = 2f) + fadeOut()
                }
            ) { state ->
                Text(
                    text = if(state) "Hidden" else "Show",
                    fontSize = 18.sp
                )
            }
        }
    }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedCounter() {
    var countState by remember { mutableStateOf(85) }

    var count = countState
    val countList = mutableListOf<Int>()

    while(count > 0) {
        countList.add(count%10)
        count /= 10
    }

    countList.reverse()

    LaunchedEffect(Unit) {
        while(true) {
            countState++
            delay(500)
        }
    }

    Spacer(
        modifier = Modifier
            .padding(vertical = 15.dp)
    )
    Row {
        countList.forEach { count ->
            AnimatedContent(
                targetState = count,
                transitionSpec = {
                    if(targetState > initialState && targetState == 0) {
                        slideInVertically(initialOffsetY = { it }) + fadeIn() with
                                slideOutVertically(targetOffsetY = { -it }) + fadeOut()
                    } else{
                        slideInVertically(initialOffsetY = { -it }) + fadeIn() with
                                slideOutVertically(targetOffsetY = { it }) + fadeOut()
                    }.using(SizeTransform(clip = false))
                }
            ){ state ->
                Text(
                    text = "$state",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}