@file:Suppress("FunctionName")

package com.github.jan222ik.common.ui.dashboard

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.dp
import kotlin.math.max

/**
 * Specialized clickable BaseDashboardCard with designated text and icon slot.
 *
 * @param modifier Modifier for BaseDashboardCard
 * @param bgColor background color
 * @param onClick invoked on click
 * @param text lambda for composable content defining the text
 * @param icon lambda for composable content defining the icon
 */
@Composable
fun IconDashboardCard(
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colors.background,
    onClick: () -> Unit,
    text: @Composable BoxScope.() -> Unit,
    icon: @Composable BoxWithConstraintsScope.() -> Unit
) {
    val hover = remember { mutableStateOf(false) }

    BaseDashboardCard(
        modifier = modifier.clickable(onClick = onClick),
        bgColor = bgColor,
    ) {
        Box(
            modifier = modifier
                .padding(all = 10.dp)
                .fillMaxSize(),
        ) {
            Layout(
                content = {
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        text.invoke(this)
                    }
                    BoxWithConstraints(
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        icon.invoke(this)
                    }
                }
            ) { measurables, constraints ->
                val placeable1 = measurables[1].measure(constraints)
                val heightConstraint = constraints.copy(maxHeight = constraints.maxHeight - placeable1.height)
                val placeable2 = measurables[0].measure(heightConstraint)
                layout(
                    width = max(max(placeable1.width, placeable2.width), 0),
                    height = placeable1.height + placeable2.height
                ) {
                    placeable1.placeRelative(x = 0, y = 0)
                    placeable2.placeRelative(x = 0, y = placeable1.height)
                }
            }
        }
    }
}
