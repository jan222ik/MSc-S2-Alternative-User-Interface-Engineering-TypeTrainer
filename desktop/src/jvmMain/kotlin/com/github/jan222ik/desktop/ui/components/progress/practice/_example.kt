@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.components.progress.practice

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import kotlinx.coroutines.delay


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
