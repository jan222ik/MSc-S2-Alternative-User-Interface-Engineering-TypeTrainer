package com.github.jan222ik.compose_mpp_charts.core.axis.ordinate

import androidx.compose.ui.geometry.Offset
import com.github.jan222ik.compose_mpp_charts.core.axis.drawer.simpleAxisLineDrawer
import com.github.jan222ik.compose_mpp_charts.core.line.ILineDrawer

fun ordinateAxisRenderer(
    location: Float = IOrdinateIAxisRenderer.Left,
    axisLineDrawer: ILineDrawer = simpleAxisLineDrawer()
) = IOrdinateIAxisRenderer {
    val x = location * size.width
    with(axisLineDrawer) {
        draw(
            start = Offset(x = x, y = 0f),
            end = Offset(x = x, y = size.height)
        )
    }
}

typealias IVerticalAxisRenderer = IOrdinateIAxisRenderer


fun verticalAxisRenderer(
    location: Float = IVerticalAxisRenderer.Left,
    axisLineDrawer: ILineDrawer = simpleAxisLineDrawer()
) = ordinateAxisRenderer(location, axisLineDrawer)
