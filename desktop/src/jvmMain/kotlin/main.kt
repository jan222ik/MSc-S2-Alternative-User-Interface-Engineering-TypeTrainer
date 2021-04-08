@file:Suppress("FunctionName")

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeysSet
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.util.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import network.Server
import network.ServerApplication
import textgen.database.DatabaseFactory
import ui.components.AnimatedLogo
import ui.dashboard.ApplicationRoutes
import ui.dashboard.content.DashboardContent
import ui.exercise.practice.PracticeScreen
import ui.exercise.results.ResultsScreen
import ui.exercise.selection.ExerciseSelection
import ui.exercise.selection.ExerciseSelectionIntent
import ui.general.WindowRouter
import ui.general.window.container.WindowContainer
import ui.history.HistoryScreen
import ui.util.debug.DebugWithAllRoutes
import ui.util.debug.ifDebug
import ui.util.i18n.LanguageConfiguration

@ExperimentalCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalStdlibApi
@KtorExperimentalAPI
fun main() {
    System.setProperty("debug", "true")
    ifDebug {
        println("Debug Pre Checks")
        println("Check for translation keys:")
        ui.util.i18n.main()
        println("-".repeat(80))
    }
    Window(size = IntSize(width = 1280, height = 720)) {
        TypeTrainerTheme {
            StartupApplication { server ->
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
                                ApplicationRoutes.Debug -> DebugWithAllRoutes()
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
                                is ApplicationRoutes.Exercise.ExerciseResults -> ResultsScreen(
                                    current.exerciseResults,
                                    current.initialPage
                                )
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
}

@OptIn(ExperimentalAnimationApi::class)
@ExperimentalCoroutinesApi
@KtorExperimentalAPI
@Composable
fun StartupApplication(afterStartUp: @Composable (server: Server) -> Unit) {
    val server = remember { Server() }
    val startUpScope = rememberCoroutineScope()
    val loadingStateFlow = remember { MutableStateFlow<StartUpLoading>(StartUpLoading.START)}
    DisposableEffect(server) {
        var engine: NettyApplicationEngine? = null
        startUpScope.launch(Dispatchers.IO) {
            loadingStateFlow.emit(StartUpLoading.DATABASE)
            DatabaseFactory.initWithDemoData()
            loadingStateFlow.emit(StartUpLoading.NETWORK)
            engine = embeddedServer(Netty, port = 8080) {
                ServerApplication(server).apply { create() }
            }.start(wait = false)
            loadingStateFlow.emit(StartUpLoading.DONE)
        }
        onDispose {
            engine?.stop(0, 0)
            DatabaseFactory.stop()
        }
    }
    val loading = loadingStateFlow.collectAsState()
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        if (loading.value != StartUpLoading.DONE) {
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 80.dp)) {
                AnimatedLogo(modifier = Modifier.align(Alignment.Center))
            }
        }
        when (loading.value) {
            StartUpLoading.START -> Text("Typetrainer is starting.")
            StartUpLoading.NETWORK -> Text("Starting the servers.")
            StartUpLoading.DATABASE -> Text("Spinning up database.")
            StartUpLoading.DONE -> afterStartUp.invoke(server)
        }
    }
}

enum class StartUpLoading {
    START, NETWORK, DATABASE, DONE
}



