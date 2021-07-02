@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.results.analysis

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import com.github.jan222ik.desktop.ui.exercise.results.analysis.error_per_key_dist.ErrorPerKey
import com.github.jan222ik.desktop.ui.exercise.results.analysis.finger_error_dist.FingerError
import com.github.jan222ik.desktop.ui.exercise.results.analysis.keyinterval.KeyInterval

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
            ErrorPerKey(exerciseEvaluation, isStandalone)
        }
        item {
            FingerError(exerciseEvaluation, isStandalone)
        }
    }
}
