@file:Suppress("FunctionName")

package ui.components.filling_layout_with_spacer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import kotlin.math.max

@Composable
fun CustomLayoutWidthWithCenterSpace(
    modifier: Modifier = Modifier,
    centralPadding: Dp,
    start: @Composable () -> Unit,
    end: @Composable () -> Unit
) {
    val centralPaddingPx: Float = with(LocalDensity.current) { centralPadding.toPx() }
    Layout(
        modifier = modifier,
        content = {
            start.invoke()
            end.invoke()
        }
    ) { list, constraints ->
        require(list.size == 2) { "The start and end composable must have exactly 1 measurable child each" }
        val halfRemainingWidth = max(0, (constraints.maxWidth - centralPaddingPx).div(2).toInt())
        val newConstraints = constraints.copy(maxWidth = halfRemainingWidth)
        val pStart = list[0].measure(newConstraints)
        val pEnd = list[1].measure(newConstraints)
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            pStart.placeRelative(0, 0)
            pEnd.placeRelative(pStart.width + centralPaddingPx.toInt(), 0)
        }
    }
}

