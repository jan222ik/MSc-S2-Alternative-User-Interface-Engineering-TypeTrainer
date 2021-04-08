@file:Suppress("FunctionName")

package ui.dashboard

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import com.github.jan222ik.common.ui.dashboard.IconDashboardCard

/**
 * Specialized IconDashboardCard which changes background color on mouse hover.
 *
 * @param modifier Modifier for BaseDashboardCard
 * @param bgColor background color
 * @param hoverColor color change on mouse hover
 * @param onClick invoked on click
 * @param text lambda for composable content defining the text
 * @param icon lambda for composable content defining the icon
 */
@Composable
fun HoverIconDashboardCard(
    modifier: Modifier = Modifier,
    bgColor: Color = MaterialTheme.colors.background,
    hoverColor: Color = MaterialTheme.colors.primary,
    onClick: () -> Unit,
    text: @Composable BoxScope.() -> Unit,
    icon: @Composable BoxScope.() -> Unit
) {
    val hover = remember { mutableStateOf(false) }

    IconDashboardCard(
        modifier = modifier
            .pointerMoveFilter(
                onEnter = {
                    hover.value = true
                    false
                },
                onExit = {
                    hover.value = false
                    false
                }
            ),
        bgColor = hoverColor.takeIf { hover.value } ?: bgColor,
        onClick = onClick,
        text = text,
        icon = icon
    )
}
