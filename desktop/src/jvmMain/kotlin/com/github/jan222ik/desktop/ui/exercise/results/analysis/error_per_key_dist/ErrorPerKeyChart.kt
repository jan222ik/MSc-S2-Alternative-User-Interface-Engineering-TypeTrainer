@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.results.analysis.error_per_key_dist

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.unit.dp
import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import com.github.jan222ik.compose_mpp_charts.core.data.DrawPoint
import com.github.jan222ik.compose_mpp_charts.core.graph.canvas.Chart
import com.github.jan222ik.compose_mpp_charts.core.graph.canvas.ChartDrawScope
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartLabelSlot
import com.github.jan222ik.compose_mpp_charts.core.grid.intGridRenderer
import com.github.jan222ik.compose_mpp_charts.core.interaction.IBoundingShape2D
import com.github.jan222ik.compose_mpp_charts.core.labels.provoder.ILabelProvider
import com.github.jan222ik.compose_mpp_charts.core.labels.provoder.intLabelProvider
import com.github.jan222ik.compose_mpp_charts.core.line.getLineInViewport
import com.github.jan222ik.compose_mpp_charts.core.series.ISeriesRenderer
import com.github.jan222ik.compose_mpp_charts.core.series.compositeRenderer
import com.github.jan222ik.compose_mpp_charts.core.viewport.Viewport
import com.github.jan222ik.desktop.textgen.error.ChartErrorKey
import java.lang.Float.min

@Composable
fun ErrorPerKeyChart(chartData: List<ChartErrorKey>, modifier: Modifier = Modifier) {
    val data = remember(chartData) { chartData.map { it.dataPoint } }
    val xMaxVP = remember(data) { data.size }

    val maxY = remember(data) { data.maxOf { it.y } }
    val maxViewport = remember(xMaxVP, maxY) {
        Viewport(
            minX = -0.5f,
            maxX = xMaxVP + 0.5f,
            minY = 0f,
            maxY = min(100f, maxY * 1.1f),
        )
    }
    val viewport = remember(data, maxViewport) {
        mutableStateOf(
            Viewport(
                minX = -0.5f,
                maxX = xMaxVP + 0.5f,
                minY = maxViewport.minY,
                maxY = maxViewport.maxY
            )
        )
    }
    val pointColor = MaterialTheme.colors.primary
    val step = remember(viewport.value) {
        (viewport.value.height / 3).toInt()
    }
    Chart(
        modifier = modifier.padding(vertical = 16.dp),
        viewport = viewport,
        maxViewport = maxViewport
    ) {
        abscissaAxis { }
        ordinateAxis { }

        grid(renderer = intGridRenderer(stepAbscissa = step, ordinateDrawer = null))
        series(
            data = data,
            renderer = compositeRenderer(
                barRenderer(barDrawer(width = 40f, brush = SolidColor(pointColor)))
            )
        )
        label(slot = ChartLabelSlot.START, labelProvider = intLabelProvider(step = step.toUInt())) {
            Text(text = it.first + "%")
        }
        label(
            slot = ChartLabelSlot.BOTTOM,
            labelProvider = ILabelProvider { min, max ->
                IntProgression
                    .fromClosedRange(rangeStart = min.toInt(), rangeEnd = max.toInt(), step = 1)
                    .map {
                        if (it < chartData.size) {
                            val charStr = when (
                                val c = chartData[it].char
                            ) {
                                " " -> "\"$c\""
                                else -> c
                            }
                            charStr to it.toFloat()
                        } else {
                            "" to it.toFloat()
                        }
                    }
            }
        ) {
            Text(text = it.first)
        }
        onClickPopupLabel { _, shape ->
            shape?.let {
                val d = chartData[it.dataPoint.x.toInt()]
                Column {
                    Canvas(
                        modifier = Modifier
                            .size(15.dp)
                            .align(Alignment.CenterHorizontally)
                            .offset(x = (-7.5).dp)
                    ) {
                        drawPath(
                            path = Path().apply {
                                moveTo(x = 0f, y = size.height)
                                lineTo(x = size.width, y = size.height)
                                lineTo(x = center.x, y = 0f)
                            },
                            brush = SolidColor(pointColor),
                        )
                    }
                    Surface(color = pointColor) {
                        Column {
                            Text(text = "${it.dataPoint.y} %")
                            Text(text = "Amount: ${d.amount}")
                        }
                    }
                }
                true
            } ?: false
        }
    }
}

fun barRenderer(drawer: IBarDrawer = barDrawer(50f)) = ISeriesRenderer { series ->
    series.getLineInViewport(context.viewport)
        .map { it.asDrawPoint(context) }
        .let { drawPoints ->
            with(this) {
                with(drawer) {
                    drawPoints.map { draw(it) } }
            }
        }
}

fun interface IBarDrawer {
    fun ChartDrawScope.draw(data: DrawPoint): IBoundingShape2D
}

fun barDrawer(
    width: Float,
    brush: Brush = SolidColor(Color.Black),
    alpha: Float = 1f,
    drawStyle: DrawStyle = Fill,
    colorFilter: ColorFilter? = null,
    blendMode: BlendMode = DrawScope.DefaultBlendMode,
) = IBarDrawer { point ->
    val topLeft = Offset(
        x = point.x - width.div(2),
        y = point.y
    )
    val size = Size(
        width = width,
        height = context.canvasSize.height - point.y
    )
    drawRect(
        brush = brush,
        topLeft = topLeft,
        size = size,
        alpha = alpha,
        style = drawStyle,
        colorFilter = colorFilter,
        blendMode = blendMode
    )
    BoundingRect(
        dataPoint = point.asDataPoint(context),
        anchorRenderPoint = point,
        topLeftOffset = topLeft,
        size = size
    )
}


data class BoundingRect(
    override val dataPoint: DataPoint,
    override val anchorRenderPoint: DrawPoint,
    val topLeftOffset: Offset,
    val size: Size
) : IBoundingShape2D {
    override fun containtsOffset(offset: Offset): Boolean {
        val startX = topLeftOffset.x
        val endX = startX.plus(size.width)
        val startY = topLeftOffset.y
        val endY = startY.plus(size.height)
        val inX = (startX..endX).contains(offset.x)
        val inY = (startY..endY).contains(offset.y)
        return inX && inY
    }
}
