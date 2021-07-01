package com.github.jan222ik.desktop.ui.util.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.desktop.ui.util.debug.ifDebug

@HasDoc
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
@HasDoc
data class RequiresTranslationI18N(val value: String) : KeyI18N("") {
    override fun resolve(): String {
        ifDebug {
            System.err.
            println("i18n > Requires Translation - \"$value\"")
        }
        return value
    }
}

@HasDoc
data class LocalTranslationI18N(val eng: String, val ger: String) : KeyI18N("") {
    override fun resolve(): String {
        ifDebug {
            //System.err.
            //println("i18n > Local Translation - \"$eng\", \"$ger\"")
        }
        return when (ResolverI18n.currentLang) {
            LanguageDefinition.English -> eng
            LanguageDefinition.German -> ger
        }
    }
}
