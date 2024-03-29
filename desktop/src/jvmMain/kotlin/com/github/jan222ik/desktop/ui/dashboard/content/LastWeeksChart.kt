@file:Suppress("FunctionName", "DuplicatedCode")

package com.github.jan222ik.desktop.ui.dashboard.content

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.compose_mpp_charts.core.graph.canvas.Chart
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartLabelSlot
import com.github.jan222ik.compose_mpp_charts.core.grid.intGridRenderer
import com.github.jan222ik.compose_mpp_charts.core.labels.provoder.intLabelProvider
import com.github.jan222ik.compose_mpp_charts.core.line.lineRenderer
import com.github.jan222ik.compose_mpp_charts.core.line.simplePathDrawer
import com.github.jan222ik.compose_mpp_charts.core.point.drawer.circularPointDrawer
import com.github.jan222ik.compose_mpp_charts.core.point.renderer.pointRenderer
import com.github.jan222ik.compose_mpp_charts.core.series.compositeRenderer
import com.github.jan222ik.compose_mpp_charts.core.viewport.Viewport
import com.github.jan222ik.desktop.ui.util.i18n.LanguageAmbient
import com.github.jan222ik.desktop.ui.util.i18n.i18n
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@HasDoc
@OptIn(ExperimentalUnsignedTypes::class)
@Composable
fun LastWeeksChart(statsIntent: DashboardStatsIntent) {
    BaseDashboardCard {
        val langConfig = LanguageAmbient.current
        val dateLabels = remember(langConfig) { i18n.str.dashboard.weeklyChart.daysOfWeek.resolve().split(" ") }
        /*
        val lineChartData = remember {
            listOf(
                DataPoint(x = -1f, y = 110f),
                DataPoint(x = 0f, y = 100f),
                DataPoint(x = 1f, y = 84f),
                DataPoint(x = 2f, y = 80f),
                DataPoint(x = 3f, y = 90f),
                DataPoint(x = 4f, y = 100f),
                //DataPoint(x = 5f, y = 0f),
                DataPoint(x = 6f, y = 80f),
            )
        }
         */
        val lineChartData = statsIntent.weekData.collectAsState()
        val maxViewport = remember(lineChartData.value) {
            val data = lineChartData.value
            val pair = if (data.isEmpty()) {
                0f to 20f
            } else {
                data.minOf { it.y } to data.maxOf { it.y }
            }
            mutableStateOf(pair)
        }
        val (minVP, maxVP) = remember(maxViewport.value) { maxViewport.value }
        Layout(
            content = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart),
                        text = +i18n.str.dashboard.weeklyChart.chartsTitle,
                        style = MaterialTheme.typography.h5
                    )
                    TextButton(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        onClick = {
                            println("Pressed show more of charts")
                        }
                    ) {
                        Text(text = +i18n.str.dashboard.weeklyChart.showMore)
                    }
                }
                val viewport = remember(minVP, maxVP) {
                    mutableStateOf(
                        Viewport(
                            minX = -0.5f,
                            maxX = 6.5f,
                            minY = max(minVP - 20f, -0.5f),
                            maxY = maxVP + 20f
                        )
                    )
                }
                val pointColor = MaterialTheme.colors.primary
                val lineColor = pointColor.copy(alpha = 0.6f)
                Chart(
                    modifier = Modifier.padding(vertical = 16.dp),
                    viewport = viewport
                ) {
                    abscissaAxis { }
                    ordinateAxis { }
                    val step = 10
                    grid(renderer = intGridRenderer(stepAbscissa = step))
                    series(
                        data = lineChartData.value,
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
                        labelProvider = { min: Float, max: Float ->
                            if (max < 0 || min.roundToInt() > 7) listOf()
                            else {
                                val dateLabelsSorted = when(
                                    val weekdayIdxOffset = LocalDate.now().dayOfWeek.value - 1
                                ) {
                                    DayOfWeek.SUNDAY.value.dec() -> dateLabels
                                    else -> listOf(
                                        *dateLabels.subList(weekdayIdxOffset.inc(), dateLabels.size).toTypedArray(),
                                        *dateLabels.subList(0, weekdayIdxOffset.inc()).toTypedArray(),
                                    )
                                }
                                val sIdx = max(0.0, min.toDouble()).toInt()
                                val eIdx = min(7.0, max.roundToInt().toDouble()).toInt()
                                dateLabelsSorted.subList(sIdx, eIdx).mapIndexed { idx, it -> it to sIdx + idx.toFloat() }
                            }
                        }
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
                                    Text(text = "${shape.dataPoint.y} WPM")
                                }
                            }
                            true
                        } ?: false
                    }
                    /*
                    linearFunction(
                        renderer = linearFunctionRenderer(
                            lineDrawer = simpleAxisLineDrawer(
                                strokeWidth = 3f,
                                brush = SolidColor(Color.Black)
                            ),
                            linearFunctionPointProvider = linearFunctionPointProvider { x ->
                                1.2f * x + 80f
                            }
                        )
                    )
                     */
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Surface(
                        modifier = Modifier.padding(end = 5.dp).size(10.dp),
                        shape = CircleShape.copy(CornerSize(5.dp)),
                        color = MaterialTheme.colors.primary
                    ) {}
                    Text(+i18n.str.dashboard.weeklyChart.description)
                }
            }
        ) { measurables, constraints ->
            val pHeader = measurables[0].measure(Constraints(maxWidth = constraints.maxWidth))
            val pLegend = measurables[2].measure(Constraints(maxWidth = constraints.maxWidth))
            val pChart = measurables[1].measure(
                Constraints(
                    maxWidth = constraints.maxWidth,
                    maxHeight = constraints.maxHeight.minus(pHeader.height).minus(pLegend.height).coerceAtLeast(0)
                )
            )
            layout(
                width = constraints.maxWidth,
                height = constraints.maxHeight
            ) {
                pHeader.place(IntOffset.Zero)
                pChart.place(x = 0, y = pHeader.height)
                pLegend.place(x = 0, y = constraints.maxHeight - pLegend.height)
            }
        }
    }
}
