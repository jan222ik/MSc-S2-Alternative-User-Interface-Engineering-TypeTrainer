package com.github.jan222ik.desktop.ui.util.i18n

import com.github.jan222ik.desktop.textgen.database.schema.UserSettings
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
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
        transaction {
            UserSettings.update(
                where = {
                    UserSettings.id eq UserSettings.CONST_ID
                },
                body = {
                    it[locale] = when (languageDefinition) {
                        LanguageDefinition.English -> "eng"
                        LanguageDefinition.German -> "ger"
                    }
                }
            )
        }
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
