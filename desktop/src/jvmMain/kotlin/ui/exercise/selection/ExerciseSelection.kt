@file:Suppress("FunctionName")

package ui.exercise.selection

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import ui.dashboard.BaseDashboardCard

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
                MultiSelectionHeader(
                    modifier = Modifier,
                    label = "Text Mode:",
                    options = ExerciseSelectionIntent.textModeSelectionOptions,
                    selected = textMode,
                    onSelectionChanged = setTextMode
                )
                Spacer(Modifier.height(50.dp))
                MultiSelectionHeader(
                    modifier = Modifier,
                    label = "Exercise Mode:",
                    options = ExerciseSelectionIntent.exerciseModeSelectionOptions,
                    selected = exerciseMode,
                    onSelectionChanged = setExerciseMode
                )
            }
        }
    }
}


@ExperimentalLayout
@Composable
private fun MultiSelectionHeader(
    modifier: Modifier = Modifier,
    label: String,
    options: List<String>,
    selected: Int,
    onSelectionChanged: (Int) -> Unit
) {
    val roundedCornerDp = 15.dp
    val mainCardShape = RoundedCornerShape(
        topStart = roundedCornerDp,
        topEnd = roundedCornerDp
    )
    Card(
        modifier = modifier
            .border(
                width = 0.dp,
                color = MaterialTheme.colors.primary,
                shape = mainCardShape
            ),
        shape = mainCardShape,
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background
    ) {
        @Composable
        fun OptionText(text: String, color: Color) {
            CenteringBox {
                Text(
                    text = text,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = color
                )
            }
        }

        @Composable
        fun VDivider() {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colors.primary)
                    .width(3.dp)
                    .preferredHeight(IntrinsicSize.Max)
                    .background(color = MaterialTheme.colors.primary)
            ) {
                Spacer(Modifier.fillMaxHeight().width(3.dp).background(color = MaterialTheme.colors.primary))
                Text("")
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(1f / (options.size + 1)),
                backgroundColor = MaterialTheme.colors.background,
                shape = mainCardShape.copy(topEnd = CornerSize(0.dp)),
                elevation = 0.dp
            ) {
                Row {
                    OptionText(text = label, color = MaterialTheme.colors.primary)
                    VDivider()
                }
            }
            val lastItemShape = RoundedCornerShape(topEnd = roundedCornerDp)
            options.forEachIndexed { index, it ->
                // Change the corner to rounded if last item
                val shape = when (index) {
                    options.size -> lastItemShape
                    else -> RectangleShape
                }

                val colorCombi = when (selected) {
                    index -> MaterialTheme.colors.primary to MaterialTheme.colors.onPrimary
                    else -> MaterialTheme.colors.background to MaterialTheme.colors.onBackground
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f / (options.size - index))
                        .clickable { onSelectionChanged.invoke(index) },
                    backgroundColor = colorCombi.first,
                    shape = shape,
                    elevation = 0.dp
                ) {
                    Row {
                        OptionText(
                            text = it,
                            color = colorCombi.second
                        )
                        VDivider()
                    }
                }
            }
        }
    }
}

@Composable
private fun CenteringBox(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        content.invoke()
    }
}
