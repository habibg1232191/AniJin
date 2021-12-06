package component.anijin.richtext.dsl.engine

import component.anijin.richtext.dsl.engine.models.AniJinAnnotatedString
import component.anijin.richtext.dsl.engine.models.AniJinAnnotatedStringBuilder

fun buildAniJinAnnotatedString(text: String, block: AniJinAnnotatedStringBuilder.() -> Unit): AniJinAnnotatedString =
    AniJinAnnotatedStringBuilder(text).apply(block).build()