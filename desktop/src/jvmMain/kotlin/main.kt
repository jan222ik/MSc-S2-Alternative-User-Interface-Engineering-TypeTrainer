import androidx.compose.desktop.Window
import androidx.compose.foundation.layout.ExperimentalLayout
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.ui.graphics.Color
import ui.dashboard.ApplicationRoutes
import ui.dashboard.content.DashboardContent
import ui.general.WindowContainer
import ui.general.WindowRouter
import ui.util.i18n.LanguageConfiguration

@OptIn(ExperimentalLayout::class)
fun main() {
    val dark = darkColors(
        background = Color(0xFF303747),
        surface = Color(0xFF31445F),
        primary = Color(0xFF839AD3),
        onPrimary = Color.White
    )
    Window {
        MaterialTheme(colors = dark) {
            LanguageConfiguration {
                WindowRouter(
                    initialRoute = ApplicationRoutes.Dashboard
                ) { current, router ->
                    WindowContainer(
                        enableBackBtn = router.hasBackDestination(),
                        backHoverBtnText = router.previous?.current?.title?.observedString(router)?.let { "To $it" }
                            ?: "",
                        onBackBtnAction = router::back,
                        title = router.current.title.observedString(router)
                    ) {
                        when (current) {
                            ApplicationRoutes.Dashboard -> {
                                DashboardContent()
                            }
                            ApplicationRoutes.Settings -> Text(+current.title)
                            ApplicationRoutes.Pictures -> Text(+current.title)
                            ApplicationRoutes.Compose -> Text(+current.title)
                            ApplicationRoutes.Logbook -> Text(+current.title)
                            ApplicationRoutes.ExerciseSelection -> Text(+current.title)
                        }
                    }
                }
            }
        }
    }
}




