@file:Suppress("FunctionName")

package ui.components.outlined_radio_button.internal

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
internal fun VDivider(additionalHeight: Dp) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary)
            .width(3.dp)
            .height(IntrinsicSize.Max)
            .background(color = MaterialTheme.colors.primary)
    ) {
        Spacer(Modifier.fillMaxHeight().width(3.dp).background(color = MaterialTheme.colors.primary))
        Text(modifier = Modifier.padding(top = additionalHeight), text = "")
    }
}
