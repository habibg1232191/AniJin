package api.shikimori.models.query

import api.shikimori.ShikimoriParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AnimeKind {
    @SerialName("tv") TV,
    @SerialName("movie") MOVIE,
    @SerialName("ova") OVA,
    @SerialName("ona") ONA,
    @SerialName("special") SPECIAL,
    @SerialName("music") MUSIC,
    @SerialName("tv_13") TV_13,
    @SerialName("tv_24") TV_24,
    @SerialName("tv_48") TV_48
}

data class AnimeKindParameter(
    override val isInclude: Boolean = true,
    override val enum: AnimeKind
): ShikimoriParameters<AnimeKind>
