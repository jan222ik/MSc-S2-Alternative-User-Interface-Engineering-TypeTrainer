@file:Suppress("FunctionName")

package ui.exercise.results.timeline

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Timer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import textgen.error.CharEvaluation
import ui.exercise.results.ResultIntent

@ExperimentalFoundationApi
@Composable
fun ColumnScope.ResultsTimeline(intent: ResultIntent, isStandalone: Boolean) {
    val widthFraction = when (isStandalone) {
        true -> 0.7f
        false -> 1f
    }
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
                item {
                    Column {
                        text.chars.forEachIndexed { charIndex, char ->
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
        }
        Icon(
            imageVector = imageVectorToColor.first,
            tint = imageVectorToColor.second,
            contentDescription = null
        )
        Icon(imageVector = Icons.Filled.Timer, contentDescription = null)
        Text(text = "${eval.timeRemaining.div(1000f)}s")
        Text(text = "Expected: ${eval.getExpectedChar(text)}")
        if (eval is CharEvaluation.TypingError) {
            Text(text = "Actual: ${eval.actual}")
        }
    }
}
