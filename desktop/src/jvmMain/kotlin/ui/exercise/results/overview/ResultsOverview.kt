@file:Suppress("FunctionName")

package ui.exercise.results.overview

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import textgen.error.ExerciseEvaluation
import textgen.generators.impl.RandomCharOptions
import textgen.generators.impl.RandomKnownTextOptions
import textgen.generators.impl.RandomKnownWordOptions
import ui.dashboard.ApplicationRoutes
import ui.exercise.AbstractTypingOptions
import ui.exercise.ExerciseMode
import ui.exercise.TypingType
import ui.exercise.results.ResultIntent
import ui.general.WindowRouterAmbient
import ui.util.i18n.KeyI18N
import ui.util.i18n.LocalTranslationI18N
import ui.util.i18n.RequiresTranslationI18N
import ui.util.i18n.i18n

@Composable
fun ColumnScope.ResultsOverview(intent: ResultIntent, exerciseEvaluation: ExerciseEvaluation, isStandalone: Boolean) {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f.takeIf { isStandalone } ?: 1f)
            .fillMaxHeight()
            .align(Alignment.CenterHorizontally),
        horizontalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(0.4f),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            KeyPoints(
                modifier = Modifier.fillMaxHeight(0.7f).fillMaxWidth(),
                eval = exerciseEvaluation,
                options = exerciseEvaluation.options
            )
            SessionOptions(
                modifier = Modifier.fillMaxSize(),
                options = intent.data.options
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Goals(
                modifier = Modifier.fillMaxHeight(0.5f)
            )
            Achievements(
                modifier = Modifier.fillMaxHeight(),
                achievements = ResultIntent.getAchievements()
            )
        }
    }
}

val convertToMinSecString: (Float) -> String = {
    val minVal = it.div(60f).toInt()
    val min = "$minVal min"
    val secVal = it.rem(60f).toInt()
    val sec = "$secVal sec"
    when {
        minVal != 0 && secVal != 0 -> "$min, $sec"
        minVal == 0 && secVal != 0 -> sec
        else -> min
    }
}

@Composable
fun SessionOptions(modifier: Modifier, options: AbstractTypingOptions) {
    val strResExercise = i18n.str.exercise.selection.exerciseMode
    val strResTyping = i18n.str.exercise.selection.typingType
    val strResText = i18n.str.exercise.selection.textMode

    BaseDashboardCard(
        modifier = modifier
    ) {


        Box(modifier = Modifier.fillMaxSize()) {
            val router = WindowRouterAmbient.current
            TextButton(
                modifier = Modifier.align(Alignment.TopEnd),
                onClick = {
                    router.navTo(ApplicationRoutes.Exercise.ExerciseSelection(options))
                }
            ) {
                Text(+LocalTranslationI18N("Restart Exercise", "Übung Neustarten"))
            }
            val verticalArrangement = Arrangement.spacedBy(5.dp)
            Column(
                verticalArrangement = verticalArrangement,
            ) {
                val indent = Modifier.padding(start = 16.dp)
                Text(+LocalTranslationI18N("Session Options:", "Session-Optionen:"))
                Column(
                    verticalArrangement = verticalArrangement,
                    modifier = indent
                ) {
                    SessionOptionsItem(
                        name = strResExercise.duration,
                        value = options.durationMillis.div(1000f).let(convertToMinSecString)
                    )
                    SessionOptionsItem(
                        name = strResExercise.exerciseMode,
                        value = when (options.exerciseMode) {
                            ExerciseMode.Timelimit -> +strResExercise.timelimit
                            ExerciseMode.NoTimelimit -> +strResExercise.noTimeLimit
                        }
                    )
                    SessionOptionsItem(
                        name = strResTyping.typingType,
                        value = when (options.typingType) {
                            TypingType.MovingCursor -> +strResTyping.movingCursor
                            TypingType.MovingText -> +strResTyping.movingText
                        }
                    )
                }
                Text(
                    text = +LocalTranslationI18N("Text Generation Options:", "Optionen für Textgenerierung:")
                )
                Column(
                    verticalArrangement = verticalArrangement,
                    modifier = indent
                ) {
                    SessionOptionsItem(
                        name = strResText.textMode,
                        value = when (options.generatorOptions) {
                            is RandomCharOptions -> +strResText.randomChars
                            is RandomKnownWordOptions -> +strResText.randomWords
                            is RandomKnownTextOptions -> +strResText.literature
                            else -> throw IllegalArgumentException(
                                "No string resource exists for this options type"
                            )
                        }
                    )
                    SessionOptionsItem(
                        name = LocalTranslationI18N("Seed", "Seed"),
                        value = options.generatorOptions.seed
                    )
                }
            }

        }
    }
}

@Composable
fun <T> SessionOptionsItem(
    name: KeyI18N,
    value: T
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = +name + ":",
            modifier = Modifier.weight(0.3f)
        )
        Text(
            text = value.toString(),
            modifier = Modifier.weight(0.55f)
        )
    }
}

