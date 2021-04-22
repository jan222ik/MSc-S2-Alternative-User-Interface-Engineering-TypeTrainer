package ui

import androidx.compose.desktop.Window
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.delay

fun main() {
    Window {
        outer()
    }
}

@Composable
fun outer() {
    val durationState = remember { mutableStateOf(600000) }
    LaunchedEffect(durationState) {
        // This not how you do a timer but close enough
        val step = 1000
        repeat(600000.div(step)) {
            durationState.value = durationState.value - step
            delay(step.toLong())
        }
    }
    questionComposable(durationState)
}

@Composable
fun questionComposable(durationState: MutableState<Int>) {
    val durationString = remember(durationState.value) {
        val duration = durationState.value
        if (duration != 0) {
            var secondTime = ((duration / 1000) % 60).toString()
            var minuteTime = ((((duration / 1000) / 60) % 60)).toString()
            if (secondTime.length == 1) {
                secondTime = "0$secondTime"
            }
            if (minuteTime.length == 1) {
                minuteTime = "0$minuteTime"
            }
            "$minuteTime:$secondTime"
        } else {
            "00:00"
        }

    }
    Text(
        text = durationString,
        style = MaterialTheme.typography.h4
    )
}
