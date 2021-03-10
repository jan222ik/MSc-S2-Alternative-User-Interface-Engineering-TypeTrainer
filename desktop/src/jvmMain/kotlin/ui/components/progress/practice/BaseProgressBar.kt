@file:Suppress("FunctionName")

package ui.components.progress.practice

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import kotlin.math.min

/**
 * Defines a base progress bar for variety of uses.
 * @param modifier Modifier for canvas
 * @param value the current value
 * @param max the maximal value
 * @param movingRow Scoped lambda (RowScope) for composable content that
 * moves with the header of the progress bar
 * @param endIcon Lambda for composable content for endIcon place
 * @param trackColor defines the color of the track indicating the progress
 * @param backgroundColor defines the color of the background of the progress bar
 */
@Composable
fun BaseProgressBar(
    modifier: Modifier = Modifier,
    value: Float,
    max: Float,
    movingRow: @Composable RowScope.(Float) -> Unit,
    endIcon: (@Composable () -> Unit)? = null,
    trackColor: Color = MaterialTheme.colors.primary,
    backgroundColor: Color = MaterialTheme.colors.surface
) {
    val progress = remember(value, max) { min(value / max, 1f) }
    val anim = animateFloatAsState(progress, animationSpec = tween(easing = LinearEasing))
    val rectangleHeight = 30f
    val cornerRadius = CornerRadius(x = rectangleHeight - 5, y = rectangleHeight)
    Layout(
        content = {
            Canvas(
                modifier = modifier,
            ) {
                val maxWidth = this.drawContext.size.width
                drawRoundRect(
                    color = backgroundColor,
                    cornerRadius = cornerRadius,
                    topLeft = Offset.Zero,
                    size = Size(width = maxWidth, height = rectangleHeight)
                )
            }
            Canvas(
                modifier = modifier.width(IntrinsicSize.Min),
            ) {
                val maxWidth = this.drawContext.size.width
                val progression = maxWidth * anim.value
                drawRoundRect(
                    color = trackColor,
                    cornerRadius = cornerRadius,
                    topLeft = Offset.Zero,
                    size = Size(width = progression, height = rectangleHeight)
                )
            }
            Row(
                modifier = Modifier.height(IntrinsicSize.Max),
                verticalAlignment = Alignment.CenterVertically
            ) {
                movingRow.invoke(this@Row, progress)
            }
            endIcon?.invoke()
        }
    ) { list: List<Measurable>, constraints: Constraints ->
        val pBackgroundCanvas = list[0].measure(constraints)
        val pTrackCanvas = list[1].measure(constraints)
        val pMovingRow = list[2].measure(constraints)
        val pEndIcon = if (endIcon != null) list[3].measure(constraints) else null
        val pEndIconWidth = pEndIcon?.width ?: 0
        val maxWidth = maxOf(0, pBackgroundCanvas.width, pTrackCanvas.width, pMovingRow.width, pEndIconWidth)
        val trackProgressionWidth = (pTrackCanvas.width * anim.value).toInt()
        val endIconOffsetX = maxWidth - pEndIconWidth
        val insideTackWidth = trackProgressionWidth - pMovingRow.width
        val movingRowOffsetX = when {
            trackProgressionWidth < pMovingRow.width -> 0  //trackProgressionWidth
            trackProgressionWidth >= endIconOffsetX -> endIconOffsetX - pMovingRow.width
            else -> insideTackWidth
        }
        val maxHeight = maxOf(0, rectangleHeight.toInt(), pMovingRow.height, pEndIcon?.height ?: 0)
        val maxHeightHalf = maxHeight.div(2)
        layout(
            width = maxWidth,
            height = maxHeight
        ) {
            pBackgroundCanvas.placeRelative(IntOffset.Zero)
            pTrackCanvas.placeRelative(IntOffset.Zero)
            pMovingRow.placeRelative(
                x = movingRowOffsetX,
                y = maxHeightHalf.minus(pMovingRow.height.div(2)).coerceAtLeast(0)
            )
            pEndIcon?.placeRelative(
                x = endIconOffsetX,
                y = maxHeightHalf.minus(pEndIcon.height.div(2)).coerceAtLeast(0)
            )
        }
    }
}
