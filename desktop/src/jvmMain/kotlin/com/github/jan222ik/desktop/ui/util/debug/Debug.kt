package com.github.jan222ik.desktop.ui.util.debug

import androidx.compose.runtime.Composable

object Debug {
    private const val debugPropName = "debug"
    private const val debugPropTrueValue = "true"
    val isDebug by lazy { System.getProperty(debugPropName) == debugPropTrueValue }
}

fun ifDebug(content: () -> Unit) {
    if (Debug.isDebug) {
        content.invoke()
    }
}

@Composable
fun ifDebugCompose(content: @Composable () -> Unit) {
    if (Debug.isDebug) {
        content.invoke()
    }
}
