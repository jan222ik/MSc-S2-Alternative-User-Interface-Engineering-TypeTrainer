package com.github.jan222ik.desktop.ui.components

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.TwoWayConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
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
fun AnimatedLogo(modifier: Modifier = Modifier, onAnimatedOnce: (Boolean) -> Unit) {
    val instantEase = Easing { it }
    val transition = rememberInfiniteTransition()
    val totalDuration = 3000
    val indexCount = remember { mutableStateOf(0) }
    if (indexCount.value == 1) {
        onAnimatedOnce(true)
    }
    val charIndex by transition.animateValue(
        initialValue = 0,
        targetValue = 12,
        typeConverter = IntToVector,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = keyframes {
                durationMillis = totalDuration
                0 at 0 with instantEase
                1 at 200 with instantEase
                2 at 400 with instantEase
                3 at 600 with instantEase
                4 at 800 with instantEase
                5 at 1000 with instantEase
                6 at 1200 with instantEase
                7 at 1400 with instantEase
                8 at 1600 with instantEase
                9 at 1800 with instantEase
                10 at 2000 with instantEase
                11 at 2200 with instantEase
                12 at 2400 with instantEase
            }
        )
    )
    if (indexCount.value == 0 && charIndex == 12) {
        indexCount.value++
    }
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
    //println(charIndex)
    Row(
        modifier = modifier.width(350.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tStyle = MaterialTheme.typography.h1
        Text(
            modifier = Modifier.offset(y = (-5).dp),
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
        //println(cursorFlash)
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

fun timeStepper(time: Int, totalSteps: Int): (nr: Float) -> Int {
    return { nr -> nr.div(totalSteps).times(time).toInt() }
}

@ExperimentalAnimationApi
@Composable
fun AnimatedLogo2(modifier: Modifier = Modifier) {
    val instantEase = Easing { it }
    val transition = rememberInfiniteTransition()
    val totalDuration = 3000
    val step = timeStepper(totalDuration, 12)
    val charIndex by transition.animateValue(
        initialValue = 0,
        targetValue = 12,
        typeConverter = IntToVector,
        animationSpec = infiniteRepeatable(
            repeatMode = RepeatMode.Restart,
            animation = keyframes {
                durationMillis = totalDuration
                0 at step(0f) with instantEase
                1 at step(1f) with instantEase
                2 at step(2f) with instantEase
                3 at step(3f) with instantEase
                4 at step(4f) with instantEase
                5 at step(5f) with instantEase
                6 at step(6f) with instantEase
                7 at step(7f) with instantEase
                8 at step(8f) with instantEase
                9 at step(9f) with instantEase
                10 at step(10f) with instantEase
                11 at step(11f) with instantEase
                12 at step(12f) with instantEase
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

@OptIn(ExperimentalAnimationApi::class)
fun main() {
    Window {
        Box(Modifier.fillMaxSize()) {
            val animOnce = remember { mutableStateOf(false) }
            AnimatedLogo(modifier = Modifier.align(Alignment.Center), onAnimatedOnce = animOnce.component2())
        }
    }
}

