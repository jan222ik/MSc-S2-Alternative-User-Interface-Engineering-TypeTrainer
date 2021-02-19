@file:Suppress("FunctionName")

package ui.general


import TypeTrainerTheme
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.preferredWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerMoveFilter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import ui.dashboard.ApplicationRoutes
import ui.exercise.selection.OutlinedRadioSelection
import ui.util.i18n.LanguageAmbient
import ui.util.i18n.LanguageDefinition
import ui.util.i18n.i18n

@ExperimentalLayout
@Composable
fun WindowContainer(
    background: Color = MaterialTheme.colors.background,
    surface: Color = MaterialTheme.colors.surface,
    title: String,
    enableBackBtn: Boolean,
    backHoverBtnText: String,
    onBackBtnAction: () -> Unit,
    container: @Composable () -> Unit
) {
    Surface(color = background) {
        Column(modifier = Modifier.fillMaxSize()) {
            val height = 35.dp
            val heightMod = Modifier.height(height)
            Row(
                modifier = heightMod.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BackBtn(
                    enableBackBtn = enableBackBtn,
                    onBackBtnAction = onBackBtnAction,
                    backHoverBtnText = backHoverBtnText,
                    height = height,
                    surface = surface
                )
                Box(heightMod.fillMaxWidth()) {
                    Box(
                        modifier = heightMod.width(50.dp),
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
                    Row(
                        modifier = heightMod,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(16.dp))
                        Text(text = title, color = MaterialTheme.colors.primary)
                    }
                    val windowControlsGroupPadding = 8.dp
                    Row(
                        modifier = heightMod.align(Alignment.CenterEnd)
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
                        val current = LocalAppWindow.current
                        MinimizeBtn(onAction = current::minimize)
                        MaximizeBtn(onAction = { if (current.isMaximized) current.restore() else current.maximize() })
                        CloseBtn(onAction = current::close)
                    }
                }
            }
            Card(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(),
                shape = RoundedCornerShape(topEnd = 35.dp),
                backgroundColor = surface,
                elevation = 0.dp
            ) {
                container.invoke()
            }
        }
    }
}

@Composable
@ExperimentalLayout
private fun QuickSettingsPopup(
    height: Dp,
    windowControlsGroupPadding: Dp,
    onDismissRequest: () -> Unit
) {
    val yPx = with(LocalDensity.current) { height.toPx() }
    val xPx = with(LocalDensity.current) { windowControlsGroupPadding.toPx() }
    Popup(
        alignment = Alignment.TopEnd,
        offset = IntOffset(xPx.toInt(), yPx.toInt()),
        onDismissRequest = onDismissRequest
    ) {
        TypeTrainerTheme {
            Card(
                modifier = Modifier.preferredWidth(IntrinsicSize.Min),
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
                    PopupSettingsBallon(
                        actionClosePopup = onDismissRequest,
                        actionOpenSettingsPage = {
                            router.navTo(ApplicationRoutes.Settings)
                            onDismissRequest.invoke()
                        }
                    )
                }
            }
        }
    }
}


@ExperimentalLayout
@Composable
private fun PopupSettingsBallon(actionClosePopup: () -> Unit, actionOpenSettingsPage: () -> Unit) {
    Column(modifier = Modifier.padding(8.dp).preferredWidth(IntrinsicSize.Min)) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Text(modifier = Modifier.align(Alignment.CenterStart), text = +i18n.str.settings.quickies.title)
            Icon(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .clickable(onClick = actionClosePopup),
                imageVector = Icons.Filled.Close,
                contentDescription = "Close settings ballon"
            )
        }
        Row {
            val current = LanguageAmbient.current
            OutlinedRadioSelection(
                modifier = Modifier.preferredWidth(IntrinsicSize.Min),
                label = +i18n.str.settings.languages.language + ":",
                selected = 0.takeIf { current.language == LanguageDefinition.English } ?: 1,
                options = listOf(i18n.str.settings.languages.eng, i18n.str.settings.languages.ger),
                onSelectionChanged = {
                    current.changeLanguage(LanguageDefinition.English.takeIf { current.language == LanguageDefinition.German }
                        ?: LanguageDefinition.German)
                }
            )
        }
        OutlinedButton(
            onClick = actionOpenSettingsPage
        ) {
            Text(+i18n.str.settings.quickies.action_open_settings)
        }
    }
}

@Composable
private fun SettingsBtn(onAction: () -> Unit) {
    Icon(
        modifier = Modifier.clickable(onClick = onAction),
        imageVector = Icons.Filled.Settings,
        contentDescription = "Open Settings"
    )
}

@Composable
private fun MinimizeBtn(
    onAction: () -> Unit
) {
    //val (onHover, setOnHover) = remember { mutableStateOf(false) }
    Icon(
        modifier = Modifier.clickable(onClick = onAction),
        imageVector = Icons.Filled.Minimize,
        contentDescription = "Minimize window"
    )
}

@Composable
private fun MaximizeBtn(
    onAction: () -> Unit
) {
    //val (onHover, setOnHover) = remember { mutableStateOf(false) }
    Icon(
        modifier = Modifier.clickable(onClick = onAction),
        imageVector = Icons.Filled.Maximize,
        contentDescription = "Maximize window"
    )
}

@Composable
private fun CloseBtn(
    onAction: () -> Unit
) {
    //val (onHover, setOnHover) = remember { mutableStateOf(false) }
    Icon(
        modifier = Modifier.clickable(onClick = onAction),
        imageVector = Icons.Filled.Close,
        contentDescription = "Close window"
    )
}

@Composable
private fun BackBtn(
    height: Dp,
    enableBackBtn: Boolean,
    onBackBtnAction: () -> Unit,
    backHoverBtnText: String,
    surface: Color,
) {
    val (onHover, setOnHover) = remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .height(height)
            .pointerMoveFilter(
                onEnter = { setOnHover(true); true },
                onExit = { setOnHover(false); true }
            )
            .clickable(onClick = onBackBtnAction),
        shape = RoundedCornerShape(topEnd = 25.dp),
        backgroundColor = surface,
        elevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (enableBackBtn) {
                Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = null)
                if (onHover) {
                    Text(text = backHoverBtnText)
                }
            } else {
                Icon(imageVector = Icons.Filled.Dashboard, contentDescription = null)
            }
        }
    }
}
