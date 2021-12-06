package shared.screen.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class HomeScreenComponent(
    componentContext: ComponentContext
): HomeScreen, ComponentContext by componentContext {
    private val _models = MutableValue(HomeScreen.Model())
    override val models: Value<HomeScreen.Model> = _models
}