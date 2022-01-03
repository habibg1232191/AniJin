package shared.screen.animePage

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value

class AnimePageComponent(
    componentContext: ComponentContext
): AnimePage, ComponentContext by componentContext {
    private val _models = MutableValue(AnimePage.Model())
    override val models: Value<AnimePage.Model> = _models
}