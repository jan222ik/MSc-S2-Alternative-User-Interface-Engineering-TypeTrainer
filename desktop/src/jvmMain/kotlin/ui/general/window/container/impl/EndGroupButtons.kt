@file:Suppress("FunctionName")

package ui.general.window.container.impl

import androidx.compose.foundation.clickable
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Maximize
import androidx.compose.material.icons.filled.Minimize
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.jan222ik.common.HasDoc

@HasDoc
@Composable
internal fun SettingsBtn(onAction: () -> Unit) {
    Icon(
        modifier = Modifier.clickable(onClick = onAction),
        imageVector = Icons.Filled.Settings,
        contentDescription = "Open Settings"
    )
}

@Composable
internal fun MinimizeBtn(
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
internal fun MaximizeBtn(
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
internal fun CloseBtn(
    onAction: () -> Unit
) {
    //val (onHover, setOnHover) = remember { mutableStateOf(false) }
    Icon(
        modifier = Modifier.clickable(onClick = onAction),
        imageVector = Icons.Filled.Close,
        contentDescription = "Close window"
    )
}
