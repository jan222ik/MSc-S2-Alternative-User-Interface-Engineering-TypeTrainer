@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.results.timeline

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.CleanHands
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.desktop.textgen.error.CharEvaluation
import com.github.jan222ik.desktop.ui.exercise.results.ResultIntent

@ExperimentalFoundationApi
@Composable
fun ColumnScope.ResultsTimeline(intent: ResultIntent, isStandalone: Boolean) {
    val widthFraction = when (isStandalone) {
        true -> 0.7f
        false -> 1f
    }
    val density: Density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density = density.density, fontScale = density.fontScale * 1.2f)
    ) {
        BaseDashboardCard(
            modifier = Modifier
                .fillMaxWidth(widthFraction)
                .padding(vertical = 16.dp)
                .fillMaxHeight()
                .align(Alignment.CenterHorizontally)
        ) {
            LazyColumn {
                intent.data.texts.forEachIndexed { index, text ->
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colors.background
                        ) {
                            Text(
                                modifier = Modifier.padding(vertical = 4.dp),
                                text = "Text ${index.inc()}"
                            )
                        }
                    }
                    text.chars.forEachIndexed { charIndex, char ->
                        item {
                            TimelineItem(charIndex, char, text.text)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TimelineItem(index: Int, eval: CharEvaluation, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val imageVectorToColor = when (eval) {
            is CharEvaluation.Correct -> Icons.Filled.CheckCircleOutline to MaterialTheme.colors.onBackground
            is CharEvaluation.TypingError -> Icons.Filled.ErrorOutline to MaterialTheme.colors.error
            is CharEvaluation.FingerError -> Icons.Filled.CleanHands to Color.Yellow
        }
        Icon(
            imageVector = imageVectorToColor.first,
            tint = imageVectorToColor.second,
            contentDescription = null
        )
        Icon(imageVector = Icons.Filled.Timer, contentDescription = null)
        Text(text = "${eval.timeRemaining.div(1000f)}s", modifier = Modifier.width(80.dp))
        Text(text = "Expected: ${eval.getExpectedChar(text)}")
        if (eval is CharEvaluation.TypingError) {
            Text(text = "Actual: ${eval.actual}")
        }
        if (eval is CharEvaluation.FingerError) {
            Text(text = "Expected Finger: ${eval.fingerUsed?.expected}")
            Text(text = "Actual Finger: ${eval.fingerUsed?.actual}")
        }
    }
}
