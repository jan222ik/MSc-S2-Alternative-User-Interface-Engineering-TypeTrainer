package com.github.jan222ik.compose_mpp_charts.core.point.drawer

import androidx.compose.ui.graphics.drawscope.DrawScope
import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import com.github.jan222ik.compose_mpp_charts.core.data.DrawPoint
import com.github.jan222ik.compose_mpp_charts.core.interaction.IBoundingShape2D

fun interface IPointDrawer {
    fun DrawScope.drawPoint(index: Int, dataPoint: DataPoint, drawPoint: DrawPoint) : IBoundingShape2D?
}
