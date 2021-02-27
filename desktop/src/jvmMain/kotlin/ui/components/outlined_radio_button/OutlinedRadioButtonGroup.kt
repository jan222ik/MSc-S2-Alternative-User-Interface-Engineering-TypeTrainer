@file:Suppress("FunctionName")

package ui.components.outlined_radio_button

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import ui.components.outlined_radio_button.internal.OptionText
import ui.components.outlined_radio_button.internal.VDivider

@ExperimentalLayout
@Composable
fun <T> OutlinedRadioButtonGroup(
    modifier: Modifier = Modifier,
    options: List<T>,
    optionTransform: @Composable (T) -> String,
    selected: Int,
    onSelectionChange: (Int) -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    primaryColor: Color = MaterialTheme.colors.primary,
    onPrimaryColor: Color = MaterialTheme.colors.onPrimary,
    cardBackgroundColor: Color = MaterialTheme.colors.background,
    onCardBackgroundColor: Color = MaterialTheme.colors.onBackground,
    optionTextPadding: PaddingValues = PaddingValues(5.dp),
    labelProvider: @Composable ((additionalVerticalPadding: Dp) -> Unit)? = null
) {
    val additionalHeight =
        max(optionTextPadding.calculateBottomPadding() + optionTextPadding.calculateTopPadding(), 0.dp)
    Card(
        modifier = modifier
            .border(
                width = 0.dp,
                color = primaryColor,
                shape = shape
            ),
        shape = shape,
        elevation = 0.dp,
        backgroundColor = cardBackgroundColor,
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            labelProvider?.invoke(additionalHeight)
            // End should keep kurve, thus override only start side
            val lastItemShape = shape.copy(topStart = CornerSize(0.dp), bottomStart = CornerSize(0.dp))
            options.forEachIndexed { index, it ->
                // Change the corner to rounded if last item
                val shapePerOption = when (index) {
                    options.size -> lastItemShape
                    else -> RectangleShape
                }

                val colorCombi = when (selected) {
                    index -> primaryColor to onPrimaryColor
                    else -> cardBackgroundColor to onCardBackgroundColor
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth(1f / (options.size - index))
                        .clickable { onSelectionChange.invoke(index) },
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
