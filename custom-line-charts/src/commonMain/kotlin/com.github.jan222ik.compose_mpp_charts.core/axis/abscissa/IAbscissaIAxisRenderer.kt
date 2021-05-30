package com.github.jan222ik.compose_mpp_charts.core.axis.abscissa

import com.github.jan222ik.compose_mpp_charts.core.axis.IAxisRenderer

/**
 * [IAxisRenderer] with abscissa specific constants.
 */
fun interface IAbscissaIAxisRenderer : IAxisRenderer {
    companion object {
        const val Top = 0.0f
        const val Bottom = 1.0f
    }
}
