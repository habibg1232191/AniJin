package api.anijin.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopAnimeSeason(
    @SerialName("id") val Id: Int,
    @SerialName("shikimori_id") val ShikimoriId: Int,
    @SerialName("name") val Name: String,
    @SerialName("image_url") val ImageUrl: String,
    @SerialName("description") val Description: String
)