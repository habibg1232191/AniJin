package api.shikimori.models.query

data class AnimeQuery(
    val page: Int? = null,
    val limit: Int? = null,
    val order: AnimeOrder? = null,
    val kind: List<AnimeKindParameter>? = null,
    val status: List<AnimeStatusParameter>? = null,
    val season: String? = null,
    val score: Int? = null,
    val duration: AnimeDuration? = null,
    val rating: AnimeRating? = null,
    val censored: Boolean? = null,
    val search: String? = null
)
