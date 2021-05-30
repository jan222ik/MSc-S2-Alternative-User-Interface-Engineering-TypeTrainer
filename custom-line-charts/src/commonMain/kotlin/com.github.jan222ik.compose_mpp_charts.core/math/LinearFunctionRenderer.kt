package com.github.jan222ik.compose_mpp_charts.core.math

import com.github.jan222ik.compose_mpp_charts.core.axis.drawer.simpleAxisLineDrawer
import com.github.jan222ik.compose_mpp_charts.core.line.ILineDrawer

fun linearFunctionRenderer(
    lineDrawer: ILineDrawer = simpleAxisLineDrawer(),
    linearFunctionPointProvider: ILinearFunctionPointProvider
) = ILinearFunctionRenderer {
    with(linearFunctionPointProvider) {
        val (start, end) = provide()
        with(lineDrawer) {
            draw(start, end)
        }
    }
}
