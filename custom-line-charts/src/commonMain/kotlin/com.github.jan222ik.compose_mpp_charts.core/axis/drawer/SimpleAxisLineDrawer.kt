package com.github.jan222ik.compose_mpp_charts.core.axis.drawer

import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke

fun simpleAxisLineDrawer(
    brush: Brush = SolidColor(Color.Black),
    strokeWidth: Float = Stroke.HairlineWidth,
    alpha: Float = 1f,
    pathEffect: PathEffect? = null,
    cap: StrokeCap = Stroke.DefaultCap,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) = IAxisLineDrawer { start, end ->
    drawLine(
        brush = brush,
        strokeWidth = strokeWidth,
        pathEffect = pathEffect,
        cap = cap,
        alpha = alpha,
        start = start,
        end = end,
        colorFilter = colorFilter,
        blendMode = blendMode
    )
}
