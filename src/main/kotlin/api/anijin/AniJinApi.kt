package api.anijin

import api.anijin.models.TopAnimeSeason
import api.tools.ApiTools
import io.ktor.client.features.websocket.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.runBlocking
import kotlin.concurrent.thread
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
object AniJinApi {
    val topAnimeSeason: TopAnimeSeasonApi
        get() = TopAnimeSeasonApi

    val topAnimeSeasonListener: EventListener<TopAnimeSeason> = emptyEventListener()

    fun invoke() {
        topAnimeSeasonListener(TopAnimeSeason(
            Id = 0,
            ShikimoriId = 0,
            Name = "0",
            ImageUrl = "0",
            Description = "0"
        ))
    }
}

object AniJinApiInfo {
    private const val url: String = "https://anijin-web.herokuapp.com/api/"
    const val urlTopAnimeSeason: String = "$url/top-anime-season"
}

class EventListener<T> {
    private val events: MutableList<(T) -> Unit> = mutableListOf()

    operator fun invoke(data: T) {
        events.forEach {
            it(data)
        }
    }

    operator fun plusAssign(event: (T) -> Unit) {
        events.add(event)
    }

    operator fun minusAssign(event: (T) -> Unit) {
        events.remove(event)
    }
}

fun <T> emptyEventListener(): EventListener<T>
    = EventListener()