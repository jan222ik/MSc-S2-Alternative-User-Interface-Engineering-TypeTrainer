@file:Suppress("FunctionName")

package com.github.jan222ik.common.ui

import android.app.Activity
import android.content.Context
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Functions
import androidx.compose.material.icons.filled.Phonelink
import androidx.compose.material.icons.filled.SentimentSatisfied
import androidx.compose.material.icons.filled.Whatshot
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.dto.MobileStatsData
import com.github.jan222ik.common.dto.SHARED_STATS_PREF_KEY
import com.github.jan222ik.common.ui.components.Logo
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.common.ui.dashboard.IconDashboardCard
import com.github.jan222ik.common.ui.router.MobileRouterAmbient
import com.github.jan222ik.common.ui.router.MobileRoutes
import com.github.jan222ik.compose_mpp_charts.core.axis.drawer.simpleAxisLineDrawer
import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import com.github.jan222ik.compose_mpp_charts.core.graph.canvas.Chart
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartLabelSlot
import com.github.jan222ik.compose_mpp_charts.core.grid.intGridRenderer
import com.github.jan222ik.compose_mpp_charts.core.interaction.BoundingCircle
import com.github.jan222ik.compose_mpp_charts.core.labels.provoder.intLabelProvider
import com.github.jan222ik.compose_mpp_charts.core.line.lineRenderer
import com.github.jan222ik.compose_mpp_charts.core.line.simplePathDrawer
import com.github.jan222ik.compose_mpp_charts.core.point.drawer.IPointDrawer
import com.github.jan222ik.compose_mpp_charts.core.point.drawer.circularPointDrawer
import com.github.jan222ik.compose_mpp_charts.core.point.renderer.pointRenderer
import com.github.jan222ik.compose_mpp_charts.core.series.compositeRenderer
import com.github.jan222ik.compose_mpp_charts.core.viewport.Viewport
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.time.DayOfWeek
import java.time.LocalDate
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@HasDoc
@Composable
fun MobileMenu(activity: Activity, lostConnection: Boolean = false) {
    val data: MobileStatsData? = activity
        .getPreferences(Context.MODE_PRIVATE)
        .getString(SHARED_STATS_PREF_KEY, null)
        ?.let {
            try {
                Json.decodeFromString<MobileStatsData>(it)
            } catch (ex: Exception) {
                null
            }
        }
    val router = MobileRouterAmbient.current
    var lostConnection = lostConnection
    Column {
        Box(modifier = Modifier.fillMaxWidth()) {
            Wave(cornerSize = 32.dp)
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .padding(bottom = 8.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Logo(modifier = Modifier.height(100.dp))
                Text(
                    text = "TypeTrainer",
                    style = MaterialTheme.typography.h3
                )
            }
        }
        Card(
            modifier = Modifier.fillMaxHeight(),
            shape = RoundedCornerShape(topEnd = 32.dp),
            backgroundColor = MaterialTheme.colors.surface,
            elevation = 0.dp
        ) {
            Scaffold(
                backgroundColor = MaterialTheme.colors.surface,
                content = { paddingValues ->
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp, bottom = 30.dp.plus(paddingValues.calculateBottomPadding()))
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        BaseDashboardCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {
                            Column {
                                val isMockData = remember { mutableStateOf(false) }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                                ) {
                                    Text(text = "Weeks Stats")
                                    Text(
                                        text = "[Mock Data]",
                                        style = MaterialTheme.typography.overline.copy(Color.Gray)
                                    )
                                }
                                val dateLabels = remember { "M T W T F S S".split(" ") }
                                val lineChartData = remember {
                                    val mockData = listOf(
                                        DataPoint(0f, 100f),
                                        DataPoint(1f, 113f),
                                        DataPoint(2f, 109f),
                                        DataPoint(3f, 112f),
                                    )
                                    val actualData = data
                                        ?.weeklyDataPoints
                                        ?.map { it.toDataPoint() }
                                        ?.also { isMockData.value = false }
                                    mutableStateOf(actualData ?: mockData.also { isMockData.value = true })
                                }
                                val maxViewport = remember(lineChartData.value) {
                                    val d = lineChartData.value
                                    val pair = if (d.isEmpty()) {
                                        0f to 20f
                                    } else {
                                        d.minOf { it.y } to d.maxOf { it.y }
                                    }
                                    mutableStateOf(pair)
                                }
                                val (minVP, maxVP) = remember(maxViewport.value) { maxViewport.value }

                                val viewport = remember(minVP, maxVP) {
                                    mutableStateOf(
                                        Viewport(
                                            minX = -0.5f,
                                            maxX = 6.8f,
                                            minY = max(minVP - 20f, -0.5f),
                                            maxY = maxVP + 20f
                                        )
                                    )
                                }
                                val pointColor = MaterialTheme.colors.primary
                                val lineColor = pointColor.copy(alpha = 0.6f)
                                val circleDrawer = circularPointDrawer(
                                    brush = SolidColor(pointColor),
                                    radius = 10f
                                )
                                Chart(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(250.dp),
                                    viewport = viewport,
                                ) {
                                    abscissaAxis {
                                        axisLineDrawer = simpleAxisLineDrawer(
                                            brush = SolidColor(Color.White)
                                        )
                                    }
                                    ordinateAxis {
                                        axisLineDrawer = simpleAxisLineDrawer(
                                            brush = SolidColor(Color.White)
                                        )
                                    }
                                    val step = 10
                                    grid(renderer = intGridRenderer(stepAbscissa = step))
                                    series(
                                        data = lineChartData.value,
                                        renderer = compositeRenderer(
                                            lineRenderer(
                                                simplePathDrawer(
                                                    brush = SolidColor(lineColor),
                                                    style = Stroke(width = 7f, cap = StrokeCap.Round)
                                                )
                                            ),
                                            pointRenderer(
                                                drawer = IPointDrawer { index, dataPoint, drawPoint ->
                                                    with(circleDrawer) { drawPoint(index, dataPoint, drawPoint) }
                                                    BoundingCircle(
                                                        dataPoint = dataPoint,
                                                        anchorRenderPoint = drawPoint,
                                                        center = drawPoint,
                                                        radius = 20f
                                                    )
                                                }
                                            )
                                        )
                                    )
                                    label(
                                        slot = ChartLabelSlot.START,
                                        labelProvider = intLabelProvider(step = step.toUInt())
                                    ) {
                                        Text(text = it.first)
                                    }
                                    label(
                                        slot = ChartLabelSlot.BOTTOM,
                                        labelProvider = { min: Float, max: Float ->
                                            if (max < 0 || min.roundToInt() > 7) listOf()
                                            else {
                                                val dateLabelsSorted = when (
                                                    val weekdayIdxOffset = LocalDate.now().dayOfWeek.value - 1
                                                ) {
                                                    DayOfWeek.SUNDAY.value.dec() -> dateLabels
                                                    else -> listOf(
                                                        *dateLabels.subList(weekdayIdxOffset.inc(), dateLabels.size)
                                                            .toTypedArray(),
                                                        *dateLabels.subList(0, weekdayIdxOffset.inc()).toTypedArray(),
                                                    )
                                                }
                                                val sIdx = max(0.0, min.toDouble()).toInt()
                                                val eIdx = min(7.0, max.roundToInt().toDouble()).toInt()
                                                dateLabelsSorted.subList(sIdx, eIdx)
                                                    .mapIndexed { idx, it -> it to sIdx + idx.toFloat() }
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
                                }
                            }
                        }
                        BaseDashboardCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Min)
                        ) {

                            Row {
                                @Composable
                                fun item(
                                    widthFraction: Float,
                                    imageVector: ImageVector,
                                    text: String,
                                    title: String
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth(widthFraction)
                                            .fillMaxHeight()
                                    ) {
                                        Row(
                                            modifier = Modifier
                                                .align(Alignment.TopCenter)
                                                .fillMaxHeight()
                                                .offset(y = (-10).dp, x = (-10).dp),
                                            horizontalArrangement = Arrangement.spacedBy(5.dp),
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                modifier = Modifier.fillMaxHeight(0.75f).fillMaxWidth(0.5f),
                                                imageVector = imageVector,
                                                contentDescription = null
                                            )
                                            Text(
                                                text = text
                                            )
                                        }
                                        Text(
                                            modifier = Modifier.align(Alignment.BottomCenter),
                                            text = title
                                        )
                                    }
                                }

                                item(
                                    title = "Streak",
                                    imageVector = Icons.Filled.Whatshot,
                                    text = "${data?.streak ?: "?"} Days",
                                    widthFraction = 0.5f
                                )
                                item(
                                    title = "Total Exercises",
                                    imageVector = Icons.Filled.Functions,
                                    text = data?.totalExercises?.toString() ?: "?",
                                    widthFraction = 1f
                                )

                            }
                        }
                        Row {
                            val interPaddingHalf = 16.dp.div(2)
                            Square(
                                modifier = Modifier
                                    .fillMaxWidth(0.5f)
                                    .padding(end = interPaddingHalf)
                            ) {
                                IconDashboardCard(
                                    modifier = Modifier.fillMaxSize(),
                                    onClick = {
                                        router.navTo(MobileRoutes.CameraSetup)
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.align(Alignment.Center).fillMaxSize(0.75f),
                                            imageVector = Icons.Filled.CameraAlt,
                                            contentDescription = null
                                        )
                                    },
                                    text = {
                                        Text("Camera Setup")
                                    }
                                )
                            }
                            Square(
                                modifier = Modifier
                                    .padding(start = interPaddingHalf)
                                    .fillMaxWidth()
                            ) {
                                IconDashboardCard(
                                    modifier = Modifier.fillMaxSize(),
                                    onClick = {
                                        router.navTo(MobileRoutes.AppBenefits)
                                    },
                                    icon = {
                                        Icon(
                                            modifier = Modifier.align(Alignment.Center).fillMaxSize(0.75f),
                                            imageVector = Icons.Filled.SentimentSatisfied,
                                            contentDescription = null
                                        )
                                    },
                                    text = {
                                        Text("App benefits")
                                    }
                                )
                            }
                        }
                    }
                },
                bottomBar = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                    ) {
                        EFabWithBackground(
                            onClick = {
                                router.navTo(MobileRoutes.Scanner)
                            }
                        ) {
                            Surface(
                                modifier = Modifier
                                    .align(Alignment.BottomCenter)
                                    .fillMaxWidth(0.85f)
                                    .height(42.dp),
                                shape = RoundedCornerShape(topStart = 35.dp, topEnd = 35.dp),
                                color = MaterialTheme.colors.background
                            ) {}
                        }
                    }
                }
            )
        }
    }

    if (lostConnection) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text(text = "Error") },
            text = { Text("Connection to desktop application lost") },
            confirmButton = {},
            dismissButton = {
                Button(
                    onClick = {
                        lostConnection = false
                    }) {
                    Text("This is the dismiss Button")
                }
            }
        )
    }
}

