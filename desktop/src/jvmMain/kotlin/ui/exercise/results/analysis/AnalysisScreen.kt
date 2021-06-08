@file:Suppress("FunctionName")

package ui.exercise.results.analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import textgen.error.ExerciseEvaluation
import ui.exercise.results.analysis.keyinterval.KeyInterval

@Composable
fun ColumnScope.AnalysisScreen(exerciseEvaluation: ExerciseEvaluation, isStandalone: Boolean) {
    LazyColumn(
        modifier = Modifier.align(Alignment.CenterHorizontally),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            KeyInterval(exerciseEvaluation, isStandalone)
        }
        item {
            KeyInterval(exerciseEvaluation, isStandalone)
        }
        item {
            KeyInterval(exerciseEvaluation, isStandalone)
        }
        item {
            KeyInterval(exerciseEvaluation, isStandalone)
        }
    }
}
