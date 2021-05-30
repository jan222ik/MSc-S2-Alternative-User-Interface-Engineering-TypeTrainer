package com.github.jan222ik.compose_mpp_charts.core.point.renderer

import com.github.jan222ik.compose_mpp_charts.core.point.drawer.IPointDrawer
import com.github.jan222ik.compose_mpp_charts.core.point.drawer.circularPointDrawer
import com.github.jan222ik.compose_mpp_charts.core.series.ISeriesRenderer

fun pointRenderer(
    drawer: IPointDrawer = circularPointDrawer(radius = 5f)
) = ISeriesRenderer { series ->
    return@ISeriesRenderer series
        .filter { context.viewport.contains(it.x, it.y) }
        .mapIndexed { index, dataPoint ->
            val chartPoint = dataPoint.asDrawPoint(context)
            with(drawer) { drawPoint(index = index, dataPoint = dataPoint, drawPoint = chartPoint) }
        }.filterNotNull()
}
