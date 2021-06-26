@file:Suppress("FunctionName")

package ui.exercise.results

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import textgen.error.ExerciseEvaluation
import ui.exercise.results.analysis.AnalysisScreen
import ui.exercise.results.overview.ResultsOverview
import ui.exercise.results.timeline.ResultsTimeline

@OptIn(ExperimentalFoundationApi::class)
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


