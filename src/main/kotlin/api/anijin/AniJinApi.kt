package api.anijin

object AniJinApi {
    val topAnimeSeason: TopAnimeSeasonApi
        get() = TopAnimeSeasonApi
}

object AniJinApiInfo {
    const val url: String = "https://anijin-web.herokuapp.com/api/"
    const val urlTopAnimeSeason: String = "$url/top-anime-season"
}