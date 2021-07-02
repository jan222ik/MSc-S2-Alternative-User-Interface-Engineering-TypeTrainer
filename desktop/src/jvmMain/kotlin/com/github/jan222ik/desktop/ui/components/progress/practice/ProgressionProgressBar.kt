@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.components.progress.practice

import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ProgressionProgressBar(
    modifier: Modifier = Modifier,
    value: Float,
    max: Float
) {
    BaseProgressBar(
        modifier = modifier,
        value = value,
        max = max,
        movingRow = {
            Text(
                modifier = Modifier.padding(horizontal = 5.dp),
                text = it.times(100).toInt().toString() + "%",
                textAlign = TextAlign.Start,
                color = MaterialTheme.colors.onPrimary
            )
        },
        endIcon = {
            Icon(
                modifier = Modifier.padding(horizontal = 5.dp),
                imageVector = Icons.Filled.PinDrop,
                contentDescription = null,
                tint = MaterialTheme.colors.onSurface
            )
        }
    )
}
