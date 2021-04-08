package com.github.jan222ik.common.ui.components

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun TypeTrainerTheme(content: @Composable () -> Unit) {
    val dark = darkColors(
        background = Color(0xFF303747),
        surface = Color(0xFF31445F),
        primary = Color(0xFF839AD3),
        onPrimary = Color.White
    )

    MaterialTheme(colors = dark) {
        content.invoke()
    }
}