@file:Suppress("FunctionName")

package ui.components.progress.practice

import TypeTrainerTheme
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlin.math.min

@Composable
fun ProgressbarPractice(
    modifier: Modifier = Modifier,
    value: Float,
    max: Float
) {
    val progress = remember(value, max) { min(value / max, 1f).also(::println) }
    val anim = animateFloatAsState(progress, animationSpec = tween(easing = LinearEasing))

    val trackColor = MaterialTheme.colors.primary
    val backgroundColor = MaterialTheme.colors.surface

    val endIcon = Icons.Filled.PinDrop


    Box {
        Canvas(
            modifier = modifier,
        ) {
            val maxWidth = this.drawContext.size.width
            val progression = maxWidth * anim.value.also(::println)
            drawRoundRect(
                color = backgroundColor,
                cornerRadius = CornerRadius(x = 15f, y = 20f),
                topLeft = Offset.Zero,
                size = Size(width = maxWidth, height = 20f)
            )
            drawRoundRect(
                color = trackColor,
                cornerRadius = CornerRadius(x = 15f, y = 20f),
                topLeft = Offset.Zero,
                size = Size(width = progression, height = 20f)
            )
        }
        Text(
            modifier = Modifier.offset(x = anim.value.times(100).dp).padding(horizontal = 5.dp),
            text = "$progress%"
        )
        Icon(
            modifier = Modifier.align(Alignment.CenterEnd).padding(horizontal = 5.dp),
            imageVector = endIcon,
            contentDescription = null,
            tint = MaterialTheme.colors.onSurface
        )
    }


}

fun main() {
    Window {
        TypeTrainerTheme {
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                val (value, setValue) = remember { mutableStateOf(0f) }
                val max = 100f

                LaunchedEffect(value, setValue) {
                    repeat(100) {
                        delay(500)
                        setValue(value.inc())
                    }
                }

                ProgressbarPractice(
                    modifier = Modifier.fillMaxWidth(0.8f),
                    value = value,
                    max = max
                )
            }
        }
    }
}
