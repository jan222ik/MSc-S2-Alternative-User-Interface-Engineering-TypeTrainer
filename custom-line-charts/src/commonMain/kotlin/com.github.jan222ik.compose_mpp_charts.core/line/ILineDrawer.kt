package com.github.jan222ik.compose_mpp_charts.core.line

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.drawscope.DrawScope

fun interface ILineDrawer {
    fun DrawScope.draw(start: Offset, end: Offset)
}
