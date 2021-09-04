package com.github.jan222ik.desktop

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi
import com.github.jan222ik.desktop.ui.util.debug.ifDebug

@ExperimentalComposeUiApi
@ExperimentalAnimationApi
@KtorExperimentalAPI
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalFoundationApi
object MainDebug {

    @JvmStatic
    fun main(args: Array<String>) {
        System.setProperty("debug", "true")
        ifDebug {
            println("Debug Pre Checks")
            println("Check for translation keys:")
            com.github.jan222ik.desktop.ui.util.i18n.main()
            println("-".repeat(80))
        }
        DesktopApplication.start()
    }
}
