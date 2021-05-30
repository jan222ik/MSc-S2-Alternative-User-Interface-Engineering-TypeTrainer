package com.github.jan222ik.compose_mpp_charts.core.axis

import androidx.compose.ui.graphics.drawscope.DrawScope

/**
 *  An AxisRenderer defines a render method with which the axis is drawn into the drawscope.
 */
fun interface IAxisRenderer {
    fun DrawScope.render()
}
