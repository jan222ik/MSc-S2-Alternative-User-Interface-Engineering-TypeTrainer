@file:Suppress("FunctionName")

package ui.general.window.container.impl

import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.general.WindowRouterAmbient

@ExperimentalLayout
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
        Box(heightMod.fillMaxWidth()) {
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

@ExperimentalLayout
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
        verticalAlignment = Alignment.CenterVertically
    ) {
        val (showSettings, setShowSettings) = remember { mutableStateOf(false) }
        val onDismissRequest = { setShowSettings(false) }
        if (showSettings) {
            QuickSettingsPopup(
                height = height,
                windowControlsGroupPadding = windowControlsGroupPadding,
                onDismissRequest = onDismissRequest
            )
        }
        SettingsBtn(onAction = { setShowSettings(showSettings.not()) })
        MinimizeBtn(onAction = appWindow::minimize)
        MaximizeBtn(onAction = { if (appWindow.isMaximized) appWindow.restore() else appWindow.maximize() })
        CloseBtn(onAction = appWindow::close)
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
