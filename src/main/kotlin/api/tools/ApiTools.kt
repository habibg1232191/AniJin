package api.tools

import api.shikimori.ShikimoriParameters
import api.shikimori.models.Anime
import api.shikimori.models.query.AnimeQuery
import io.ktor.client.engine.apache.*
import io.ktor.client.features.json.*
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import io.ktor.client.request.*
import kotlinx.serialization.ExperimentalSerializationApi
import org.apache.http.conn.ssl.NoopHostnameVerifier
import org.apache.http.conn.ssl.TrustSelfSignedStrategy
import org.apache.http.ssl.SSLContextBuilder

class ApiTools {
    companion object {
        val client = HttpClient(Apache) {
            install(JsonFeature) {
                serializer = KotlinxSerializer()
            }
            engine {
                customizeClient {
                    setSSLContext(SSLContextBuilder.create().loadTrustMaterial(TrustSelfSignedStrategy()).build())
                    setSSLHostnameVerifier(NoopHostnameVerifier())
                }
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        suspend fun getAnimesWithQuery(url: String, query: AnimeQuery): Result<List<Anime>> {
            return try {
                val response: HttpResponse = client.get(url) {
                    parameter("page", query.page)
                    parameter("limit", query.limit)
                    parameter("order", query.order.toString().lowercase())
                    parameter("kind", enumToShikimoriParameters(query.kind).ifEmpty { null })
                    parameter("status", enumToShikimoriParameters(query.status).ifEmpty { null })
                    parameter("season", query.season)
                    parameter("score", query.score)
                    parameter("duration", query.duration)
                    parameter("rating", query.rating)
                    parameter("censored", query.censored)
                    parameter("search", query.search)
                }
                val responseString: String = response.receive()

                Result.success(Json {ignoreUnknownKeys = true}.decodeFromString(responseString))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        @OptIn(ExperimentalSerializationApi::class)
        suspend inline fun <reified T> get(url: String): Result<T> {
            return try {
                val response: HttpResponse = client.get(url) {
                    contentType(ContentType.Application.Json)
                }
                val responseString: String = response.receive()

                Result.success(Json.decodeFromString(responseString))
            } catch (e: Exception) {
                Result.failure(e)
            }
        }

        fun <T : Enum<T>> enumToShikimoriParameters(
            parameters: List<ShikimoriParameters<T>>?,
            lowercase: Boolean = false
        ): String = StringBuilder().apply {
            parameters?.let {
                it.forEachIndexed { index, parameter ->
                    append(if(parameter.isInclude) "!" else "")
                    append(if (lowercase) parameter.enum.name.lowercase() else parameter.enum.name)
                    append(if(it.size > index + 1) "," else "")
                }
            }
        }.toString()
    }
}