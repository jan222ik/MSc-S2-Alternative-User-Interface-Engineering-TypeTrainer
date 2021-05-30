package com.github.jan222ik.compose_mpp_charts.core.graph.canvas

import androidx.compose.ui.graphics.drawscope.DrawScope

class ChartDrawScope(
    drawScope: DrawScope,
    val context: ChartContext
) : DrawScope by drawScope
