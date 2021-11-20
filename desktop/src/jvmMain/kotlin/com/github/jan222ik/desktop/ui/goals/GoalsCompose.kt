@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.goals

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
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
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.i18n.LocalTranslationI18N
import com.github.jan222ik.desktop.ui.util.i18n.i18n
import kotlin.math.roundToInt

@Composable
fun GoalComposeScreen() {
    val router = WindowRouterAmbient.current
    GoalComposeScreenLayout(
        form = {
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = +i18n.str.debug.notImplementedYet,
                style = MaterialTheme.typography.h2
            )
        },
        goodGoalDescription = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = +LocalTranslationI18N("What makes a good goal ?", "Was macht ein gutes Ziel aus ?"),
                    style = MaterialTheme.typography.h4
                )
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = +LocalTranslationI18N(
                        """Think about what you want to achieve, and commit to it.
                        | To maintain motivation a goal should be tangible. 
                        | Thus, the goals need to be specific, measurable, attainable, 
                        | relevant and time-bound. (SMART- Goals)""".trimMargin().replace("\n", "")
                    ,
                        """Überlege dir, was du erreichen willst, und bleib dran.
                        | Um motiviert zu bleiben, sollte ein Ziel realistisch sein. 
                        | Deshalb sollten Ziele spezifisch, messbar, erreichbar, 
                        | relevant und zeitlich limitiert sein. (SMART- Goals)""".trimMargin().replace("\n", "")),
                    style = MaterialTheme.typography.h5
                )
            }
        },
        goalInWords = {
            Column(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = +LocalTranslationI18N("Your Goal in Words:", "Dein Ziel in Worten:"),
                    style = MaterialTheme.typography.h4,
                    color = MaterialTheme.colors.primary
                )
                Text(
                    modifier = Modifier.padding(horizontal = 32.dp),
                    text = +LocalTranslationI18N(
                        """Your Goal of type ‘IncreaseTyping Speed’ provides you with a speed progress tracking.
                            | The Goal is to increase your speed by ‘1’ Words
                            |  per Minute within the next  7 Days.""".trimMargin().replace("\n", ""),
                        """Dein Ziel vom Typ ‘Geschwindigkeit erhöhen’ bietet eine Geschwindigkeitsfortschrittsverfolgung.
                            | Das Ziel ist, deine Geschwindigkeit um ‘1‘ Wort
                            |  pro Minute in den nächsten 7 Tage zu erhöhen.""".trimMargin().replace("\n", "")
                    ),
                    style = MaterialTheme.typography.h5
                )
            }
        },
        createButton = {
            Button(
                onClick = {
                    // TODO Create goal and open it instead of dashboard
                    router.navTo(ApplicationRoutes.Dashboard())
                }
            ) {
                Text(
                    text = +LocalTranslationI18N("Create Goal", "Ziel erstellen"),
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
                    BoxWithConstraints(modifier = Modifier.fillMaxSize(), content = form)
                }
                BaseDashboardCard(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize(), content = goodGoalDescription)
                }
                BaseDashboardCard(
                    modifier = Modifier.fillMaxSize()
                ) {
                    BoxWithConstraints(modifier = Modifier.fillMaxSize(), content = goalInWords)
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
