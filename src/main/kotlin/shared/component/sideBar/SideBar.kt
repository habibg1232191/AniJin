package shared.component.sideBar

import androidx.compose.ui.Modifier
import com.arkivanov.decompose.value.Value
import shared.root.RootComponent

interface SideBar {
    val models: Value<Model>

    data class Model(
        val tabs: List<Tab>
    )

    data class Tab(
        val name: String,
        val tooltipText: String,
        val configRoute: RootComponent.Config,
        val modifier: Modifier = Modifier
    )
}