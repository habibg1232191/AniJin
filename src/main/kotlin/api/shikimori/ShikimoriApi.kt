package api.shikimori

object ShikimoriApi {
    val anime: AnimeApi
        get() = AnimeApi
}

object ShikimoriApiInfo {
    const val url: String = "https://shikimori.one/api"
    const val urlAnimes: String = "$url/animes"
}