@file:Suppress("FunctionName")

package com.github.jan222ik.common.ui.components

import androidx.compose.foundation.Image
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp

const val logo_part1: String = "M84.6406 45.9688H52.7188V134H35.0703V45.9688H3.42969V31.625H84.6406V45.9688Z"
val logo_part1_color = Color.White
const val logo_part2: String = "M34.4218 150.263L66.3416 150.624L67.338 62.5986L84.9854 62.7983L83.9889 " +
        "150.824L115.628 151.182L115.465 165.525L34.2594 164.606L34.4218 150.263Z"
val logo_part2_color = Color(0xFFC4C4C4)


@Composable
fun Logo(modifier: Modifier = Modifier) {
    val part1 = remember { addPathNodes(logo_part1) }
    val part2 = remember { addPathNodes(logo_part2) }
    val vecPainter = rememberVectorPainter(
        defaultWidth = 119.dp,
        defaultHeight = 169.dp,
        viewportWidth = 119f,
        viewportHeight = 169f,
    ) { _, _ ->
        Path(
            pathData = part1,
            fill = SolidColor(logo_part1_color),
        )
        Path(
            pathData = part2,
            fill = SolidColor(logo_part2_color),
        )
    }
    Image(
        modifier = modifier,
        painter = vecPainter,
        contentDescription = "TypeTrainer Logo"
    )
}
