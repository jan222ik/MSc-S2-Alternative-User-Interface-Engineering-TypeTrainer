@file:Suppress("FunctionName")

package com.github.jan222ik.compose_mpp_charts.core.graph.dsl.axis

import com.github.jan222ik.compose_mpp_charts.core.axis.drawer.simpleAxisLineDrawer
import com.github.jan222ik.compose_mpp_charts.core.axis.ordinate.IOrdinateIAxisRenderer
import com.github.jan222ik.compose_mpp_charts.core.axis.ordinate.ordinateAxisRenderer
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartScopeMarker
import com.github.jan222ik.compose_mpp_charts.core.line.ILineDrawer

@ChartScopeMarker
@AxisLineDrawerScopeMarker
class OrdinateBuilder(
    var location: Float = IOrdinateIAxisRenderer.Left,
    var axisLineDrawer: ILineDrawer = simpleAxisLineDrawer()
) {
    // Capital because they are constants
    @Suppress("PropertyName")
    val Left = IOrdinateIAxisRenderer.Left

    @Suppress("PropertyName")
    val Right = IOrdinateIAxisRenderer.Right

    fun drawer(
        builder: AxisLineDrawerBuilder = AxisLineDrawerBuilder(),
        config: (AxisLineDrawerBuilder.() -> Unit)? = null
    ): AxisLineDrawerBuilder {
        config?.invoke(builder)
        return builder.also {
            axisLineDrawer = simpleAxisLineDrawer(
                brush = it.brush,
                strokeWidth = it.strokeWidth,
                alpha = it.alpha,
                pathEffect = it.pathEffect,
                cap = it.cap,
                colorFilter = it.colorFilter,
                blendMode = it.blendMode
            )
        }
    }

    fun build(): IOrdinateIAxisRenderer {
        return ordinateAxisRenderer(
            location = location,
            axisLineDrawer = axisLineDrawer
        )
    }
}
