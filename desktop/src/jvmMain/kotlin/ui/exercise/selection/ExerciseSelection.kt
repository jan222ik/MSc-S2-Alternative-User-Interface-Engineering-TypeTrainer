@file:Suppress("FunctionName")

package ui.exercise.selection

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.components.outlined_radio_button.LabeledOutlinedRadioButtonGroup
import ui.dashboard.BaseDashboardCard
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
                    label = "Text Mode:",
                    forceLabelUnclipped = false,
                    options = ExerciseSelectionIntent.textModeSelectionOptions,
                    optionTransform = @Composable { +it },
                    selected = textMode,
                    onSelectionChange = setTextMode,
                    shape = headerShape
                )
                //TextModeSelectionBody(
                //    selected = textMode
                //)
                ExerciseSelectionBodyWithSlot(
                    shape = bodyShape
                ) {
                    when (textMode) {
                        0 -> LiteratureSelectionBody()
                        1 -> WordRngSelectionBody()
                        2 -> CharRngSelectionBody()
                    }
                }
                Spacer(Modifier.height(50.dp))
                LabeledOutlinedRadioButtonGroup(
                    modifier = Modifier,
                    label = "Exercise Mode:",
                    forceLabelUnclipped = false,
                    options = ExerciseSelectionIntent.exerciseModeSelectionOptions,
                    optionTransform = @Composable { +it },
                    selected = exerciseMode,
                    onSelectionChange = setExerciseMode,
                    shape = headerShape
                )
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
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 0.dp,
                color = MaterialTheme.colors.primary,
                shape = shape
            ),
        shape = shape,
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        Column(Modifier.padding(all = 16.dp)) {
            container.invoke()
        }
    }
}


@Composable
private fun LiteratureSelectionBody() {
    Text(text = +i18n.str.exercise.selection.textMode.literatureDescription)
}

@Composable
private fun CharRngSelectionBody() {
    Text(text = +i18n.str.exercise.selection.textMode.randomChars)
}

@Composable
private fun WordRngSelectionBody() {
    Text(text = +i18n.str.exercise.selection.textMode.randomWords)
}


