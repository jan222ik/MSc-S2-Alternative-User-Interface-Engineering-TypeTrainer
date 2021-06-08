@file:Suppress("FunctionName")

package ui.exercise.results.analysis.keyinterval

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.unit.dp
import com.github.jan222ik.compose_mpp_charts.core.axis.drawer.simpleAxisLineDrawer
import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import com.github.jan222ik.compose_mpp_charts.core.graph.canvas.Chart
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartLabelSlot
import com.github.jan222ik.compose_mpp_charts.core.grid.intGridRenderer
import com.github.jan222ik.compose_mpp_charts.core.labels.provoder.intLabelProvider
import com.github.jan222ik.compose_mpp_charts.core.line.lineRenderer
import com.github.jan222ik.compose_mpp_charts.core.line.simplePathDrawer
import com.github.jan222ik.compose_mpp_charts.core.math.linearFunctionPointProvider
import com.github.jan222ik.compose_mpp_charts.core.math.linearFunctionRenderer
import com.github.jan222ik.compose_mpp_charts.core.point.drawer.circularPointDrawer
import com.github.jan222ik.compose_mpp_charts.core.point.renderer.pointRenderer
import com.github.jan222ik.compose_mpp_charts.core.series.compositeRenderer
import com.github.jan222ik.compose_mpp_charts.core.viewport.Viewport

@Composable
fun KeyIntervalChart(data: List<DataPoint>, modifier: Modifier = Modifier) {
    val (yMinVP, yMaxVP) = remember(data) { data.minOf { it.y } to data.maxOf { it.y } }
    val xMaxVP = remember(data) { data.maxOf { it.x } }
    val yAvg = remember(data) { data.filter { it.y != 0f }.let {
        it.sumByDouble { item -> item.y.toDouble() }.div(it.size).toFloat() }
    }
    val maxViewport = remember(xMaxVP, yMaxVP, yMinVP) {
        Viewport(
            minX = -0.5f,
            maxX = xMaxVP + 0.5f,
            minY = yMinVP * 0.9f,
            maxY = yMaxVP * 1.1f
        )
    }
    val viewport = remember {
        mutableStateOf(
            Viewport(
                minX = -0.5f,
                maxX = 100f,
                minY = yMinVP * 0.9f,
                maxY = yMaxVP * 1.1f
            )
        )
    }
    val pointColor = MaterialTheme.colors.primary
    val bgColor = MaterialTheme.colors.background
    val lineColor = pointColor.copy(alpha = 0.6f)
    Chart(
        modifier = modifier.padding(vertical = 16.dp),
        viewport = viewport,
        maxViewport = maxViewport
    ) {
        abscissaAxis { }
        ordinateAxis { }
        val step = 50
        grid(renderer = intGridRenderer(stepAbscissa = step, ordinateDrawer = null))
        series(
            data = data,
            renderer = compositeRenderer(
                lineRenderer(
                    simplePathDrawer(brush = SolidColor(lineColor))
                ),
                pointRenderer(
                    drawer = circularPointDrawer(
                        radius = 5f,
                        brush = SolidColor(pointColor)
                    )
                )
            )
        )
        label(slot = ChartLabelSlot.START, labelProvider = intLabelProvider(step = step.toUInt())) {
            Text(text = it.first)
        }
        label(
            slot = ChartLabelSlot.BOTTOM,
            labelProvider = intLabelProvider(step = 3u)
        ) {
            Text(text = it.first)
        }
        onClickPopupLabel { _, shape ->
            shape?.let {
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
                        Text(text = "${shape.dataPoint.y} ms")
                    }
                }
                true
            } ?: false
        }

        linearFunction(
            renderer = linearFunctionRenderer(
                lineDrawer = simpleAxisLineDrawer(
                    strokeWidth = 3f,
                    brush = SolidColor(Color.Black)
                ),
                linearFunctionPointProvider = linearFunctionPointProvider { yAvg }
            )
        )
    }
}
