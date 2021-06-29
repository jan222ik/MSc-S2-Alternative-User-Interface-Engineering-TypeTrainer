@file:Suppress("FunctionName")

package ui.util.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.github.jan222ik.common.HasDoc


val LanguageAmbient = compositionLocalOf<AppLangConfig> { error("No language definition found!") }

/**
 * Applies a default language and provides setter to change language via LanguageAmbient.
 *
 * @param languageDefault defines the default language
 * @param content Lambda for composable content
 */
@HasDoc
@Composable
fun LanguageConfiguration(
    languageDefault: LanguageDefinition = LanguageDefinition.English,
    content: @Composable () -> Unit
) {
    val (langConfig, setLangConfig) = remember(languageDefault) {
        ResolverI18n.changeLang(languageDefault)
        mutableStateOf(AppLangConfig(languageDefault))
    }
    langConfig.internalChangeLanguage = setLangConfig
    CompositionLocalProvider(
        LanguageAmbient provides langConfig
    ) {
        content.invoke()
    }
}

data class AppLangConfig(val language: LanguageDefinition) {
    internal lateinit var internalChangeLanguage: ((AppLangConfig) -> Unit)

    fun changeLanguage(language: LanguageDefinition) {
        ResolverI18n.changeLang(language)
        internalChangeLanguage.invoke(this.copy(language = language))
    }
}
