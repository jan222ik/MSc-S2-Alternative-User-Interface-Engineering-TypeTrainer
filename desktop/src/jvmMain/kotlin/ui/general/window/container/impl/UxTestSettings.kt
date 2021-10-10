@file:Suppress("FunctionName")

package ui.general.window.container.impl


import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconToggleButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Switch
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
import com.github.jan222ik.desktop.UXTest
import com.github.jan222ik.desktop.ui.components.outlined_radio_button.LabeledOutlinedRadioButtonGroup
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.i18n.LocalTranslationI18N
import com.github.jan222ik.desktop.ui.util.i18n.i18n


@OptIn(ExperimentalAnimationApi::class)
@Composable
internal fun UxTestSettings(
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
                        Column(modifier = Modifier.padding(8.dp).width(250.dp)) {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = +LocalTranslationI18N(
                                        eng = "UX Test Settings",
                                        ger = "UX Test Einstellungen",
                                    )
                                )
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .clickable(onClick = onDismissRequest),
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close settings balloon"
                                )
                            }
                            val localIsUxTest = UXTest.LocalIsUXTest.current
                            val localUxTest = UXTest.LocalUXTestRun.current
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Text("Enable UX-Test:")
                                Switch(
                                    checked = localIsUxTest.value,
                                    onCheckedChange = {
                                        localIsUxTest.value = it
                                    }
                                )
                            }
                            val options = listOf(
                                LocalTranslationI18N(eng = "Group 1", ger = "Gruppe 1"),
                                LocalTranslationI18N(eng = "Group 2", ger = "Gruppe 2")
                            )
                            LabeledOutlinedRadioButtonGroup(
                                modifier = Modifier,
                                label = +LocalTranslationI18N(eng = "Variant:", ger = "Variante:"),
                                selected = localUxTest.value.variant,
                                options = options,
                                optionTransform = { +it },
                                onSelectionChange = {
                                    localUxTest.value = localUxTest.value.copy(variant = it)
                                }
                            )
                            Button(
                                onClick = {
                                    localUxTest.value = localUxTest.value.copy(step = localUxTest.value.step.inc().rem(UXTest.variant2.size.inc()))
                                }
                            ) {
                                Text("Skip Step")
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
