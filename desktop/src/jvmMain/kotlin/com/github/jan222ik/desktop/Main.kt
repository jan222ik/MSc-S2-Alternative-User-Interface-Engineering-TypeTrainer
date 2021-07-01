@file:Suppress("FunctionName")

package com.github.jan222ik.desktop

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.ExperimentalFoundationApi
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalAnimationApi
@KtorExperimentalAPI
@ExperimentalCoroutinesApi
@ExperimentalStdlibApi
@ExperimentalFoundationApi
class Main {
    companion object {
        @JvmStatic
        fun main(args: Array<String>) {
            DesktopApplication.start()
        }
    }
}



