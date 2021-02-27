@file:Suppress("FunctionName")

package ui.components.outlined_radio_button.internal

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow

@Composable
internal fun OptionText(text: String, color: Color, textModifier: Modifier = Modifier) {
    CenteringBox {
        Text(
            modifier = textModifier,
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = color
        )
    }
}
