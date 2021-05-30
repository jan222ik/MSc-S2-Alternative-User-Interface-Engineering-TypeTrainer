package com.github.jan222ik.compose_mpp_charts.core.series

import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import com.github.jan222ik.compose_mpp_charts.core.graph.canvas.ChartDrawScope
import com.github.jan222ik.compose_mpp_charts.core.interaction.IBoundingShape2D

fun interface ISeriesRenderer {
    fun ChartDrawScope.render(series: List<DataPoint>) : List<IBoundingShape2D>
}
