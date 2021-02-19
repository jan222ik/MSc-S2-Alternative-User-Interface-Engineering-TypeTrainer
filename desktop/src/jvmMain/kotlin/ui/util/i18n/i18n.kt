package ui.util.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import java.util.*


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
    override fun resolve(): String = value
}

object ResolverI18n {
    var currentLang: LanguageDefinition = LanguageDefinition.English
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

private operator fun ResourceBundle.get(key: String): String? {
    return try {
        this.getString(key)
    } catch (npe: NullPointerException) {
        null
    } catch (e: MissingResourceException) {
        System.err.println("MissingResourceException: Can't find resource for bundle java.util.PropertyResourceBundle, key $key")
        null
    }
}


@Suppress("ClassName", "MemberVisibilityCanBePrivate")
sealed class i18n {
    fun String.i18Key() = KeyI18N("$path.$this")
    abstract val path: String

    sealed class str : i18n() {

        object app : str() {
            override val path: String = "app"

            val name: KeyI18N = "app_name".i18Key()
        }

        sealed class navigation : str() {
            override val path: String = "navigation"

            object self : navigation() {
                val settings: KeyI18N = "settings".i18Key()
            }
        }

        sealed class exercise : str() {
            override val path: String = "exercise"

            sealed class selection : exercise() {
                override val path: String = super.path + ".selection"

                object textMode : selection() {
                    override val path: String = super.path + ".textMode"
                    val literature = "literature".i18Key()
                    val literatureDescription = "literature_description".i18Key()
                    val randomWords = "rng_words".i18Key()
                    val randomChars = "rng_chars".i18Key()
                    fun getAll() = listOf(literature, randomWords, randomChars)
                }

                object exerciseMode : selection() {
                    override val path: String = super.path + ".exerciseMode"
                    val speed = "speed".i18Key()
                    val accuracy = "accuracy".i18Key()
                    val noTimeLimit = "no_time_limit".i18Key()
                    fun getAll() = listOf(speed, accuracy, noTimeLimit)
                }
            }
        }

    }
}

sealed class LanguageDefinition(private val locale: Locale) {
    fun buildMap(): ResourceBundle {
        return ResourceBundle.getBundle("strings", locale) ?: defaultLang
    }

    companion object {
        val defaultLang: ResourceBundle = ResourceBundle.getBundle("strings", Locale.ROOT)
    }

    object English : LanguageDefinition(Locale.ROOT)

    object German : LanguageDefinition(Locale.GERMAN)
}
