package com.github.jan222ik.compose_mpp_charts.core.axis.ordinate

import com.github.jan222ik.compose_mpp_charts.core.axis.IAxisRenderer

/**
 * [IAxisRenderer] with ordinate specific constants.
 */
fun interface IOrdinateIAxisRenderer : IAxisRenderer {
    companion object {
        const val Right = 1f
        const val Left = 0f
    }
}
