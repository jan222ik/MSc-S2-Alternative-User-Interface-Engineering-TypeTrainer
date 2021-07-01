@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.general.window.container.impl

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc

/**
 * The back button shows an icon based on the state of enableBackBtn.
 * On Click the onBackButtonAction is invoked.
 * Additionally, the button shows the backHoverBtnText on hover.
 *
 * @param height defines the maximum height of the component
 * @param enableBackBtn if true -> the button is enabled and the Arrow Icon is shown
 *                      otherwise -> the Dashboard icon is shown
 * @param onBackBtnAction Invoked if button was clicked
 * @param backHoverBtnText Text shown when Arrow Icon is hovered
 * @param surface Color of button surface
 */
@Composable
@HasDoc
internal fun BackBtn(
    height: Dp,
    enableBackBtn: Boolean,
    onBackBtnAction: () -> Unit,
    backHoverBtnText: String,
    surface: Color,
) {
    val (onHover, setOnHover) = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .height(height)
            .pointerMoveFilter(
                onEnter = { setOnHover(true); true },
                onExit = { setOnHover(false); true }
            )
            .clickable(onClick = onBackBtnAction),
        shape = RoundedCornerShape(topEnd = 25.dp),
        backgroundColor = surface,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (enableBackBtn) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                if (onHover) {
                    Text(text = backHoverBtnText)
                }
            } else {
                Icon(imageVector = Icons.Filled.Dashboard, contentDescription = null)
            }
        }
    }
}
