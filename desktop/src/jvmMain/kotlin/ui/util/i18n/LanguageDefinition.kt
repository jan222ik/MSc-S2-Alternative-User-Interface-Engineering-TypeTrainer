package ui.util.i18n

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class LanguageDefinition(@Contextual internal val locale: Locale) {

    @Serializable
    object English : LanguageDefinition(Locale.ROOT)

    @Serializable
    object German : LanguageDefinition(Locale.GERMAN)

}
