@file:Suppress("FunctionName")

package ui.components.progress.practice

import TypeTrainerTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay


@ExperimentalLayout
fun main() {
    Window {
        TypeTrainerTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background),
                contentAlignment = Alignment.Center
            ) {
                val (value, setValue) = remember { mutableStateOf(0f) }
                val max = 100f

                LaunchedEffect(value, setValue) {
                    repeat(100) {
                        delay(200)
                        setValue(value.inc())
                    }
                }
                Column {
                    ProgressionProgressBar(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = value,
                        max = max
                    )
                    CountDownProgressBar(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = value,
                        max = max
                    )
                }
            }
        }
    }
}
