@file:Suppress("FunctionName")

package ui.dashboard.goal_preview

import TypeTrainerTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import ui.dashboard.BaseDashboardCard
import ui.util.i18n.LanguageConfiguration
import ui.util.i18n.RequiresTranslationI18N

@Composable
fun DashboardGoalsPreviewCard(goalIntend: GoalIntend = GoalIntend()) {
    BaseDashboardCard {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    modifier = Modifier.align(Alignment.CenterStart),
                    text = +RequiresTranslationI18N("Goals:")
                )
                TextButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {},
                    content = {
                        Text(text = +RequiresTranslationI18N("Open all Goals"))
                    }
                )
            }
            Row {
                val (first, second) = goalIntend.getPreviewGoals()
                DashboardGoal(first)
                DashboardGoal(second)
            }
        }
    }
}

@Composable
fun DashboardGoal(goal: IGoal) {
    Card(
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Column {
            val backgroundColor = MaterialTheme.colors.background
            val progressColor = MaterialTheme.colors.primaryVariant
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .padding(horizontal = 8.dp)
                    .size(200.dp)
                    .drawWithContent {
                        drawContent()
                        drawArc(
                            color = backgroundColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = Stroke(width = 15f)
                        )
                        drawArc(
                            color = progressColor,
                            startAngle = 0f,
                            sweepAngle = goal.current * 360f,
                            useCenter = false,
                            style = Stroke(width = 15f)
                        )
                    }
            )
            Text(text = goal.name)
        }
    }
}

fun main(args: Array<String>) {
    Window {
        LanguageConfiguration {
            TypeTrainerTheme {
                DashboardGoalsPreviewCard()
            }
        }
    }
}
