package com.github.jan222ik.compose_mpp_charts.core.point.drawer

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import com.github.jan222ik.compose_mpp_charts.core.interaction.BoundingCircle

fun circularPointDrawer(
    brush: Brush = SolidColor(Color.Black),
    radius: Float,
    alpha: Float = 1f,
    style: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) = IPointDrawer { _, dataPoint, drawPoint ->
    drawCircle(
        brush = brush,
        radius = radius,
        center = drawPoint.offset,
        alpha = alpha,
        style = style,
        colorFilter = colorFilter,
        blendMode = blendMode
    )
    return@IPointDrawer BoundingCircle(
        dataPoint = dataPoint,
        anchorRenderPoint = drawPoint,
        center = drawPoint,
        radius = radius
    )
}
