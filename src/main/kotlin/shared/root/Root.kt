package shared.root

import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.value.Value
import shared.screen.animePage.AnimePageComponent
import shared.screen.home.HomeScreenComponent

interface Root {
    val routerState: Value<RouterState<*, Child>>

    fun navigate(config: RootComponent.Config)

    sealed class Child {
        class HomeScreen(val component: HomeScreenComponent) : Child()
        class AnimePage(val component: AnimePageComponent) : Child()
    }
}