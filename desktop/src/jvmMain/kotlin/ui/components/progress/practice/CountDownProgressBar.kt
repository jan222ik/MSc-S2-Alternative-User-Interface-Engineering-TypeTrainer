@file:Suppress("FunctionName")

package ui.components.progress.practice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun CountDownProgressBar(
    modifier: Modifier = Modifier,
    value: Float,
    max: Float
) {
    BaseProgressBar(
        modifier = modifier,
        value = value,
        max = max,
        movingRow = {
            Icon(
                modifier = Modifier.padding(horizontal = 5.dp),
                imageVector = Icons.Filled.Timer,
                contentDescription = null,
                tint = MaterialTheme.colors.onPrimary
            )
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = max.minus(value).toInt().coerceAtLeast(0).toString() + "s",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.onSurface
            )
        },
        trackColor = MaterialTheme.colors.surface,
        backgroundColor = MaterialTheme.colors.primary
    )
}
