package shared.screen.animePage

import com.arkivanov.decompose.value.Value

interface AnimePage {
    val models: Value<Model>

    data class Model(
        val test: String = ""
    )
}