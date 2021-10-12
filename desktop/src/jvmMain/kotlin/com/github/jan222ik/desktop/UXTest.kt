@file:Suppress("FunctionName")

package com.github.jan222ik.desktop

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.BoxWithConstraintsScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.common.ui.util.router.Router
import com.github.jan222ik.desktop.textgen.generators.AbstractGeneratorOptions
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownTextOptions
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordOptions
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.exercise.ExerciseMode
import com.github.jan222ik.desktop.ui.exercise.TypingOptions
import com.github.jan222ik.desktop.ui.exercise.TypingType
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import com.github.jan222ik.desktop.ui.util.i18n.LocalTranslationI18N
import java.time.LocalDateTime

@ExperimentalAnimationApi
object UXTest {

    val LocalIsUXTest = compositionLocalOf { mutableStateOf(false) }
    val LocalUXTestRun = compositionLocalOf { mutableStateOf(TestRun(variant = 0, step = 0)) }

    data class TestRun(
        val start: LocalDateTime = LocalDateTime.now(),
        val variant: Int,
        val step: Int
    )

    interface TestRunStep {
        val nr: Int
    }

    data class Step(
        override val nr: Int,
        val generatorOptions: AbstractGeneratorOptions,
        val type: TypingType
    ) : TestRunStep

    data class EndStep(override val nr: Int) : TestRunStep

    private val text1 = RandomKnownTextOptions(seed = 1L, language = LanguageDefinition.German)
    private val text2 = RandomKnownTextOptions(seed = 2L, language = LanguageDefinition.German)
    //private val text3 = RandomKnownTextOptions(seed = 3L, language = LanguageDefinition.German)

    private val words1 =
        RandomKnownWordOptions(seed = 1L, language = LanguageDefinition.German, minimalSegmentLength = 350)
    private val words2 =
        RandomKnownWordOptions(seed = 2L, language = LanguageDefinition.German, minimalSegmentLength = 350)
    //private val words3 =
    //    RandomKnownWordOptions(seed = 3L, language = LanguageDefinition.German, minimalSegmentLength = 350)

    private val variant1 = listOf(
        Step(nr = 1, generatorOptions = text1, type = TypingType.MovingText),
        Step(nr = 2, generatorOptions = words1, type = TypingType.MovingCursor),
        Step(nr = 3, generatorOptions = text2, type = TypingType.MovingText),
        Step(nr = 4, generatorOptions = words2, type = TypingType.MovingCursor)
    )

    val variant2 = listOf(
        Step(nr = 1, generatorOptions = text1, type = TypingType.MovingCursor),
        Step(nr = 2, generatorOptions = words1, type = TypingType.MovingText),
        Step(nr = 3, generatorOptions = text2, type = TypingType.MovingCursor),
        Step(nr = 4, generatorOptions = words2, type = TypingType.MovingText)
    )

    private fun startStep(step: Step, router: Router<ApplicationRoutes>) {
        val trainingOptions = TypingOptions(
            generatorOptions = step.generatorOptions,
            durationMillis = 60_000,
            exerciseMode = ExerciseMode.Timelimit,
            isCameraEnabled = false,
            typingType = step.type
        )
        val destination = ApplicationRoutes.Exercise.Training(
            trainingOptions = trainingOptions
        )
        router.navTo(destination)
    }

