@file:Suppress("FunctionName")

package com.github.jan222ik.common.ui.dashboard

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc

/**
 * Basic material card with content slot.
 * @param modifier Modifier for Card
 * @param bgColor Background Color of Card
 * @param shape Shape of card
 * @param content Scoped lambda (BoxScope) for composable content
 */
@Composable
@HasDoc
fun BaseDashboardCard(
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colors.background,
    shape: Shape = RoundedCornerShape(16.dp),
    content: @Composable BoxScope.() -> Unit
) {
    Card(
        modifier = modifier,
        shape = shape,
        backgroundColor = bgColor,
        elevation = 16.dp // For shadow
    ) {
        Box(
            modifier = Modifier.padding(16.dp).fillMaxSize()
        ) {
            content.invoke(this)
        }
    }
}
