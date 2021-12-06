package shared.screen.home

import api.anijin.models.TopAnimeSeason
import com.arkivanov.decompose.value.Value

interface HomeScreen {
    val models: Value<Model>

    data class Model(
        val topAnimeCurrentYearStateLoading: TopAnimeCurrentYearStateLoading = TopAnimeCurrentYearStateLoading.LOADING,
        val topAnimeCurrentYear: List<TopAnimeSeason> = listOf()
    )

    enum class TopAnimeCurrentYearStateLoading{
        LOADING,
        LOADED,
        ERROR
    }
}