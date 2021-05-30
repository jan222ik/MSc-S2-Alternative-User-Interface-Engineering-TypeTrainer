@file:Suppress("FunctionName")

package com.github.jan222ik.compose_mpp_charts.core.graph.canvas

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.unit.Constraints
import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import com.github.jan222ik.compose_mpp_charts.core.labels.subcompose.labelSubcompose
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartLabelSlot
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartScope
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.ChartScopeImpl
import com.github.jan222ik.compose_mpp_charts.core.graph.dsl.detectTransformGestures
import com.github.jan222ik.compose_mpp_charts.core.interaction.IBoundingShape2D
import com.github.jan222ik.compose_mpp_charts.core.series.ISeriesRenderer
import com.github.jan222ik.compose_mpp_charts.core.viewport.Viewport
import kotlin.math.roundToInt

@Composable
fun Chart(
    modifier: Modifier,
    viewport: MutableState<Viewport>,
    maxViewport: Viewport = viewport.value,
    minViewportSize: Size = viewport.value.size,
    maxViewportSize: Size = viewport.value.size,
    enableZoom: Boolean = false,
    definition: ChartScope.() -> Unit
) {
    val graphScopeImpl = ChartScopeImpl()
    definition.invoke(graphScopeImpl)
    val (slotCanvas) = remember { listOf("canvas") }
    val renderedPoints = remember { mutableStateOf(listOf<IBoundingShape2D>()) }
    val activePopupsAt = remember { mutableStateOf<Pair<Offset, IBoundingShape2D?>?>(null) }

    SubcomposeLayout(modifier) { constraints ->

        val (spaceForStartLabel, startSlotResults) = labelSubcompose(
            slot = ChartLabelSlot.START,
            viewport = viewport.value,
            graphScopeImpl = graphScopeImpl,
            constraints = constraints
        )

        val (spaceForEndLabel, endSlotResults) = labelSubcompose(
            slot = ChartLabelSlot.END,
            viewport = viewport.value,
            graphScopeImpl = graphScopeImpl,
            constraints = constraints
        )

        val (spaceForBottomLabel, bottomSlotResults) = labelSubcompose(
            slot = ChartLabelSlot.BOTTOM,
            viewport = viewport.value,
            graphScopeImpl = graphScopeImpl,
            constraints = constraints
        )

        val (spaceForTopLabel, topSlotResults) = labelSubcompose(
            slot = ChartLabelSlot.TOP,
            viewport = viewport.value,
            graphScopeImpl = graphScopeImpl,
            constraints = constraints
        )

        val pPopupsWithOffset = subcompose("popups") {
            activePopupsAt.value?.let { (offset: Offset, shape2D: IBoundingShape2D?) ->
                val clickConsumed = graphScopeImpl.onClickPopupLabel?.invoke(offset, shape2D) ?: false
                LaunchedEffect(offset, shape2D) {
                    if (!clickConsumed) {
                        graphScopeImpl.onClick?.invoke(offset, shape2D)
                    }
                }
            }
        }.firstOrNull()?.let {
            it.measure(constraints) to activePopupsAt.value?.first
        }

        val canvasSize = Size(
            width = constraints.maxWidth.toFloat() - spaceForStartLabel - spaceForEndLabel,
            height = constraints.maxHeight.toFloat() - spaceForBottomLabel - spaceForTopLabel
        )
        val rendererContext = ChartContext.of(viewport.value, canvasSize)

        val mCanvas = subcompose(slotCanvas) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(viewport) {
                        detectTransformGestures { _, pan, zoom, direction ->
                            println("Gesture detected: Zoom: $zoom")
                            val current = if (enableZoom) {
                                viewport.value.applyZoom(
                                    zoom = zoom,
                                    direction = direction,
                                    minSize = minViewportSize,
                                    maxSize = maxViewportSize
                                )
                            } else {
                                viewport.value
                            }

                            val dx = -pan.x / rendererContext.scaleX
                            val dy = pan.y / rendererContext.scaleY
                            viewport.value = current.applyPan(dx, dy, maxViewport)
                        }
                    }
                    .pointerInput(renderedPoints) {
                        detectTapGestures { offset ->
                            val shape2D = renderedPoints.value.firstOrNull { it.containtsOffset(offset) }
                            activePopupsAt.value = offset to shape2D
                        }
                    }
            ) {
                clipRect clipScope@{
                    with(
                        ChartDrawScope(
                            drawScope = this@clipScope,
                            context = ChartContext.of(
                                viewport = viewport.value,
                                canvasSize = this.size
                            )
                        )
                    ) {
                        graphScopeImpl.gridRenderer.forEach {
                            with(it) { render() }
                        }
                        renderedPoints.value = graphScopeImpl.seriesMap.map { (series: List<DataPoint>, renderer: ISeriesRenderer) ->
                            with(renderer) { render(series) }
                        }.flatten()
                        graphScopeImpl.linearFunctionRenderer.forEach {
                            with(it) { render() }
                        }
                    }
                }
                graphScopeImpl.abscissaAxisRenderer.forEach { it.apply { render() } }
                graphScopeImpl.ordinateAxisRenderer.forEach { it.apply { render() } }
            }
        }

        val pCanvas = mCanvas.first().measure(
            Constraints.fixed(
                width = canvasSize.width.roundToInt(),
                height = canvasSize.height.roundToInt()
            )
        )

        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            startSlotResults
                .forEach { (placeable, labelInChartSpace) ->
                    val halfComposableHeight = placeable.height.div(2)
                    val y =
                        with(rendererContext) { labelInChartSpace.toRendererY() + halfComposableHeight }.roundToInt()
                    if (y >= 0 && y <= constraints.maxHeight - spaceForBottomLabel - halfComposableHeight) {
                        placeable.placeRelative(
                            x = 0,
                            y = y,
                        )
                    }
                }
            endSlotResults
                .forEach { (placeable, labelInChartSpace) ->
                    val halfComposableHeight = placeable.height.div(2)
                    val y =
                        with(rendererContext) { labelInChartSpace.toRendererY() + halfComposableHeight }.roundToInt()
                    if (y >= 0 && y <= constraints.maxHeight - spaceForBottomLabel - halfComposableHeight) {
                        placeable.placeRelative(
                            x = constraints.maxWidth - spaceForEndLabel,
                            y = y,
                        )
                    }
                }
            topSlotResults
                .forEach { (placeable, labelInChartSpace) ->
                    val halfComposableWidth = placeable.width.div(2)
                    val x =
                        with(rendererContext) { labelInChartSpace.toRendererX() + spaceForStartLabel - halfComposableWidth }.roundToInt()
                    if (x >= spaceForStartLabel - halfComposableWidth && x <= constraints.maxWidth - spaceForStartLabel - halfComposableWidth) {
                        placeable.placeRelative(
                            x = x,
                            y = 0
                        )
                    }
                }
            bottomSlotResults
                .forEach { (placeable, labelInChartSpace) ->
                    val halfComposableWidth = placeable.width.div(2)
                    val x =
                        with(rendererContext) { labelInChartSpace.toRendererX() + spaceForStartLabel - halfComposableWidth }.roundToInt()
                    if (x >= spaceForStartLabel - halfComposableWidth && x <= constraints.maxWidth - spaceForStartLabel - halfComposableWidth) {
                        placeable.placeRelative(
                            x = x,
                            y = constraints.maxHeight - spaceForBottomLabel
                        )
                    }
                }
            pCanvas.placeRelative(x = spaceForStartLabel, y = spaceForTopLabel)
            pPopupsWithOffset?.let { (placeable: Placeable, offset: Offset?) ->
                offset?.let { (x, y) ->
                    placeable.placeRelative(x = x.roundToInt(), y = y.roundToInt())
                }
            }
        }
    }
}
