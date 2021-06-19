@file:Suppress("FunctionName")

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeysSet
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.network.ServerConfig
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import network.Server
import network.ServerApplication
import textgen.database.DatabaseFactory
import ui.camera.CameraSetupScreen
import ui.components.AnimatedLogo
import ui.connection.ConnectionScreen
import ui.dashboard.ApplicationRoutes
import ui.dashboard.content.DashboardContent
import ui.exercise.connection.KeyboardSynchronisationScreen
import ui.exercise.practice.PracticeScreen
import ui.exercise.results.ResultsScreen
import ui.exercise.selection.ExerciseSelection
import ui.exercise.selection.ExerciseSelectionIntent
import ui.general.WindowRouter
import ui.general.window.container.WindowContainer
import ui.goals.GoalComposeScreen
import ui.history.HistoryScreen
import ui.util.debug.Debug
import ui.util.debug.DebugWithAllRoutes
import ui.util.i18n.LanguageConfiguration
import util.FingerMatcher

@ExperimentalAnimationApi
object DesktopApplication {

    @ExperimentalCoroutinesApi
    @ExperimentalFoundationApi
    @ExperimentalStdlibApi
    @KtorExperimentalAPI
    fun start() {
        val initSize = IntSize(width = 1920, height = 1080)
        Window(size = initSize, undecorated = !Debug.isDebug) {
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
                                Crossfade(current) { current ->
                                    when (current) {
                                        ApplicationRoutes.Debug -> DebugWithAllRoutes()
                                        ApplicationRoutes.Dashboard -> DashboardContent()
                                        ApplicationRoutes.Settings -> Text("Missing Screen: " + +current.title)
                                        ApplicationRoutes.User.Login -> Text("Missing Screen: " + +current.title)
                                        is ApplicationRoutes.User.AccountManagement -> Text("Missing Screen: " + +current.title)
                                        is ApplicationRoutes.Exercise.ExerciseSelection -> ExerciseSelection(
                                            ExerciseSelectionIntent(current.initData)
                                        )
                                        is ApplicationRoutes.Exercise.Connection.SetupConnection ->
                                            ConnectionScreen(
                                                server = server,
                                                trainingOptions = current.trainingOptions
                                            )
                                        is ApplicationRoutes.Exercise.Connection.SetupInstructions -> CameraSetupScreen()
                                        is ApplicationRoutes.Exercise.Connection.KeyboardSynchronisation -> KeyboardSynchronisationScreen(
                                            trainingOptions = current.trainingOptions,
                                            server = server
                                        )
                                        is ApplicationRoutes.Exercise.Training -> {
                                            PracticeScreen(
                                                typingOptions = current.trainingOptions,
                                                fingerMatcher = current.fingerMatcher
                                            )
                                            if (current.trainingOptions.isCameraEnabled) {
                                                FingerCanvas(server = server, fingerMatcher = current.fingerMatcher)
                                            }
                                        }
                                        is ApplicationRoutes.Exercise.ExerciseResults -> ResultsScreen(
                                            current.exerciseResults,
                                            current.initialPage
                                        )
                                        ApplicationRoutes.Goals.Overview -> Text("Missing Screen: " + +current.title)
                                        ApplicationRoutes.Goals.Compose -> GoalComposeScreen()
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
    }


    @ExperimentalAnimationApi
    @ExperimentalCoroutinesApi
    @KtorExperimentalAPI
    @Composable
    fun StartupApplication(afterStartUp: @Composable (server: Server) -> Unit) {
        val server = remember { Server() }
        val startUpScope = rememberCoroutineScope()
        val loadingStateFlow = remember { MutableStateFlow<StartUpLoading>(StartUpLoading.START) }
        DisposableEffect(server) {
            var engine: NettyApplicationEngine? = null
            startUpScope.launch(Dispatchers.IO) {
                loadingStateFlow.emit(StartUpLoading.DATABASE)
                //DatabaseFactory.init()
                //DEMO.demoTrainingEntries()
                DatabaseFactory.start()
                loadingStateFlow.emit(StartUpLoading.NETWORK)
                engine = embeddedServer(Netty, port = ServerConfig.PORT) {
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
        val animOnce = remember { mutableStateOf(false) }
        val window = LocalAppWindow.current
        DisposableEffect(window, animOnce.value) {
            val keySet = KeysSet(Key.E)
            val keyboard = window.keyboard
            if (animOnce.value) {
                keyboard.removeShortcut(keySet)
            } else {
                keyboard.setShortcut(keySet) {
                    animOnce.component2()(true)
                    println("Skip Animation must run once to end!")
                }
            }
            onDispose {
                keyboard.removeShortcut(keySet)
            }
        }
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            Box(
                modifier = Modifier.scale(1f),
                contentAlignment = Alignment.TopStart
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    if (!animOnce.component1() || loading.value != StartUpLoading.DONE) {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 80.dp)) {
                            AnimatedLogo(
                                modifier = Modifier.align(Alignment.Center),
                                onAnimatedOnce = animOnce.component2()
                            )
                        }
                    }
                    when (loading.value) {
                        StartUpLoading.START -> Text("Typetrainer is starting.")
                        StartUpLoading.NETWORK -> Text("Starting the servers.")
                        StartUpLoading.DATABASE -> Text("Spinning up database.")
                        StartUpLoading.DONE -> {
                            if (animOnce.value) {
                                afterStartUp.invoke(server)
                            }
                        }
                    }
                }
            }
        }
    }

    enum class StartUpLoading {
        START, NETWORK, DATABASE, DONE
    }

    @Composable
    fun FingerCanvas(server: Server, fingerMatcher: FingerMatcher?) {
        fingerMatcher ?: return
        LaunchedEffect(server, fingerMatcher) {
            Window {
                TypeTrainerTheme {
                    Surface(color = MaterialTheme.colors.surface) {
                        val hands = server.handLandMarks.receiveAsFlow().collectAsState(listOf())
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            clipRect {
                                val (w, h) = this.size
                                println("w: $w h: $h")
                                val colors = listOf(
                                    Color.Red,
                                    Color.Green,
                                    Color.Cyan
                                )
                                hands.value.forEachIndexed { idx, it ->
                                    println("Hand $idx")
                                    val c = colors[idx.rem(colors.size)]
                                    it.fingerLandmarks.values.map {
                                        val point = it.toKeyBoardRef( Offset.Zero)
                                        val x = point.x * w
                                        val y = point.y * h
                                        println("Finger ${it.finger} x: $x y: $y")
                                        Offset(x, y)
                                    }.let {
                                        drawPoints(points = it, brush = SolidColor(c), pointMode = PointMode.Points, strokeWidth = 4f, cap = StrokeCap.Round)
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}





