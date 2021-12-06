package api.shikimori.models

import api.shikimori.models.query.AnimeKind
import api.shikimori.models.query.AnimeStatus
import kotlinx.datetime.LocalDate
import kotlinx.serialization.*
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Anime(
    val id: Int,
    val name: String,
    val russian: String,
    val image: AnimeImage,
    val url: String,
    val kind: AnimeKind,
    val score: Float,
    val status: AnimeStatus,
    val episodes: Int,
    val episodes_aired: Int,
    val aired_on: LocalDate,
    val released_on: LocalDate?
)

@Serializable
data class AnimeImage(
    @Serializable(with = ShikimoriUrlSerializer::class)
    val original: String,
    @Serializable(with = ShikimoriUrlSerializer::class)
    val preview: String,
    @Serializable(with = ShikimoriUrlSerializer::class)
    val x96: String,
    @Serializable(with = ShikimoriUrlSerializer::class)
    val x48: String
)

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = String::class)
object ShikimoriUrlSerializer : KSerializer<String> {
    private const val urlShikimori = "https://shikimori.one"
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("ShikimoriUrl", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: String) =
        encoder.encodeString(value)

    override fun deserialize(decoder: Decoder): String =
        urlShikimori + decoder.decodeString().split("?")[0]
}