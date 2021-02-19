package ui.util.i18n

import java.util.*

sealed class LanguageDefinition(internal val locale: Locale) {

    object English : LanguageDefinition(Locale.ROOT)

    object German : LanguageDefinition(Locale.GERMAN)

}
