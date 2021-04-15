@file:Suppress("FunctionName")

package ui.exercise.results

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import textgen.error.ExerciseEvaluation
import ui.exercise.results.overview.ResultsOverview
import ui.exercise.results.timeline.ResultsTimeline

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
            ResultsRoutes.OVERVIEW -> ResultsOverview(intent, isStandalone)
            ResultsRoutes.ANALYSIS -> Text(+current.title)
            ResultsRoutes.ERROR_HEATMAP -> Text(+current.title)
            ResultsRoutes.TIMELINE -> ResultsTimeline(intent, isStandalone)
        }
    }
}
