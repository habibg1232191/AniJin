package shared.component.sideBar

import androidx.compose.foundation.layout.offset
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import shared.root.RootComponent

class SideBarComponent(
    componentContext: ComponentContext,
): SideBar, ComponentContext by componentContext {
    private val _models = MutableValue(SideBar.Model(tabs))
    override val models: Value<SideBar.Model> = _models

    private val tabs: List<SideBar.Tab>
        get() = listOf(
            SideBar.Tab(
                name = "Home",
                configRoute = RootComponent.Config.HomeScreen,
                tooltipText = "Главная",
                modifier = Modifier.offset(y = (-3).dp)
            ),
            SideBar.Tab(
                name = "Search",
                tooltipText = "Поиск",
                configRoute = RootComponent.Config.HomeScreen,
            ),
            SideBar.Tab(
                name = "Star",
                tooltipText = "Избранное(В скором времени)",
                configRoute = RootComponent.Config.HomeScreen,
                modifier = Modifier.offset(y = (-3).dp)
            ),
            SideBar.Tab(
                name = "Shuffle",
                tooltipText = "Случайное(В скором времени)",
                configRoute = RootComponent.Config.HomeScreen,
            )
        )
}