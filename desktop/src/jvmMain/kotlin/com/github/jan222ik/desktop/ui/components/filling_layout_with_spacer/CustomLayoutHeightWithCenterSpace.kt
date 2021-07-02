@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.components.filling_layout_with_spacer

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import com.github.jan222ik.common.HasDoc
import kotlin.math.max

/**
 * Component takes all vertical space and distributes it to
 * its two composition children equally after subtraction
 * the central padding value.
 *
 * @param modifier to modify the Layout
 * @param centralPadding defines the padding between top and bottom composables
 * @param top Lambda for top composable content
 * @param bottom Lambda for bottom composable content
 */
@Composable
@HasDoc
fun CustomLayoutHeightWithCenterSpace(
    modifier: Modifier = Modifier,
    centralPadding: Dp,
    top: @Composable () -> Unit,
    bottom: @Composable () -> Unit
) {
    val centralPaddingPx: Float = with(LocalDensity.current) { centralPadding.toPx() }
    Layout(
        modifier = modifier,
        content = {
            top.invoke()
            bottom.invoke()
        }
    ) { list, constraints ->
        require(list.size == 2) { "The top and bottom composable must have exactly 1 measurable child each" }
        val halfRemainingHeight = max(0, (constraints.maxHeight - centralPaddingPx).div(2).toInt())
        val newConstraints = constraints.copy(maxHeight = halfRemainingHeight)
        val pTop = list[0].measure(newConstraints)
        val pBottom = list[1].measure(newConstraints)
        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            pTop.placeRelative(0, 0)
            pBottom.placeRelative(0, pTop.height + centralPaddingPx.toInt())
        }
    }
}
