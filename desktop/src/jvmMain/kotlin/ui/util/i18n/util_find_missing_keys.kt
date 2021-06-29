package ui.util.i18n

import kotlin.reflect.KClass
import kotlin.reflect.typeOf

/**
 * Execute to extract missing resource paths.
 */
@ExperimentalStdlibApi
fun main() {
    // Set root resource set
    ResolverI18n.changeLang(LanguageDefinition.English)
    var count = 0
    exploreClasses(kClass = i18n::class) {
        if (it is RequiresTranslationI18N) {
            println(it)
        } else {
            try {
                it.resolve()
            } catch (e: IllegalStateException) {
                println(it.key + "=")
                count++
            }
        }
    }
    println("\nDone - Missing Keys: $count")
}

@ExperimentalStdlibApi
fun exploreClasses(kClass: KClass<*>, onPath: (KeyI18N) -> Unit) {
    when {
        kClass.isSealed -> {
            kClass.nestedClasses.forEach { exploreClasses(kClass = it, onPath = onPath) }
        }
        kClass.objectInstance != null -> {
            val instance = kClass.objectInstance
            kClass.members
                .filter { it.returnType == typeOf<KeyI18N>() && it.name != "i18Key" }
                .map { it.call(instance) as KeyI18N }
                .forEach(onPath)
        }
    }
}
