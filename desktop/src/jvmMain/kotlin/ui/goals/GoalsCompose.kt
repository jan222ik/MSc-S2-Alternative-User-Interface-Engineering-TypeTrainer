@file:Suppress("FunctionName")

package ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import ui.dashboard.ApplicationRoutes
import ui.general.WindowRouterAmbient
import ui.util.i18n.RequiresTranslationI18N
import kotlin.math.roundToInt

@Composable
fun GoalComposeScreen() {
    val router = WindowRouterAmbient.current
    GoalComposeScreenLayout(
        form = {

        },
        goodGoalDescription = {

        },
        goalInWords = {

        },
        createButton = {
            Button(
                onClick = {
                    // TODO Create goal and open it instead of dashboard
                    router.navTo(ApplicationRoutes.Dashboard)
                }
            ) {
                Text(
                    text = +RequiresTranslationI18N("Create Goal"),
                    style = MaterialTheme.typography.h4
                )
            }
        }
    )
}

@Composable
private fun GoalComposeScreenLayout(
    form: @Composable BoxWithConstraintsScope.() -> Unit,
    goodGoalDescription: @Composable BoxWithConstraintsScope.() -> Unit,
    goalInWords: @Composable BoxWithConstraintsScope.() -> Unit,
    createButton: @Composable () -> Unit
) {
    val density = LocalDensity.current
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Layout(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 24.dp, horizontal = 16.dp),
            content = {
                BaseDashboardCard(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BoxWithConstraints(content = form)
                }
                BaseDashboardCard(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BoxWithConstraints(content = goodGoalDescription)
                }
                BaseDashboardCard(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BoxWithConstraints(content = goalInWords)
                }
                Row(
                    modifier = Modifier.padding(vertical = 32.dp, horizontal = 24.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    createButton.invoke()
                }
            }
        ) { measurable, constraints ->
            val pBtn = measurable.last().measure(constraints)
            val wrapperMaxHeight = constraints.maxHeight - pBtn.height
            val formsWidth = constraints.maxWidth.times(0.6f).roundToInt()
            val interPadding = with(density) { 32.dp.toPx() }.roundToInt()
            val goalPartsWidth = constraints.maxWidth.minus(formsWidth).minus(interPadding)
            val goalPartsHeight = wrapperMaxHeight.minus(interPadding).div(2)
            val pForms = measurable[0].measure(Constraints(maxHeight = wrapperMaxHeight, maxWidth = formsWidth))
            val goalPartsConstraints = Constraints(maxHeight = goalPartsHeight, maxWidth = goalPartsWidth)
            val pGoodGoal = measurable[1].measure(goalPartsConstraints)
            val pWordGoal = measurable[2].measure(goalPartsConstraints)
            layout(
                width = constraints.maxWidth,
                height = constraints.maxHeight
            ) {
                pForms.place(IntOffset.Zero)
                val goalPartsX = pForms.width.plus(interPadding)
                pGoodGoal.place(x = goalPartsX, y = 0)
                pWordGoal.place(x = goalPartsX, y = pGoodGoal.height.plus(interPadding))
                pBtn.place(x = 0, y = pForms.height)
            }
        }
    }
}
