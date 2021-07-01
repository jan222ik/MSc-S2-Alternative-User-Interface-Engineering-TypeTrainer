package com.github.jan222ik.desktop.ui.util.i18n

import java.util.*

object ResolverI18n {
    private val defaultLang: ResourceBundle = ResourceBundle.getBundle("strings", Locale.ROOT)
    var currentLang: LanguageDefinition = LanguageDefinition.English
    var currentLangBuild = defaultLang

    fun resolve(key: KeyI18N): String? {
        return currentLangBuild[key.key]
    }

    fun changeLang(languageDefinition: LanguageDefinition) {
        if (languageDefinition == currentLang) return
        currentLang = languageDefinition
        currentLangBuild = languageDefinition.buildMap()
    }

    private fun LanguageDefinition.buildMap(): ResourceBundle {
        return ResourceBundle.getBundle("strings", locale) ?: defaultLang
    }

    private operator fun ResourceBundle.get(key: String): String? {
        return try {
            this.getString(key)
        } catch (npe: NullPointerException) {
            null
        } catch (e: MissingResourceException) {
            null
        }
    }

}
