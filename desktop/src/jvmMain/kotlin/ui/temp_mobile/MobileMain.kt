@file:Suppress("FunctionName")

package ui.temp_mobile

import TypeTrainerTheme
import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntSize
import ui.temp_mobile.router.MobileRouter
import ui.temp_mobile.router.MobileRouterAmbient
import ui.temp_mobile.router.MobileRoutes
import ui.temp_mobile.screens.menu.MobileMenu

fun main() {
    val base = 40
    Window(size = IntSize(width = base * 9, height = base * 19)) {
        TypeTrainerTheme {
            Surface(color = MaterialTheme.colors.background) {
                Box(modifier = Modifier.fillMaxSize()) {
                    val initialRoute: MobileRoutes = MobileRoutes.Menu
                    MobileRouter(
                        initialRoute = initialRoute
                    ) { current, _ ->
                        when (current) {
                            MobileRoutes.Menu -> MobileMenu()
                            MobileRoutes.Scanner -> CurrentlyMissing("Scanner")
                            is MobileRoutes.Exercise -> CurrentlyMissing("Exercise active")
                            MobileRoutes.CameraSetup -> CurrentlyMissing("Camera Setup")
                            MobileRoutes.AppBenefits -> CurrentlyMissing("App Benefits")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CurrentlyMissing(
    title: String
) {
    val router = MobileRouterAmbient.current
    Column {
        Text(text = "Screen $title is not implemented.")
        Button(
            onClick = router::back
        ) {
            Text(text = "Back")
        }
    }
}

