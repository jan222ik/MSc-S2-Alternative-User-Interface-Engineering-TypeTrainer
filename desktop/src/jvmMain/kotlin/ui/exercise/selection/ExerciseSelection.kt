@file:Suppress("FunctionName")

package ui.exercise.selection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import ui.components.outlined_radio_button.LabeledOutlinedRadioButtonGroup
import ui.components.outlined_radio_button.OutlinedRadioButtonGroup
import ui.dashboard.ApplicationRoutes
import ui.general.WindowRouterAmbient
import ui.util.i18n.i18n

@Composable
fun ExerciseSelection(selectionIntentO: ExerciseSelectionIntent = ExerciseSelectionIntent()) {
    val selectionIntent = remember(selectionIntentO) { selectionIntentO }
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
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 16.dp)
                    .fillMaxSize()
            ) {
                val (textMode, setTextMode) = remember { selectionIntent.textModeSelection }
                val (exerciseMode, setExerciseMode) = remember { selectionIntent.exerciseModeSelection }
                val roundedCornerDp = 15.dp
                val headerShape = RoundedCornerShape(
                    topStart = roundedCornerDp,
                    topEnd = roundedCornerDp
                )
                val bodyShape = RoundedCornerShape(
                    bottomStart = roundedCornerDp,
                    bottomEnd = roundedCornerDp
                )
                LabeledOutlinedRadioButtonGroup(
                    modifier = Modifier,
                    label = +i18n.str.exercise.selection.textMode.textMode + ":",
                    forceLabelUnclipped = false,
                    options = ExerciseSelectionIntent.textModeSelectionOptions,
                    optionTransform = @Composable { +it },
                    selected = textMode,
                    onSelectionChange = setTextMode,
                    shape = headerShape
                )
                ExerciseSelectionBodyWithSlot(
                    shape = bodyShape
                ) {
                    when (textMode) {
                        0 -> LiteratureSelectionBody(selectionIntent = selectionIntent)
                        1 -> WordRngSelectionBody(selectionIntent = selectionIntent)
                        2 -> CharRngSelectionBody(selectionIntent = selectionIntent)
                    }
                }
                Spacer(Modifier.height(50.dp))
                LabeledOutlinedRadioButtonGroup(
                    modifier = Modifier,
                    label = +i18n.str.exercise.selection.exerciseMode.exerciseMode + ":",
                    forceLabelUnclipped = false,
                    options = ExerciseSelectionIntent.exerciseModeSelectionOptions,
                    optionTransform = @Composable { +it },
                    selected = exerciseMode,
                    onSelectionChange = setExerciseMode,
                    shape = headerShape
                )
                ExerciseSelectionBodyWithSlot(
                    shape = bodyShape
                ) {
                    val param = when (exerciseMode) {
                        0 -> i18n.str.exercise.selection.exerciseMode.speedDescription to true
                        1 -> i18n.str.exercise.selection.exerciseMode.accuracyDescription to true
                        2 -> i18n.str.exercise.selection.exerciseMode.noTimeLimitDescription to false
                        else -> throw IndexOutOfBoundsException()
                    }
                    ExerciseModeSubCard(
                        selectionIntent = selectionIntent,
                        descriptionText = +param.first,
                        timeLimit = param.second
                    )
                }
                Spacer(Modifier.height(50.dp))
                Row(
                    modifier = Modifier.align(Alignment.End),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    val router = WindowRouterAmbient.current
                    val (withFingerTracking, setWithFingerTracking) = selectionIntent.withFingerTracking
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        Checkbox(
                            checked = withFingerTracking,
                            onCheckedChange = setWithFingerTracking,
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colors.primary,
                            )
                        )
                        Text(
                            text = +i18n.str.exercise.selection.controls.useHandTracking,
                            style = MaterialTheme.typography.body1
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Button(
                        onClick = {
                            val options = selectionIntent.generateTypingOptions()
                            router.navTo(
                                dest = when (options.isCameraEnabled) {
                                    true -> ApplicationRoutes.Exercise.Connection.QRCode(options)
                                    false -> ApplicationRoutes.Exercise.Training(options)
                                }
                            )
                        }
                    ) {
                        Text(text = +i18n.str.exercise.selection.controls.startBtn)
                    }
                }
            }
        }
    }
}


/**
 * //TODO
 *
 * @param shape defines the outer shape of the card and its border
 * @param primaryColor defines the border color
 * @param cardBackgroundColor defines the color used for the background of the card
 * @param container Lambda for composable content
 */
@Composable
private fun ExerciseSelectionBodyWithSlot(
    modifier: Modifier = Modifier,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    primaryColor: Color = MaterialTheme.colors.primary,
    cardBackgroundColor: Color = MaterialTheme.colors.background,
    container: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = 0.dp,
                color = primaryColor,
                shape = shape
            ),
        shape = shape,
        elevation = 0.dp,
        backgroundColor = cardBackgroundColor
    ) {
        Column(Modifier.padding(all = 16.dp)) {
            container.invoke()
        }
    }
}


