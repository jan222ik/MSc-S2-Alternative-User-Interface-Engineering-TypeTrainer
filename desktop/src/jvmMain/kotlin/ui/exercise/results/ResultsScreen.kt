@file:Suppress("FunctionName")

package ui.exercise.results

import androidx.compose.material.Text
import androidx.compose.runtime.Composable

@Composable
fun ResultsScreen(
    innerRouting: ResultsRoutes
) {
    BaseResultScreen(
        resultsRoutes = innerRouting
    ) { current ->
        when (current) {
            ResultsRoutes.OVERVIEW -> ResultsOverview()
            ResultsRoutes.ANALYSIS -> Text(+current.title)
            ResultsRoutes.ERROR_HEATMAP -> Text(+current.title)
            ResultsRoutes.TIMELINE -> Text(+current.title)
        }
    }
}
