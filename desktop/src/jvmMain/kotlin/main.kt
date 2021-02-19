@file:Suppress("FunctionName")

import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.IntSize
import ui.dashboard.ApplicationRoutes
import ui.dashboard.content.DashboardContent
import ui.exercise.selection.ExerciseSelection
import ui.exercise.selection.ExerciseSelectionIntent
import ui.general.window.container.WindowContainer
import ui.general.WindowRouter
import ui.util.i18n.LanguageConfiguration

@OptIn(ExperimentalLayout::class)
fun main() {
    Window(size = IntSize(width = 1280, height = 720)) {
        TypeTrainerTheme {
            LanguageConfiguration {
                WindowRouter(
                    initialRoute = ApplicationRoutes.Dashboard
                ) { current, router ->
                    WindowContainer(
                        title = router.current.title.observedString(router)
                    ) {
                        when (current) {
                            ApplicationRoutes.Dashboard -> DashboardContent()
                            ApplicationRoutes.Settings -> Text(+current.title)
                            ApplicationRoutes.Pictures -> Text(+current.title)
                            ApplicationRoutes.Compose -> Text(+current.title)
                            ApplicationRoutes.Logbook -> Text(+current.title)
                            ApplicationRoutes.ExerciseSelection -> ExerciseSelection(ExerciseSelectionIntent())
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun TypeTrainerTheme(content: @Composable () -> Unit) {
    val dark = darkColors(
        background = Color(0xFF303747),
        surface = Color(0xFF31445F),
        primary = Color(0xFF839AD3),
        onPrimary = Color.White
    )

    MaterialTheme(colors = dark) {
        content.invoke()
    }
}
