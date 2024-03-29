@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.general.window.container.impl


import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
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
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.desktop.ui.util.i18n.LocalTranslationI18N

@HasDoc
@Composable
internal fun UserPopup(
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
                        Column(modifier = Modifier.padding(8.dp)) {
                            Box(modifier = Modifier.width(400.dp)) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text = +LocalTranslationI18N(
                                        eng = "User Management",
                                        ger = "Benutzenden Verwaltung"
                                    ),
                                    maxLines = 1
                                )
                                Icon(
                                    modifier = Modifier
                                        .align(Alignment.CenterEnd)
                                        .clickable(onClick = onDismissRequest),
                                    imageVector = Icons.Filled.Close,
                                    contentDescription = "Close settings balloon"
                                )
                            }
                            Text(
                                text = +LocalTranslationI18N(
                                    eng = "User Management is not implemented yet!",
                                    ger = "Benutzenden Verwaltung ist noch nicht implementiert!"
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}
