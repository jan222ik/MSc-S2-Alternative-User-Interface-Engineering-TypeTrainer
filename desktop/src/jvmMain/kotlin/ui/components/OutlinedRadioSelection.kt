@file:Suppress("FunctionName")

package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredHeight
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max

@ExperimentalLayout
@Composable
fun <T> OutlinedRadioSelection(
    modifier: Modifier = Modifier,
    label: String,
    forceLabelUnclipped: Boolean = true,
    options: List<T>,
    optionTransform: @Composable (T) -> String,
    selected: Int,
    onSelectionChanged: (Int) -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    optionTextPadding: PaddingValues = PaddingValues(5.dp)
) {
    val additionalHeight = max(optionTextPadding.calculateBottomPadding() + optionTextPadding.calculateTopPadding(), 0.dp)
    Card(
        modifier = modifier
            .border(
                width = 0.dp,
                color = MaterialTheme.colors.primary,
                shape = shape
            ),
        shape = shape,
        elevation = 0.dp,
        backgroundColor = MaterialTheme.colors.background,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            val labelWidthModifier = when {
                forceLabelUnclipped -> Modifier.preferredWidth(IntrinsicSize.Min)
                else -> Modifier.fillMaxWidth(1f / (options.size + 1))
            }
            Card(
                modifier = labelWidthModifier,
                backgroundColor = MaterialTheme.colors.background,
                shape = shape.copy(topEnd = CornerSize(0.dp), bottomEnd = CornerSize(0.dp)),
                elevation = 0.dp
            ) {
                Row {
                    OptionText(
                        text = label,
                        color = MaterialTheme.colors.primary,
                        textModifier = Modifier.padding(optionTextPadding)
                    )
                    VDivider(additionalHeight)
                }
            }
            // End should keep kurve, thus override only start side
            val lastItemShape = shape.copy(topStart = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
            options.forEachIndexed { index, it ->
                // Change the corner to rounded if last item
                val shapePerOption = when (index) {
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
                    shape = shapePerOption,
                    elevation = 0.dp
                ) {
                    Row {
                        OptionText(
                            text = optionTransform.invoke(it),
                            color = colorCombi.second,
                            textModifier = Modifier.padding(optionTextPadding)
                        )
                        VDivider(additionalHeight)
                    }
                }
            }
        }
    }
}

@Composable
private fun OptionText(text: String, color: Color, textModifier: Modifier = Modifier) {
    CenteringBox {
        Text(
            modifier = textModifier,
            text = text,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            color = color
        )
    }
}

@ExperimentalLayout
@Composable
private fun VDivider(additionalHeight: Dp) {
    Box(
        modifier = Modifier
            .background(color = MaterialTheme.colors.primary)
            .width(3.dp)
            .preferredHeight(IntrinsicSize.Max)
            .background(color = MaterialTheme.colors.primary)
    ) {
        Spacer(Modifier.fillMaxHeight().width(3.dp).background(color = MaterialTheme.colors.primary))
        Text(modifier = Modifier.padding(top = additionalHeight), text = "")
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