    @Composable
    fun Screen() {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            BaseDashboardCard(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .padding(vertical = 24.dp)
                    .fillMaxHeight()
            ) {
                val uxTest = LocalUXTestRun.current
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Content(uxTest.value)
                }
            }
        }
    }

    @Composable
    fun ColumnScope.Content(uxTest: TestRun) {
        val paddingVertical = 24.dp
        val variant = remember(uxTest) {
            when (uxTest.variant) {
                0 -> variant1
                1 -> variant2
                else -> error("Unknown Variant.")
            }.let { it + EndStep(it.size.inc()) }
        }
        Text(
            text = "UX Test (Variant" + +LocalTranslationI18N(
                eng = "",
                ger = "e"
            ) + " ${uxTest.variant})",
            style = MaterialTheme.typography.h3
        )
        Row(
            modifier = Modifier.weight(1f)//.fillMaxHeight(0.9f)
        ) {
            BoxWithConstraints {
                ProgressIndicator(
                    currentStep = uxTest.step,
                    totalSteps = variant.size,
                    paddingVertical = paddingVertical
                )
            }
            Column(
                modifier = Modifier
                    .padding(
                        vertical = paddingVertical,
                        horizontal = paddingVertical
                    )
                    .fillMaxHeight(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                variant.forEach {
                    val highlightColorAnim = animateColorAsState(
                        when {
                            uxTest.step < it.nr.dec() -> Color.Gray
                            uxTest.step == it.nr.dec() -> MaterialTheme.colors.primary
                            else -> MaterialTheme.colors.onSurface
                        }
                    )
                    if (it is Step) {
                        Row(
                            modifier = Modifier.height(150.dp),
                        ) {
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text(
                                    text = +LocalTranslationI18N(eng = "Step ", ger = "Schritt ") + it.nr.toString() + ":",
                                    style = MaterialTheme.typography.h4,
                                    color = highlightColorAnim.value
                                )
                                AnimatedVisibility(
                                    visible = it.nr.dec() <= uxTest.step
                                ) {
                                    Column(
                                        modifier = Modifier.padding(horizontal = 24.dp),
                                        verticalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        Row {
                                            Text(
                                                text = +LocalTranslationI18N(
                                                    eng = "Mode: ",
                                                    ger = "Modus: "
                                                ),
                                                style = MaterialTheme.typography.h5
                                            )
                                            Text(
                                                text = when (it.type) {
                                                    TypingType.MovingCursor -> LocalTranslationI18N(
                                                        eng = "Moving Cursor",
                                                        ger = "Bewegender Cursor"
                                                    )
                                                    TypingType.MovingText -> LocalTranslationI18N(
                                                        eng = "Moving Text",
                                                        ger = "Bewegender Text"
                                                    )
                                                }.observedString(it.type),
                                                style = MaterialTheme.typography.h5
                                            )
                                        }
                                        Row {
                                            Text(
                                                text = +LocalTranslationI18N(
                                                    eng = "Text Type: ",
                                                    ger = "Text Typ: "
                                                ),
                                                style = MaterialTheme.typography.h5
                                            )
                                            Text(
                                                text = when (it.generatorOptions) {
                                                    is RandomKnownWordOptions -> LocalTranslationI18N(
                                                        eng = "Random Words",
                                                        ger = "Zufälliger Wörter"
                                                    )
                                                    is RandomKnownTextOptions -> LocalTranslationI18N(
                                                        eng = "Random Text",
                                                        ger = "Zufälliger Text"
                                                    )
                                                    else -> error("Unknown Option")
                                                }.observedString(it.generatorOptions),
                                                style = MaterialTheme.typography.h5
                                            )
                                        }
                                    }
                                }
                            }
                            AnimatedVisibility(
                                modifier = Modifier
                                    .weight(3f, false)
                                    .align(Alignment.CenterVertically),
                                visible = it.nr.dec() == uxTest.step,
                                enter = fadeIn() + expandHorizontally(animationSpec = tween(durationMillis = 1000, delayMillis = 100)),
                                exit = fadeOut(animationSpec = tween(durationMillis = 1000, delayMillis = 100))
                                        + shrinkHorizontally(animationSpec = tween(durationMillis = 1000, delayMillis = 100))
                            ) {
                                val router = WindowRouterAmbient.current
                                Button(
                                    onClick = {
                                        startStep(
                                            step = it,
                                            router = router
                                        )
                                    }
                                ) {
                                    Text(
                                        text = +LocalTranslationI18N(eng = "Start Step", ger = "Schritt starten"),
                                        style = MaterialTheme.typography.h5
                                    )
                                }
                            }
                        }
                    } else {
                        if (it is EndStep) {
                            Row {
                                Text(
                                    text = +LocalTranslationI18N(eng = "End", ger = "Ende"),
                                    style = MaterialTheme.typography.h4,
                                    color = highlightColorAnim.value
                                )
                            }
                        }
                    }
                }
            }
        }
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterStart),
                text = +LocalTranslationI18N(eng = "Timestamp", ger = "Zeitstempel") + ": ${uxTest.start}",
                color = Color.LightGray.copy(alpha = 0.8f)
            )
        }
    }

    @Composable
    fun BoxWithConstraintsScope.ProgressIndicator(
        currentStep: Int,
        totalSteps: Int,
        totalWidth: Dp = 60.dp,
        paddingVertical: Dp = 15.dp,
        cornerRadius: Dp = 24.dp,
        dotColor: Color = MaterialTheme.colors.onBackground,
        trackColor: Color = MaterialTheme.colors.primary,
        backgroundColor: Color = MaterialTheme.colors.surface
    ) {
        val density = LocalDensity.current
        val vPad = with(density) { paddingVertical.toPx() }
        val shape = RoundedCornerShape(cornerRadius)
        val dotRadius = totalWidth.value.div(2).times(0.6f)
        val progress = remember { mutableStateOf(0f) }
        val progressHeight = animateFloatAsState(
            targetValue = progress.value,
            animationSpec = tween(delayMillis = 100, durationMillis = 1000)
        )
        LaunchedEffect(currentStep, maxHeight.value, vPad, dotRadius, totalSteps) {
            progress.value = currentStep * (maxHeight.value.minus(vPad.times(2))
                .minus(dotRadius.times(2)) / totalSteps.dec()) + (vPad + dotRadius).times(2)
        }
        Canvas(
            modifier = Modifier
                .fillMaxHeight()
                .width(totalWidth)
                .clip(shape)
        ) {
            drawRect(color = backgroundColor)
            drawRoundRect(
                color = trackColor,
                size = Size(
                    width = size.width,
                    height = progressHeight.value
                ),
                cornerRadius = CornerRadius(cornerRadius.toPx())
            )
            0.rangeTo(totalSteps).forEach {
                drawCircle(
                    color = dotColor,
                    radius = dotRadius,
                    center = Offset(
                        x = size.width / 2,
                        y = it * (maxHeight.value.minus(vPad.times(2))
                            .minus(dotRadius.times(2)) / totalSteps.dec()) + vPad + dotRadius
                    )
                )
            }
        }
    }
}
