package shared.root

import com.arkivanov.decompose.*
import com.arkivanov.decompose.router.RouterState
import com.arkivanov.decompose.router.push
import com.arkivanov.decompose.router.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import shared.screen.home.HomeScreen
import shared.screen.home.HomeScreenComponent

class RootComponent(
    componentContext: ComponentContext
): Root, ComponentContext by componentContext {
    private val router =
        router<Config, Root.Child>(
            initialConfiguration = Config.HomeScreen,
            handleBackButton = true, // Pop the back stack on back button press
            childFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Root.Child>> = router.state

    override fun navigate(config: Config){
        router.push(config)
    }

    private fun createChild(config: Config, componentContext: ComponentContext): Root.Child =
        when (config) {
            is Config.HomeScreen -> Root.Child.HomeScreen(itemHomeScreen(componentContext) as HomeScreenComponent)
        }

    private fun itemHomeScreen(componentContext: ComponentContext): HomeScreen =
        HomeScreenComponent(
            componentContext = componentContext
        )

    sealed class Config : Parcelable {
        object HomeScreen : Config()
    }
}
