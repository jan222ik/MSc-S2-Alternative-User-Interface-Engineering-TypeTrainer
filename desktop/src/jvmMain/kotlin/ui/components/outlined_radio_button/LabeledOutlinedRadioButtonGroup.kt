@file:Suppress("FunctionName")

package ui.components.outlined_radio_button

import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ui.components.outlined_radio_button.internal.OptionText
import ui.components.outlined_radio_button.internal.VDivider

@ExperimentalLayout
@Composable
fun <T> LabeledOutlinedRadioButtonGroup(
    modifier: Modifier = Modifier,
    label: String,
    forceLabelUnclipped: Boolean = true,
    options: List<T>,
    optionTransform: @Composable (T) -> String,
    selected: Int,
    onSelectionChange: (Int) -> Unit,
    shape: RoundedCornerShape = RoundedCornerShape(0.dp),
    primaryColor: Color = MaterialTheme.colors.primary,
    onPrimaryColor: Color = MaterialTheme.colors.onPrimary,
    cardBackgroundColor: Color = MaterialTheme.colors.background,
    onCardBackgroundColor: Color = MaterialTheme.colors.onBackground,
    optionTextPadding: PaddingValues = PaddingValues(5.dp)
) {
    OutlinedRadioButtonGroup(
        modifier = modifier,
        options = options,
        optionTransform = optionTransform,
        selected = selected,
        onSelectionChange = onSelectionChange,
        shape = shape,
        primaryColor = primaryColor,
        onPrimaryColor = onPrimaryColor,
        cardBackgroundColor = cardBackgroundColor,
        onCardBackgroundColor = onCardBackgroundColor,
        optionTextPadding = optionTextPadding,
        labelProvider = { additionalVerticalPadding ->
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
                    VDivider(additionalVerticalPadding)
                }
            }
        }
    )
}
