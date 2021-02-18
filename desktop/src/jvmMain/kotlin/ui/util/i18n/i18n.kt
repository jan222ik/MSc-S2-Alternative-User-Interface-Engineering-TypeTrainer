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
    override fun resolve(): String = value
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

@Suppress("ClassName", "MemberVisibilityCanBePrivate")
sealed class i18n {

    sealed class str : i18n() {
        object app {
            val name: KeyI18N = "app_name".i18Key()
        }

        sealed class navigation : str() {

            companion object {
                val settings: KeyI18N = "navigation_settings".i18Key()
            }
        }

        sealed class exercise : str() {
            sealed class selection : exercise() {
                object textMode : selection() {
                    val literature = "text_mode_literature".i18Key()
                    val literatureDescription = "text_mode_literature_description".i18Key()
                    val randomWords = "text_mode_rng_words".i18Key()
                    val randomChars = "text_mode_rng_chars".i18Key()
                    fun getAll() = listOf(literature, randomWords, randomChars)
                }

                object exerciseMode : selection() {
                    val speed = "exercise_mode_speed".i18Key()
                    val accuracy = "exercise_mode_accuracy".i18Key()
                    val noTimeLimit = "exercise_mode_no_time_limit".i18Key()
                    fun getAll() = listOf(speed, accuracy, noTimeLimit)
                }
            }
        }

    }
}



@Suppress("PrivatePropertyName", "PropertyName")
abstract class LanguageDefinition {
    // Not-Translatable
    private val i18n_str_app_name = "TypeTrainer"
    private val ipsum = """
        Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
    """.trimIndent()

    // Can be translated
    internal open val i18n_str_navigation_settings = "Settings"

    internal open val i18n_str_exercise_selection_textMode_literature = "Literature"
    internal open val i18n_str_exercise_selection_textMode_literature_description = ipsum
    internal open val i18n_str_exercise_selection_textMode_randomWords = "Random Words"
    internal open val i18n_str_exercise_selection_textMode_randomChars = "Random Characters"

    internal open val i18n_str_exercise_selection_exerciseMode_speed = "Speed"
    internal open val i18n_str_exercise_selection_exerciseMode_accuracy = "Accuracy"
    internal open val i18n_str_exercise_selection_exerciseMode_noTimeLimit = "No Timelimit"

    fun buildMap(): Map<String, String> {
        return mapOf(
            i18n.str.app.name.key to i18n_str_app_name,
            i18n.str.navigation.settings.key to i18n_str_navigation_settings,
            i18n.str.exercise.selection.textMode.literature.key to i18n_str_exercise_selection_textMode_literature,
            i18n.str.exercise.selection.textMode.literatureDescription.key to i18n_str_exercise_selection_textMode_literature_description,
            i18n.str.exercise.selection.textMode.randomWords.key to i18n_str_exercise_selection_textMode_randomWords,
            i18n.str.exercise.selection.textMode.randomChars.key to i18n_str_exercise_selection_textMode_randomChars,
            i18n.str.exercise.selection.exerciseMode.speed.key to i18n_str_exercise_selection_exerciseMode_speed,
            i18n.str.exercise.selection.exerciseMode.accuracy.key to i18n_str_exercise_selection_exerciseMode_accuracy,
            i18n.str.exercise.selection.exerciseMode.noTimeLimit.key to i18n_str_exercise_selection_exerciseMode_noTimeLimit,
        )
    }
}

object English : LanguageDefinition()

object German : LanguageDefinition() {
    override val i18n_str_navigation_settings = "Einstellungen"


    // Exercise
    // Selection
    // Text Mode
    override val i18n_str_exercise_selection_textMode_literature = "Literatur"
    override val i18n_str_exercise_selection_textMode_randomWords = "Zufällige Wörter"
    override val i18n_str_exercise_selection_textMode_randomChars = "Zufällige Buchstaben"
    // Exercise Mode
    override val i18n_str_exercise_selection_exerciseMode_speed = "Geschwindigkeit"
    override val i18n_str_exercise_selection_exerciseMode_accuracy = "Genauigkeit"
    override val i18n_str_exercise_selection_exerciseMode_noTimeLimit = "Keine Zeitbegrenzung"
}
