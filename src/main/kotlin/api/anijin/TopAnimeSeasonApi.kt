package api.anijin

import api.tools.ApiTools
import api.anijin.models.TopAnimeSeason
import kotlinx.serialization.ExperimentalSerializationApi

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
object TopAnimeSeasonApi {
    @OptIn(ExperimentalSerializationApi::class)
    suspend fun get(): Result<List<TopAnimeSeason>> =
        ApiTools.get(AniJinApiInfo.urlTopAnimeSeason)
}