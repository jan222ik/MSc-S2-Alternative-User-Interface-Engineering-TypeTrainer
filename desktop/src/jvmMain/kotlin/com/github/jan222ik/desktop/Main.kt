@file:Suppress("FunctionName")

package com.github.jan222ik.desktop

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.ExperimentalComposeUiApi
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalAnimationApi
@KtorExperimentalAPI
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalFoundationApi
class Main {
    companion object {
        @ExperimentalComposeUiApi
        @JvmStatic
        fun main(args: Array<String>) {
            DesktopApplication.start()
        }
    }
}



