@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.dashboard.goal_preview

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.TargetBasedAnimation
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.tween
import androidx.compose.desktop.Window
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import kotlinx.coroutines.launch
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.i18n.LanguageConfiguration
import com.github.jan222ik.desktop.ui.util.i18n.i18n

@Composable
@HasDoc
fun DashboardGoalsPreviewCard(goalIntend: GoalIntend = GoalIntend()) {
    BaseDashboardCard {
        Column {
            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.align(Alignment.CenterStart),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Assignment,
                        contentDescription = null
                    )
                    Text(
                        text = +i18n.str.dashboard.goals.goalsTitle,
                        style = MaterialTheme.typography.h5
                    )
                }
                TextButton(
                    modifier = Modifier.align(Alignment.CenterEnd),
                    onClick = {},
                    content = {
                        Text(text = +i18n.str.dashboard.goals.openAll)
                    }
                )
            }
            Row(
                modifier = Modifier.fillMaxSize(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                val (first, second) = goalIntend.getPreviewGoals()
                DashboardGoal(first, {})
                DashboardGoal(second, {})
                ComposeNewGoalBtn()
            }
        }
    }
}

@Composable
fun ComposeNewGoalBtn() {
    val router = WindowRouterAmbient.current
    val onHover = remember { mutableStateOf(false) }
    val color = animateColorAsState(
        when (onHover.value) {
            true -> MaterialTheme.colors.primary
            false -> MaterialTheme.colors.onBackground
        }
    )
    Box(
        modifier = Modifier
            .pointerMoveFilter(
                onEnter = {
                    onHover.component2()(true)
                    true
                },
                onExit = {
                    onHover.component2()(false)
                    true
                }
            )
            .clickable(
                onClick = {
                    router.navTo(ApplicationRoutes.Goals.Compose)
                }
            )
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(5.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = null,
                tint = color.value
            )
            Text(
                text = +i18n.str.dashboard.goals.composeNew,
                color = color.value
            )
        }
    }
}

@Composable
fun DashboardGoal(goal: MockGoal, onOpenGoal: (MockGoal) -> Unit) {
    val activeTrackColor = MaterialTheme.colors.primary
    val inactiveTrackColor = activeTrackColor.copy(alpha = 0.24f)//MaterialTheme.colors.background
    val (isHover, setHover) = remember { mutableStateOf(false) }
    val strokeStyle = Stroke(width = 5f)
    val hoverStrokeStyle = Stroke(width = 8f)

    val animTime = (1000 * goal.progress).toInt()
    val targetValue = 360f * goal.progress
    val anim = remember {
        TargetBasedAnimation(
            animationSpec = tween(durationMillis = animTime, easing = LinearEasing),
            typeConverter = Float.VectorConverter,
            initialValue = 0f,
            targetValue = targetValue
        )
    }
    var playTime by remember { mutableStateOf(0L) }
    var animationValue by remember { mutableStateOf(0f) }

    LaunchedEffect(targetValue) {
        launch {
            val startTime = withFrameNanos { it }
            do {
                playTime = withFrameNanos { it } - startTime
                animationValue = anim.getValueFromNanos(playTime)
            } while (!anim.isFinishedFromNanos(playTime))
        }
    }

    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .aspectRatio(1f)
            .clickable { onOpenGoal.invoke(goal) }
            .pointerMoveFilter(
                onEnter = {
                    setHover(true)
                    true
                },
                onExit = {
                    setHover(false)
                    true
                }
            ),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 0.dp
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp, bottom = 4.dp)
                    .padding(horizontal = 8.dp)
                    .size(150.dp)
                    .drawBehind {
                        drawArc(
                            color = inactiveTrackColor,
                            startAngle = 0f,
                            sweepAngle = 360f,
                            useCenter = false,
                            style = strokeStyle
                        )
                        drawArc(
                            color = activeTrackColor,
                            startAngle = -90f,
                            sweepAngle = animationValue,
                            useCenter = false,
                            style = if (isHover) hoverStrokeStyle else strokeStyle
                        )
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(+goal.centerText)
            }
            Column(
                modifier = Modifier.weight(1f).padding(top = 5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp)
            ) {
                Text(text = +goal.name)
                Text(
                    text = +goal.timeframe,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
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