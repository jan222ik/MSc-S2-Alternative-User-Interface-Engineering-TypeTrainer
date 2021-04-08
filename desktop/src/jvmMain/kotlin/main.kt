@file:Suppress("FunctionName")

import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeysSet
import androidx.compose.ui.unit.IntSize
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import textgen.database.DatabaseFactory
import textgen.error.ExerciseEvaluation
import textgen.generators.impl.RandomKnownWordGenerator
import ui.dashboard.ApplicationRoutes
import ui.dashboard.content.DashboardContent
import ui.exercise.ExerciseMode
import ui.exercise.TypingOptions
import ui.exercise.practice.PracticeScreen
import ui.exercise.results.ResultsScreen
import ui.exercise.selection.ExerciseSelection
import ui.exercise.selection.ExerciseSelectionIntent
import ui.general.WindowRouter
import ui.general.WindowRouterAmbient
import ui.general.window.container.WindowContainer
import ui.history.HistoryScreen
import ui.util.debug.ifDebug
import ui.util.i18n.LanguageConfiguration
import ui.util.i18n.LanguageDefinition
import kotlin.reflect.KClass

@ExperimentalStdlibApi
fun main() {
    System.setProperty("debug", "true")
    ifDebug {
        println("Debug Pre Checks")
        println("Check for translation keys:")
        ui.util.i18n.main()
        println("-".repeat(80))
    }
    DatabaseFactory.initWithDemoData()
    Window(size = IntSize(width = 1280, height = 720)) {
        TypeTrainerTheme {
            LanguageConfiguration {
                WindowRouter(
                    initialRoute = ApplicationRoutes.Dashboard
                ) { current, router ->
                    val window = LocalAppWindow.current
                    LaunchedEffect(window, current) {
                        window.apply {
                            keyboard.setShortcut(Key.CtrlRight) {
                                router.navTo(ApplicationRoutes.Debug)
                            }
                            if (current is ApplicationRoutes.Exercise.Training) {
                                keyboard.removeShortcut(KeysSet(Key.CtrlLeft))
                            } else {
                                keyboard.setShortcut(Key.CtrlLeft, router::back)
                            }
                        }
                    }
                    WindowContainer(
                        title = router.current.title.observedString(router)
                    ) {
                        when (current) {
                            ApplicationRoutes.Debug -> AllRoutes()
                            ApplicationRoutes.Dashboard -> DashboardContent()
                            ApplicationRoutes.Settings -> Text("Missing Screen: " + +current.title)
                            ApplicationRoutes.User.Login -> Text("Missing Screen: " + +current.title)
                            is ApplicationRoutes.User.AccountManagement -> Text("Missing Screen: " + +current.title)
                            ApplicationRoutes.Exercise.ExerciseSelection -> ExerciseSelection(
                                ExerciseSelectionIntent()
                            )
                            ApplicationRoutes.Exercise.Connection.QRCode -> Text("Missing Screen: " + +current.title)
                            ApplicationRoutes.Exercise.Connection.SetupInstructions -> Text("Missing Screen: " + +current.title)
                            is ApplicationRoutes.Exercise.Training -> PracticeScreen(current.trainingOptions)
                            is ApplicationRoutes.Exercise.ExerciseResults -> ResultsScreen(current.exerciseResults, current.initialPage)
                            ApplicationRoutes.Goals.Overview -> Text("Missing Screen: " + +current.title)
                            ApplicationRoutes.Goals.Compose -> Text("Missing Screen: " + +current.title)
                            ApplicationRoutes.Achievements -> Text("Missing Screen: " + +current.title)
                            ApplicationRoutes.Competitions.Overview -> Text("Missing Screen: " + +current.title)
                            ApplicationRoutes.History -> HistoryScreen()
                            ApplicationRoutes.AppBenefits -> Text("Missing Screen: " + +current.title)
                        }
                    }
                }
            }
        }
    }
}

private fun <T : Any> getDebugPaths(kClass: KClass<T>): List<KClass<out T>> {
    return when {
        kClass.isSealed -> kClass.sealedSubclasses.map { getDebugPaths(it) }.flatten()
        kClass.isData || kClass.objectInstance != null -> {
            return listOf(kClass)
        }
        else -> emptyList()
    }

}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun AllRoutes() {
    val router = WindowRouterAmbient.current
    LazyColumn {
        stickyHeader {
            Surface {
                Text("Routes:")
            }
        }
        items(getDebugPaths(ApplicationRoutes::class)) {
            when (it.objectInstance) {
                ApplicationRoutes.Dashboard,
                ApplicationRoutes.Settings,
                ApplicationRoutes.User.Login,
                ApplicationRoutes.Exercise.ExerciseSelection,
                ApplicationRoutes.Exercise.Connection.QRCode,
                ApplicationRoutes.Exercise.Connection.SetupInstructions,
                ApplicationRoutes.Goals.Overview,
                ApplicationRoutes.Goals.Compose,
                ApplicationRoutes.Achievements,
                ApplicationRoutes.Competitions.Overview,
                ApplicationRoutes.Debug,
                ApplicationRoutes.History -> {
                    val objectInstance = it.objectInstance!!
                    Button(onClick = { router.navTo(objectInstance) }) {
                        Text(text = +objectInstance.title)
                    }
                }
                null -> when (it) {
                    ApplicationRoutes.User.AccountManagement::class -> {
                        val dest = ApplicationRoutes.User.AccountManagement(Any())
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.Exercise.Training::class -> {
                        val dest = ApplicationRoutes.Exercise.Training(
                            TypingOptions(
                                generatorOptions = RandomKnownWordGenerator.RandomKnownWordOptions(
                                    seed = 1L,
                                    minimalSegmentLength = 300,
                                    language = LanguageDefinition.German
                                ),
                                durationMillis = 1 * 60_000,
                                type = ExerciseMode.Speed,
                                isCameraEnabled = true
                            )
                        )
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    ApplicationRoutes.Exercise.ExerciseResults::class -> {
                        val dest = ApplicationRoutes.Exercise.ExerciseResults(ExerciseEvaluation())
                        Button(onClick = { router.navTo(dest) }) {
                            Text(text = +dest.title)
                        }
                    }
                    else -> {
                        Text(text = "Missing path in debug for class $it")
                    }
                }
            }
        }
    }
}

