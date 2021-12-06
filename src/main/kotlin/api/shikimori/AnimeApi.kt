package api.shikimori

import api.shikimori.models.Anime
import api.shikimori.models.query.AnimeQuery
import api.tools.ApiTools
import api.shikimori.models.FullAnime

object AnimeApi {
    suspend fun getAnimes(query: AnimeQuery): Result<List<Anime>> =
        ApiTools.getAnimesWithQuery(ShikimoriApiInfo.urlAnimes, query)

    suspend fun getById(id: Int): Result<FullAnime> =
        ApiTools.get("${ShikimoriApiInfo.urlAnimes}/$id")

    suspend fun Anime.getFull(): Result<FullAnime> =
        ApiTools.get("${ShikimoriApiInfo.url}/$id")
}

interface ShikimoriParameters<T : Enum<T>> {
    val isInclude: Boolean
    val enum: Enum<T>
}