@Composable
fun Square(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Layout(
        modifier = modifier,
        content = content
    ) { list, constraints ->
        require(list.size == 1) { "Square composable may only have one direct child" }
        val squareSize = min(constraints.maxWidth, constraints.maxHeight)
        val placeable =
            list.first().measure(constraints.copy(maxWidth = squareSize, maxHeight = squareSize))
        layout(placeable.width, placeable.height) {
            placeable.placeRelative(0, 0)
        }
    }
}

@Composable
fun BoxScope.Wave(cornerSize: Dp) {
    val modifier = Modifier
        .align(Alignment.BottomStart)
        .width(cornerSize)
        .height(cornerSize.times(2))
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.surface,
    ) { }
    Surface(
        modifier = modifier,
        color = MaterialTheme.colors.background,
        shape = RoundedCornerShape(bottomStart = cornerSize)
    ) { }
}

@Composable
fun EFabWithBackground(onClick: () -> Unit, backgroundShape: @Composable () -> Unit) {
    Layout(content = {
        backgroundShape.invoke()
        ExtendedFloatingActionButton(
            backgroundColor = MaterialTheme.colors.primary,
            onClick = onClick,
            text = {
                Text("Connect to PC", style = MaterialTheme.typography.h5)
            },
            icon = {
                Icon(
                    modifier = Modifier,
                    imageVector = Icons.Default.Phonelink,
                    contentDescription = null
                )
            }
        )
    }) { list, constraints ->
        val shape = list[0].measure(constraints)
        val efab = list[1].measure(constraints)
        val height = shape.height + efab.height / 2
        val maxWidth = max(shape.width, efab.width)
        layout(width = maxWidth, height = height) {
            shape.placeRelative(0, y = efab.height / 2)
            efab.placeRelative(x = (maxWidth - efab.width) / 2, y = 0)
        }
    }
}




