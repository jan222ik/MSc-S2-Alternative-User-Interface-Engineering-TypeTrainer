package ui.util.i18n

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import ui.util.debug.ifDebug

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
    override fun resolve(): String {
        ifDebug {
            println("i18n > Requires Translation - \"$value\"")
        }
        return value
    }
}
