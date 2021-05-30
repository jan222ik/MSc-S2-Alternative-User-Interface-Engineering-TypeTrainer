package com.github.jan222ik.compose_mpp_charts.core.grid

import com.github.jan222ik.compose_mpp_charts.core.graph.canvas.ChartDrawScope

/**
 * IGridRenderer defines a render function on the ChartDrawScope.
 */
fun interface IGridRenderer {
    /**
     * Render call for a grid for given [ChartDrawScope].
     */
    fun ChartDrawScope.render()
}
