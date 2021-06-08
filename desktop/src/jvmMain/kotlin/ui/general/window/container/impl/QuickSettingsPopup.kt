@file:Suppress("FunctionName")

package ui.general.window.container.impl


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import ui.components.outlined_radio_button.LabeledOutlinedRadioButtonGroup
import ui.dashboard.ApplicationRoutes
import ui.general.WindowRouterAmbient
import ui.util.i18n.LanguageAmbient
import ui.util.i18n.LanguageDefinition
import ui.util.i18n.i18n

@Composable
internal fun QuickSettingsPopup(
    height: Dp,
    windowControlsGroupPadding: Dp,
    onDismissRequest: () -> Unit
) {
    val yPx = with(LocalDensity.current) { height.toPx() }
    val xPx = with(LocalDensity.current) { windowControlsGroupPadding.toPx() }
    Popup(
        alignment = Alignment.TopEnd,
        onDismissRequest = onDismissRequest
    ) {
        TypeTrainerTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clickable(
                        onClick = onDismissRequest,
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ),
            ) {
                Card(
                    modifier = Modifier
                        .width(IntrinsicSize.Min)
                        .align(Alignment.TopEnd)
                        .offset { IntOffset(xPx.toInt(), yPx.toInt()) },
                    backgroundColor = MaterialTheme.colors.background,
                    shape = RoundedCornerShape(16.dp),
                    elevation = 0.dp
                ) {
                    Card(
                        modifier = Modifier.padding(8.dp),
                        backgroundColor = MaterialTheme.colors.surface,
                        shape = RoundedCornerShape(16.dp),
                        elevation = 0.dp
                    ) {
                        val router = WindowRouterAmbient.current
                        Column(modifier = Modifier.padding(8.dp).width(IntrinsicSize.Min)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = +i18n.str.settings.quickies.title
                                )
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .clickable(onClick = onDismissRequest),
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close settings balloon"
                                )
                            }
                            Row {
                                val current = LanguageAmbient.current
                                val langOptions = listOf(
                                    i18n.str.settings.languages.eng to LanguageDefinition.English,
                                    i18n.str.settings.languages.ger to LanguageDefinition.German
                                )
                                LabeledOutlinedRadioButtonGroup(
                                    modifier = Modifier.width(IntrinsicSize.Min),
                                    label = +i18n.str.settings.languages.language + ":",
                                    selected = langOptions.indexOfFirst { current.language == it.second },
                                    options = langOptions,
                                    optionTransform = { +it.first },
                                    onSelectionChange = {
                                        current.changeLanguage(langOptions[it].second)
                                    }
                                )
                            }
                            OutlinedButton(
                                onClick = {
                                    router.navTo(ApplicationRoutes.Settings)
                                    onDismissRequest.invoke()
                                }
                            ) {
                                Text(+i18n.str.settings.quickies.action_open_settings)
                            }
                        }
                    }
                }
            }
        }
    }
}
