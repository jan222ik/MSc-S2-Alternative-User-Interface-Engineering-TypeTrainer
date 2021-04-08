package ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.*
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.dp

private val IntToVector: TwoWayConverter<Int, AnimationVector1D> =
    TwoWayConverter({ AnimationVector1D(it.toFloat()) }, { it.value.toInt() })

private val BoolToVector: TwoWayConverter<Boolean, AnimationVector1D> =
    TwoWayConverter({ AnimationVector1D(0f.takeIf { _ -> it } ?: 1f) }, { it.value <= 0.5f })

@ExperimentalAnimationApi
@Composable
fun AnimatedLogo(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition()
    val totalDuration = 3000
    val charIndex by transition.animateValue(
        initialValue = 0,
        targetValue = 12,
        typeConverter = IntToVector,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = keyframes {
                durationMillis = totalDuration
                0 at 0 with LinearOutSlowInEasing
                1 at 200 with LinearOutSlowInEasing
                2 at 400 with LinearOutSlowInEasing
                3 at 600 with LinearOutSlowInEasing
                4 at 800 with LinearOutSlowInEasing
                5 at 1000 with LinearOutSlowInEasing
                6 at 1200 with LinearOutSlowInEasing
                7 at 1400 with LinearOutSlowInEasing
                8 at 1600 with LinearOutSlowInEasing
                9 at 1800 with LinearOutSlowInEasing
                10 at 2000 with LinearOutSlowInEasing
                11 at 2200 with LinearOutSlowInEasing
                12 at 2400 with LinearOutSlowInEasing
            }
        )
    )
    val cursorFlash by transition.animateValue(
        initialValue = false,
        targetValue = true,
        typeConverter = BoolToVector,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = keyframes {
                durationMillis = 600
                true at 0 with LinearOutSlowInEasing
                false at 300 with LinearOutSlowInEasing
                true at 600 with LinearOutSlowInEasing
            }
        )
    )
    println(charIndex)
    Row(modifier = modifier.width(500.dp), verticalAlignment = Alignment.CenterVertically) {
        val tStyle = MaterialTheme.typography.h1
        Text(
            modifier = Modifier.offset(y = -5.dp),
            text = "T",
            style = tStyle
        )
        Row {
            AnimatedLogoChar(0, charIndex)
            AnimatedLogoChar(1, charIndex)
            AnimatedLogoChar(2, charIndex)
            AnimatedLogoChar(3, charIndex)
            AnimatedLogoChar(4, charIndex)
            AnimatedLogoChar(5, charIndex)
            AnimatedLogoChar(6, charIndex)
            AnimatedLogoChar(7, charIndex)
            AnimatedLogoChar(8, charIndex)
            AnimatedLogoChar(9, charIndex)
            AnimatedLogoChar(10, charIndex)
        }
        println(cursorFlash)
        if (cursorFlash) {
            Text(modifier = Modifier.offset(y = 5.dp).rotate(180f), text = "T", style = tStyle)
        }
    }

}


@ExperimentalAnimationApi
@Composable
fun AnimatedLogoChar(index: Int, currentIndex: Int) {
    val charStyle = MaterialTheme.typography.h3
    val char = remember(index) { "TypeTrainer"[index] }
    if (currentIndex > index) {
        Text(text = char.toString(), style = charStyle)
    }

}

@OptIn(ExperimentalAnimationApi::class)
fun main() {
    Window {
        Box(Modifier.fillMaxSize()) {
            AnimatedLogo(modifier = Modifier.align(Alignment.Center))
        }
    }
}

