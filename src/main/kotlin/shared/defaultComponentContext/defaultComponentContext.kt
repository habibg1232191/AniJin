package shared.defaultComponentContext

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry

fun defaultComponentContext() =
    DefaultComponentContext(LifecycleRegistry())