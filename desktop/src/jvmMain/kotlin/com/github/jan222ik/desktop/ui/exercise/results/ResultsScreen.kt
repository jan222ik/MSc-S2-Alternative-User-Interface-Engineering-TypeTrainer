@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.results

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import com.github.jan222ik.desktop.ui.exercise.results.analysis.AnalysisScreen
import com.github.jan222ik.desktop.ui.exercise.results.overview.ResultsOverview
import com.github.jan222ik.desktop.ui.exercise.results.timeline.ResultsTimeline

@OptIn(ExperimentalFoundationApi::class)
@HasDoc
@Composable
fun ResultsScreen(
    exerciseEvaluation: ExerciseEvaluation,
    innerRouting: ResultsRoutes,
    isStandalone: Boolean = true
) {
    val intent = ResultIntent(exerciseEvaluation)
    BaseResultScreen(
        resultsRoutes = innerRouting
    ) { current ->
        when (current) {
            ResultsRoutes.OVERVIEW -> ResultsOverview(intent, exerciseEvaluation, isStandalone)
            ResultsRoutes.ANALYSIS -> AnalysisScreen(exerciseEvaluation, isStandalone)
            ResultsRoutes.ERROR_HEATMAP -> Text(+current.title)
            ResultsRoutes.TIMELINE -> ResultsTimeline(intent, isStandalone)
        }
    }
}


