@file:Suppress("FunctionName")

package ui.exercise.results.analysis.error_per_key_dist

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import textgen.error.ExerciseEvaluation


@Composable
fun LazyItemScope.ErrorPerKey(exerciseEvaluation: ExerciseEvaluation, isStandalone: Boolean) {
    val data = remember(exerciseEvaluation) { exerciseEvaluation.errorRateOfKeys }
    BaseDashboardCard(
        modifier = Modifier
            .fillParentMaxWidth(0.8f.takeIf { isStandalone } ?: 1f)
            .fillParentMaxHeight(0.8f)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Error Rate of Characters:", style = MaterialTheme.typography.h5)
            /*
            Row(horizontalArrangement = Arrangement.spacedBy(5.dp)) {
                Text("Longest: ${exerciseEvaluation.longestTimeTweenStrokes}ms")
                Text("Shortest: ${exerciseEvaluation.shortestTimeTweenStrokes}ms")
                Text("Average: ${exerciseEvaluation.averageTimeTweenStrokes}ms")
            }

             */
            Box {
                BoxWithConstraints(
                    modifier = Modifier.fillParentMaxWidth()
                ) {
                    ErrorPerKeyChart(
                        chartData = data,
                        modifier = Modifier.size(this.maxWidth, this.maxHeight)
                    )
                }
                TextButton(
                    modifier = Modifier.align(Alignment.TopEnd).padding(5.dp),
                    onClick = {
                        println("Open other window btn clicked!")
                        Window(
                            title = "Chart: Time between Key Presses",
                            size = IntSize(width = 1920, height = 1080)
                        ) {
                            TypeTrainerTheme {
                                Surface(color = MaterialTheme.colors.background) {
                                    ErrorPerKeyChart(chartData = data, modifier = Modifier.fillMaxSize())
                                }
                            }
                        }
                    }
                ) {
                    Text("Open in other Window")
                }
            }
        }
    }
}
