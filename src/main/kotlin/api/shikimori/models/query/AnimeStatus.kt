package api.shikimori.models.query

import api.shikimori.ShikimoriParameters
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class AnimeStatus {
    @SerialName("anons") ANONS,
    @SerialName("ongoing") ONGOING,
    @SerialName("released") RELEASED
}

data class AnimeStatusParameter(
    override val isInclude: Boolean = false,
    override val enum: AnimeStatus
): ShikimoriParameters<AnimeStatus>