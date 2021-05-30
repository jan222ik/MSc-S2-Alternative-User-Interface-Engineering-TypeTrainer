@file:Suppress("FunctionName")

package ui.general.window.container


import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import ui.general.window.container.impl.WCHeader

@Composable
fun WindowContainer(
    background: Color = MaterialTheme.colors.background,
    surface: Color = MaterialTheme.colors.surface,
    topBarHeight: Dp = 35.dp,
    title: String,
    container: @Composable () -> Unit
) {
    // Update the title of the Windows Window
    val appWindow = LocalAppWindow.current
    remember(title) { appWindow.setTitle(title) }
    // Create Container
    Surface(color = background) {
        Column(modifier = Modifier.fillMaxSize()) {
            WCHeader(
                height = topBarHeight,
                surface = surface,
                background = background,
                title = title
            )
            //Container for Content
            Card(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(topEnd = 35.dp),
                backgroundColor = surface,
                elevation = 0.dp
            ) {
                container.invoke()
            }
        }
    }
}

