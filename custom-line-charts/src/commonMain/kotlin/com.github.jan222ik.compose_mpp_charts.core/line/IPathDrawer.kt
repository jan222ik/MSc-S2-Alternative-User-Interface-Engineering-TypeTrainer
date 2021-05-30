package com.github.jan222ik.compose_mpp_charts.core.line

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.jan222ik.compose_mpp_charts.core.data.DrawPoint

fun interface IPathDrawer {
    fun DrawScope.draw(points: List<DrawPoint>)
}
