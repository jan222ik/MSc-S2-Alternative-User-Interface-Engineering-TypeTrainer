@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.general.window.container.impl

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Adb
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.debug.ifDebugCompose

@HasDoc
@Composable
internal fun WCHeader(
    height: Dp,
    surface: Color,
    background: Color,
    title: String,
) {
    val router = WindowRouterAmbient.current
    val appWindow = LocalAppWindow.current

    val heightMod = Modifier.height(height)
    Row(
        modifier = heightMod.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        BackBtn(
            enableBackBtn = router.hasBackDestination(),
            onBackBtnAction = router::back,
            backHoverBtnText = router.previous?.current?.title?.observedString(router)?.let { "To $it" } ?: "",
            height = height,
            surface = surface
        )
        Box(heightMod
            .fillMaxWidth()
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmoun2t ->
                    val dragAmount = change.positionChange()
                    appWindow.setLocation(
                        x = (appWindow.x + dragAmount.x).toInt(),
                        y = (appWindow.y + dragAmount.y).toInt()
                    )
                    change.consumeAllChanges()
                }
            }
        ) {
            FallingWave(
                surface = surface,
                background = background,
                height = height
            )
            CurrentTitle(
                modifier = heightMod,
                title = title
            )
            val windowControlsGroupPadding = 8.dp
            EndGroup(
                modifier = heightMod,
                windowControlsGroupPadding = windowControlsGroupPadding,
                height = height,
                appWindow = appWindow
            )
        }
    }
}


@Composable
private fun BoxScope.EndGroup(
    modifier: Modifier,
    windowControlsGroupPadding: Dp,
    height: Dp,
    appWindow: AppWindow
) {
    Row(
        modifier = modifier
            .align(Alignment.CenterEnd)
            .padding(horizontal = windowControlsGroupPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        val (showSettings, setShowSettings) = remember { mutableStateOf(false) }
        val (showUser, setShowUser) = remember { mutableStateOf(false) }
        val onDismissRequestSettings = { setShowSettings(false) }
        val onDismissRequestUser = { setShowUser(false) }
        if (showSettings) {
            QuickSettingsPopup(
                height = height,
                windowControlsGroupPadding = windowControlsGroupPadding,
                onDismissRequest = onDismissRequestSettings
            )
        }
        if (showUser) {
            UserPopup(
                height = height,
                windowControlsGroupPadding = windowControlsGroupPadding,
                onDismissRequest = onDismissRequestUser
            )
        }
        ifDebugCompose {
            val router = WindowRouterAmbient.current
            Icon(
                modifier = Modifier.clickable {
                    router.navTo(ApplicationRoutes.Debug)
                },
                imageVector = Icons.Default.Adb,
                contentDescription = null,
                tint = Color.Gray
            )
        }
        CurrentUser(onAction = { setShowUser(true) })
        SettingsBtn(onAction = { setShowSettings(showSettings.not()) })
        MinimizeBtn(onAction = appWindow::minimize)
        MaximizeBtn(onAction = { if (appWindow.isMaximized) appWindow.restore() else appWindow.maximize() })
        CloseBtn(onAction = appWindow::close)
    }
}

@Composable
private fun CurrentUser(onAction: () -> Unit) {
    TextButton(
        onClick = onAction
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(imageVector = Icons.Filled.Person, contentDescription = null)
            val user = System.getProperty("user") ?: "Unknown User"
            Text(text = user)
        }
    }
}

@Composable
private fun CurrentTitle(modifier: Modifier, title: String) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Spacer(Modifier.width(16.dp))
        Text(text = title, color = MaterialTheme.colors.primary)
    }
}

@Composable
private fun FallingWave(
    surface: Color,
    background: Color,
    height: Dp
) {
    Box(
        modifier = Modifier.height(height).width(50.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        listOf(
            RoundedCornerShape(0.dp) to surface,
            RoundedCornerShape(bottomStart = 35.dp) to background
        ).forEach {
            val (shape, color) = it
            Card(
                modifier = Modifier.height(height / 2).width(50.dp),
                shape = shape,
                backgroundColor = color,
                elevation = 0.dp
            ) {}
        }
    }
}
