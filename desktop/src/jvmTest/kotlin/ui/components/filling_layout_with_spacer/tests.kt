@file:Suppress("FunctionName")

package ui.components.filling_layout_with_spacer

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.components.TypeTrainerTheme

fun main() {
    `Width - Test`()
    `Height - Test`()
}

private fun `Height - Test`() {
    Window(size = IntSize(600, 800)) {
        TypeTrainerTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                val mod = Modifier.fillMaxSize()
                CustomLayoutHeightWithCenterSpace(
                    centralPadding = 16.dp,
                    top = {
                        Card(mod) { Text("Top") }
                    },
                    bottom = {
                        Card(mod) { Text("Bottom") }
                    }
                )
            }
        }
    }
}

private fun `Width - Test`() {
    Window(size = IntSize(600, 800)) {
        TypeTrainerTheme {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                val mod = Modifier.fillMaxSize()
                CustomLayoutWidthWithCenterSpace(
                    centralPadding = 16.dp,
                    start = {
                        Card(mod) { Text("Start") }

                    },
                    end = {
                        Card(mod) { Text("End") }
                    })
            }
        }
    }
}
