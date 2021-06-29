@file:Suppress("FunctionName")

package ui.exercise.results

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Measurable
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.util.router.BaseRouter
import com.github.jan222ik.common.ui.util.router.Router
import ui.components.outlined_radio_button.OutlinedRadioButtonGroup
import ui.dashboard.ApplicationRoutes
import ui.general.WindowRouterAmbient
import ui.util.i18n.KeyI18N
import ui.util.i18n.i18n

val ResultsSubRouterAmbient = compositionLocalOf<Router<ResultsRoutes>> { error("No active router found!") }

@Composable
fun BaseResultScreen(
    resultsRoutes: ResultsRoutes,
    screenImpls: @Composable ColumnScope.(ResultsRoutes) -> Unit
) {
    val options = listOf(*ResultsRoutes.values())
    BaseRouter(
        initialRoute = resultsRoutes,
        ambient = ResultsSubRouterAmbient
    ) { current, _ ->
        val globalRouter = WindowRouterAmbient.current
        Column {
            Layout(
                modifier = Modifier.fillMaxSize(),
                content = {
                    val router = ResultsSubRouterAmbient.current
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                    ) {
                        OutlinedRadioButtonGroup(
                            modifier = Modifier.fillMaxWidth(0.6f),
                            cardBackgroundColor = MaterialTheme.colors.surface,
                            shape = RoundedCornerShape(15.dp),
                            selected = current.ordinal,
                            onSelectionChange = { router.setRoot(options[it]) },
                            options = options,
                            optionTransform = { +it.title }
                        )
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight()
                            .padding(top = 15.dp)
                    ) {
                        screenImpls.invoke(this, current)
                    }
                    Row(modifier = Modifier.padding(bottom = 16.dp, top = 32.dp)) {
                        ExtendedFloatingActionButton(
                            onClick = {
                                globalRouter.setRoot(ApplicationRoutes.Dashboard)
                            },
                            text = {
                                Text(text = +i18n.str.exercise.results.base.returnToDashboard)
                            },
                            icon = {
                                Icon(
                                    imageVector = Icons.Filled.Dashboard,
                                    contentDescription = null
                                )
                            },
                            backgroundColor = MaterialTheme.colors.primary
                        )
                    }
                }
            ) { list: List<Measurable>, inConstraints: Constraints ->
                val constraints = inConstraints.copy(minHeight = 0, minWidth = 0)
                val pRadioGroup = list[0].measure(constraints)
                val pEfab = list[2].measure(constraints)
                val remHeight = constraints.copy(
                    maxHeight = constraints.maxHeight.minus(pRadioGroup.height).minus(pEfab.height)
                )
                val pContent = list[1].measure(remHeight)

                val maxWidth = maxOf(pRadioGroup.width, pEfab.width, pContent.width)
                layout(
                    width = maxWidth,
                    height = constraints.maxHeight
                ) {
                    pRadioGroup.placeRelative(x = 0, y = 0)
                    pContent.placeRelative(x = 0, y = pRadioGroup.height)
                    pEfab.placeRelative(
                        x = maxWidth.minus(pEfab.width).div(2),
                        y = constraints.maxHeight.minus(pEfab.height)
                    )
                }
            }
        }
    }
}

enum class ResultsRoutes(val title: KeyI18N) {
    OVERVIEW(i18n.str.exercise.results.base.overview),
    ANALYSIS(i18n.str.exercise.results.base.analysis),
    ERROR_HEATMAP(i18n.str.exercise.results.base.errorHeatmap),
    TIMELINE(i18n.str.exercise.results.base.timeline)
}
