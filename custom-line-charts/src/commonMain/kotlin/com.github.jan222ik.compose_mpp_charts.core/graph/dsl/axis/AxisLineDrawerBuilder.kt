package com.github.jan222ik.compose_mpp_charts.core.graph.dsl.axis

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import com.github.jan222ik.compose_mpp_charts.core.axis.drawer.simpleAxisLineDrawer
import com.github.jan222ik.compose_mpp_charts.core.line.ILineDrawer

@AxisLineDrawerScopeMarker
data class AxisLineDrawerBuilder(
    var brush: Brush = SolidColor(Color.Black),
    var strokeWidth: Float = Stroke.HairlineWidth,
    var alpha: Float = 1f,
    var pathEffect: PathEffect? = null,
    var cap: StrokeCap = Stroke.DefaultCap,
    var colorFilter: ColorFilter? = null,
    var blendMode: BlendMode = DrawScope.DefaultBlendMode,
) {
    fun toDrawer(): ILineDrawer {
        return simpleAxisLineDrawer(
            brush = brush,
            strokeWidth = strokeWidth,
            alpha = alpha,
            pathEffect = pathEffect,
            cap = cap,
            colorFilter = colorFilter,
            blendMode = blendMode
        )
    }
}
