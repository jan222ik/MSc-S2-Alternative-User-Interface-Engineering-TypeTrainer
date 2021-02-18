package ui.util.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember


open class KeyI18N(val key: String) {

    open fun resolve(): String = ResolverI18n.resolve(this) ?: error("Missing Resource for key: $key")

    @Composable
    fun observedString() = remember(LanguageAmbient.current) { resolve() }

    @Composable
    fun observedString(vararg keys: Any?) = remember(LanguageAmbient.current, *keys) { resolve() }

    @Composable
    operator fun unaryPlus() = observedString()

}

//@Deprecated("Should not be used in product but useful during development")
data class RequiresTranslationI18N(val value: String) : KeyI18N("") {
    override fun resolve() : String = value
}

object ResolverI18n {
    var currentLang: LanguageDefinition = English
    var currentLangBuild = currentLang.buildMap()

    fun resolve(key: KeyI18N): String? {
        return currentLangBuild[key.key]
    }

    fun changeLang(languageDefinition: LanguageDefinition) {
        if (languageDefinition == currentLang) return
        currentLang = languageDefinition
        currentLangBuild = languageDefinition.buildMap()
    }
}

private fun String.i18Key() = KeyI18N(this)

@Suppress("ClassName")
sealed class i18n {

    sealed class str : i18n() {
        object app {
            val name: KeyI18N = "app_name".i18Key()
        }

        sealed class navigation : i18n() {

            companion object {
                val settings: KeyI18N = "navigation_settings".i18Key()
            }
        }

    }
}

@Suppress("PrivatePropertyName", "PropertyName")
abstract class LanguageDefinition {
    // Not-Translatable
    private val i18n_str_app_name =  "TypeTrainer"

    // Can be translated
    internal open val i18n_str_navigation_settings = "Settings"

    fun buildMap(): Map<String, String> {
        return mapOf(
            i18n.str.app.name.key to i18n_str_app_name,
            i18n.str.navigation.settings.key to i18n_str_navigation_settings
        )
    }
}

object English : LanguageDefinition()

object German : LanguageDefinition() {
    override val i18n_str_navigation_settings = "Einstellungen"
}