@Composable
fun KeyPoints(modifier: Modifier, eval: ExerciseEvaluation, options: AbstractTypingOptions) {
    BaseDashboardCard(
        modifier = modifier
    ) {
        val strResExercise = i18n.str.exercise.selection.exerciseMode
        val strResKeypoints = i18n.str.exercise.results.overview_keypoints

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = +i18n.str.exercise.results.overview.keyPoints,
                style = MaterialTheme.typography.h5
            )
            Column(
                modifier = Modifier.fillMaxWidth().padding(start = 16.dp),
                horizontalAlignment = Alignment.Start
            ) {
                // Duration
                KeyPointsItem(
                    title = strResExercise.duration,
                    value = options.durationMillis.div(1000f).let(convertToMinSecString)
                )
                // Date
                // TODO
                // Words Typed
                KeyPointsItem(
                    title = strResKeypoints.wordsTyped,
                    value = eval.wordsTyped.toString()
                )
                // Chars Typed
                KeyPointsItem(
                    title = strResKeypoints.charsTyped,
                    value = eval.totalCharsTyped.toString()
                )
                // WPM
                KeyPointsItem(
                    title = strResKeypoints.wpm,
                    value = eval.wps.times(60).toInt().toString()
                )
                // CPM
                KeyPointsItem(
                    title = strResKeypoints.cpm,
                    value = eval.cps.times(60).toInt().toString()
                )
                Text(
                    text = +LocalTranslationI18N("Errors:", "Fehler:"),
                    style = MaterialTheme.typography.h5
                )
                Column(
                    modifier = Modifier.padding(start = 16.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(5.dp)
                ) {
                    // Total Errors
                    KeyPointsItem(
                        title = strResKeypoints.totalErrors,
                        value = eval.totalErrors.toString()
                    )
                    // Accuracy
                    KeyPointsItem(
                        title = strResKeypoints.accuracy,
                        value = eval.totalAccuracy.toPercentage(2) + "%"
                    )

                    // Typing Errors
                    KeyPointsItem(
                        title = strResKeypoints.typingErrors,
                        value = eval.falseCharsTyped.toString()
                    )
                    // Typing Errors %
                    KeyPointsItem(
                        title = strResKeypoints.typingErrorsPercentage,
                        value = eval.falseCharsTyped.div(eval.totalErrors.toFloat()).toPercentage(2) + "%"
                    )
                    Text(
                        text = +LocalTranslationI18N("Typing Errors per Category:", "Tippfehler pro Kategorie:"),
                        style = MaterialTheme.typography.h6
                    )
                    Column(
                        modifier = Modifier.padding(start = 16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        // Typing Errors Case
                        KeyPointsItem(
                            title = strResKeypoints.typingErrorsCase,
                            value = eval.falseCharsTypedCase.toString()
                        )
                        // Typing Errors Case %
                        KeyPointsItem(
                            title = strResKeypoints.typingErrorsCasePercentatge,
                            value = eval.falseCharsTypedCase.div(eval.falseCharsTyped.toFloat()).toPercentage(2) + "%"
                        )

                        // Typing Errors Whitespace
                        KeyPointsItem(
                            title = strResKeypoints.typingErrorsWhitespace,
                            value = eval.falseCharsTypedWhitespace.toString()
                        )
                        // Typing Errors Whitespace %
                        KeyPointsItem(
                            title = strResKeypoints.typingErrorsWhitespacePercentatge,
                            value = eval
                                .falseCharsTypedWhitespace
                                .div(eval.falseCharsTyped.toFloat())
                                .toPercentage(2) + "%"
                        )
                    }


                    // Finger Errors
                    KeyPointsItem(
                        title = strResKeypoints.fingerErrors,
                        value = eval.falseKeyFingerStrokes.toString()
                    )
                    // Finger Errors %
                    KeyPointsItem(
                        title = strResKeypoints.fingerErrorsPercentage,
                        value = eval.falseKeyFingerStrokes.div(eval.totalErrors.toFloat()).toPercentage(2) + "%"
                    )
                }
            }
        }
    }
}

fun Float.toPercentage(posAfter: Int): String {
    val mul = (0..posAfter).fold(1) { acc, i -> acc.times(10.takeUnless { i == 0 } ?: 1) }
    return this.times(100).times(mul).toInt().div(mul.toFloat()).toString()
}

@Composable
fun KeyPointsItem(title: KeyI18N, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Text(
            text = +title,
            modifier = Modifier.weight(0.45f)
        )
        Text(
            text = value,
            modifier = Modifier.weight(0.55f)
        )
    }
}


@Composable
fun Goals(modifier: Modifier) {
    BaseDashboardCard(
        modifier = modifier
    ) {
        Text(text = +i18n.str.exercise.results.overview.goals)
    }
}

@Composable
fun Achievements(modifier: Modifier, achievements: List<List<String>>) {
    BaseDashboardCard(
        modifier = modifier
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Text(text = +i18n.str.exercise.results.overview.achievements)
                val color = MaterialTheme.colors.primary
                Text(
                    text = achievements.size.toString(),
                    modifier = Modifier.drawBehind {
                        drawCircle(color = color, radius = 13f)
                    }
                )

            }
            LazyColumn(
                contentPadding = PaddingValues(top = 5.dp),
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                items(achievements) { achievement ->
                    Card(
                        backgroundColor = MaterialTheme.colors.background,
                        border = BorderStroke(width = 2.dp, color = MaterialTheme.colors.primary),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth(0.85f).padding(top = 5.dp),
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth().padding(10.dp),
                        ) {
                            Text(
                                text = achievement.first(),
                                fontStyle = FontStyle.Italic,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                modifier = Modifier.align(Alignment.Start)
                            )
                            Text(
                                text = achievement.last(),
                                color = MaterialTheme.colors.primary,
                                modifier = Modifier.align(Alignment.End)
                            )
                        }
                    }
                }
            }
        }
    }
}