@Composable
private fun LiteratureSelectionBody(selectionIntent: ExerciseSelectionIntent) {
    TextModeSubCard(
        selectionIntent = selectionIntent,
        descriptionText = +i18n.str.exercise.selection.textMode.literatureDescription,
        languageSelection = true
    )
}

@Composable
private fun CharRngSelectionBody(selectionIntent: ExerciseSelectionIntent) {
    TextModeSubCard(
        selectionIntent = selectionIntent,
        descriptionText = +i18n.str.exercise.selection.textMode.randomCharsDescription,
        languageSelection = false
    )
}

@Composable
private fun WordRngSelectionBody(selectionIntent: ExerciseSelectionIntent) {
    TextModeSubCard(
        selectionIntent = selectionIntent,
        descriptionText = +i18n.str.exercise.selection.textMode.randomWordsDescription,
        languageSelection = true
    )
}

@Composable
private fun TextModeSubCard(
    selectionIntent: ExerciseSelectionIntent,
    descriptionText: String,
    languageSelection: Boolean
) {
    Column {
        Text(text = descriptionText)
        Spacer(modifier = Modifier.height(25.dp))
        Row {
            val (language, setLanguage) = remember { selectionIntent.languageSelection }
            val roundedCornerDp = 15.dp
            val shape = RoundedCornerShape(
                topStart = roundedCornerDp,
                bottomStart = roundedCornerDp,
                topEnd = roundedCornerDp,
                bottomEnd = roundedCornerDp,
            )
            Spacer(modifier = Modifier.width(15.dp))
            if (languageSelection) {
                LabeledOutlinedRadioButtonGroup(
                    modifier = Modifier,
                    label = +i18n.str.settings.languages.language + ":",
                    forceLabelUnclipped = false,
                    options = ExerciseSelectionIntent.languageSelectionOptions,
                    optionTransform = @Composable { +it },
                    selected = language,
                    onSelectionChange = setLanguage,
                    shape = shape
                )
            }
        }
    }
}

@Composable
private fun CustomDurationInput(selectionIntent: ExerciseSelectionIntent, setSelectionIndex: () -> Unit) {
    val (customDuration, setCustomDuration) = remember { selectionIntent.customDurationSelection }
    TextField(
        modifier = Modifier.onPreviewKeyEvent { setSelectionIndex.invoke(); false },
        value = customDuration,
        placeholder = { +i18n.str.exercise.selection.exerciseMode.customDuration },
        singleLine = true,
        trailingIcon = {
            Text(text = " min")
        },
        onValueChange = {
            setCustomDuration.invoke(it)
        }
    )
}

@Composable
private fun ExerciseModeSubCard(
    selectionIntent: ExerciseSelectionIntent,
    descriptionText: String,
    timeLimit: Boolean
) {
    Column(
        modifier = Modifier.wrapContentSize()
    ) {
        Text(text = descriptionText)
        Spacer(modifier = Modifier.height(25.dp))
        val (duration, setDuration) = remember { selectionIntent.durationSelection }
        Row {
            val roundedCornerDp = 15.dp
            val shape = RoundedCornerShape(
                topStart = roundedCornerDp,
                bottomStart = roundedCornerDp,
                topEnd = roundedCornerDp,
                bottomEnd = roundedCornerDp,
            )
            Spacer(modifier = Modifier.width(15.dp))
            if (timeLimit) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = +i18n.str.exercise.selection.exerciseMode.duration + ":")
                    Spacer(modifier = Modifier.width(15.dp))
                    OutlinedRadioButtonGroup(
                        modifier = Modifier.fillMaxWidth(0.6f),//.width(IntrinsicSize.Min),
                        options = ExerciseSelectionIntent.durationSelectionOptions,
                        optionTransform = @Composable {
                            /*
                            if (it.key == i18n.str.exercise.selection.exerciseMode.customDuration.key) {
                                CustomDurationInput(selectionIntent = selectionIntent, setSelectionIndex = {
                                    setDuration(ExerciseSelectionIntent.durationSelectionOptions.size)
                                })
                                ""
                            } else {
                                +it
                            }4
                             */
                            +it
                        },
                        selected = duration,
                        onSelectionChange = setDuration,
                        shape = shape
                    )
                }
            }
        }
        if (timeLimit && duration == ExerciseSelectionIntent.durationSelectionOptions.size - 1) {
            Spacer(modifier = Modifier.height(15.dp))
            Row(
                modifier = Modifier.padding(start = 50.dp),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Enter a duration:")
                CustomDurationInput(
                    selectionIntent = selectionIntent,
                    setSelectionIndex = {
                        //setDuration(ExerciseSelectionIntent.durationSelectionOptions.size)
                    }
                )
            }
        }
    }
}

