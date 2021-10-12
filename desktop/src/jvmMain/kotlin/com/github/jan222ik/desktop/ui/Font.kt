package com.github.jan222ik.desktop.ui

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.platform.Font

object Fonts {
    val monofont = FontFamily(
        Font(
            resource = "fonts/ubuntu-mono/UbuntuMono-Bold.ttf",
            weight = FontWeight.W400,
            style = FontStyle.Normal
        )
    )
}
