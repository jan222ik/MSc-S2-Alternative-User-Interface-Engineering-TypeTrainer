@file:Suppress("FunctionName")

package ui.exercise.selection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import ui.components.outlined_radio_button.LabeledOutlinedRadioButtonGroup
import ui.dashboard.BaseDashboardCard
import ui.util.i18n.ResolverI18n
import ui.util.i18n.i18n

@ExperimentalLayout
@Composable
fun ExerciseSelection(selectionIntent: ExerciseSelectionIntent) {
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
                ExerciseSelectionBodyWithSlot(shape = bodyShape){
                    when(exerciseMode){
                        0 -> SpeedSelectionBody(selectionIntent = selectionIntent)
                        1 -> AccuracySelectionBody(selectionIntent = selectionIntent)
                        2 -> NoTimelimitSelectionBody(selectionIntent = selectionIntent)
                    }
                }
                Spacer(Modifier.height(50.dp))
                Row(modifier = Modifier.align(Alignment.End)){
                    Button(onClick = {

                    }){
                        Text(text = "start exercise")
                    }
                }
            }
        }
    }
}

@Composable
@ExperimentalLayout
private fun SpeedSelectionBody(selectionIntent: ExerciseSelectionIntent){
    ExerciseModeSubCard(selectionIntent = selectionIntent,
                        descriptionText = +i18n.str.exercise.selection.exerciseMode.speedDescription,
                        timeLimit = true)
}

@Composable
@ExperimentalLayout
private fun AccuracySelectionBody(selectionIntent: ExerciseSelectionIntent){
    ExerciseModeSubCard(selectionIntent = selectionIntent,
                        descriptionText = +i18n.str.exercise.selection.exerciseMode.accuracyDescription,
                        timeLimit = true)
}

@Composable
@ExperimentalLayout
private fun NoTimelimitSelectionBody(selectionIntent: ExerciseSelectionIntent){
    ExerciseModeSubCard(selectionIntent = selectionIntent,
                        descriptionText = +i18n.str.exercise.selection.exerciseMode.noTimeLimitDescription,
                        timeLimit = false)
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
@ExperimentalLayout
private fun LiteratureSelectionBody(selectionIntent: ExerciseSelectionIntent) {
    TextModeSubCard(selectionIntent = selectionIntent,
        descriptionText = +i18n.str.exercise.selection.textMode.literatureDescription)
}

@Composable
@ExperimentalLayout
private fun CharRngSelectionBody(selectionIntent: ExerciseSelectionIntent) {
    TextModeSubCard(selectionIntent = selectionIntent,
        descriptionText = +i18n.str.exercise.selection.textMode.randomCharsDescription)
}

@Composable
@ExperimentalLayout
private fun WordRngSelectionBody(selectionIntent: ExerciseSelectionIntent) {
    TextModeSubCard(selectionIntent = selectionIntent,
        descriptionText = +i18n.str.exercise.selection.textMode.randomWordsDescription)
}


@Composable
@ExperimentalLayout
private fun TextModeSubCard(selectionIntent: ExerciseSelectionIntent,
                            descriptionText: String,
){
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

@Composable
@ExperimentalLayout
private fun ExerciseModeSubCard(selectionIntent: ExerciseSelectionIntent,
                                descriptionText: String,
                                timeLimit: Boolean){
    Column {
        Text(text = descriptionText)
        Spacer(modifier = Modifier.height(25.dp))
        Row {
            val (duration, setDuration) = remember { selectionIntent.durationSelection }
            val roundedCornerDp = 15.dp
            val shape = RoundedCornerShape(
                topStart = roundedCornerDp,
                bottomStart = roundedCornerDp,
                topEnd = roundedCornerDp,
                bottomEnd = roundedCornerDp,
            )
            Spacer(modifier = Modifier.width(15.dp))
            if(timeLimit) {
                LabeledOutlinedRadioButtonGroup(
                    modifier = Modifier,
                    label = +i18n.str.exercise.selection.exerciseMode.duration + ":",
                    forceLabelUnclipped = false,
                    options = ExerciseSelectionIntent.durationSelectionOptions,
                    optionTransform = @Composable { +it },
                    selected = duration,
                    onSelectionChange = setDuration,
                    shape = shape
                )
            }
        }
    }
}

