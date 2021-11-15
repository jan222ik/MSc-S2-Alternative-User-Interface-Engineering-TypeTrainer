@file:Suppress("FunctionName")

package com.github.jan222ik.desktop

import androidx.compose.animation.Crossfade
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.desktop.WindowEvents
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeysSet
import androidx.compose.ui.platform.LocalDensity
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
import com.github.jan222ik.desktop.network.Server
import com.github.jan222ik.desktop.network.ServerApplication
import com.github.jan222ik.desktop.textgen.database.DatabaseFactory
import com.github.jan222ik.desktop.textgen.database.schema.UserSettings
import com.github.jan222ik.desktop.ui.camera.CameraSetupScreen
import com.github.jan222ik.desktop.ui.components.AnimatedLogo
import com.github.jan222ik.desktop.ui.connection.ConnectionScreen
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.dashboard.content.DashboardContent
import com.github.jan222ik.desktop.ui.exercise.connection.KeyboardSynchronisationScreen
import com.github.jan222ik.desktop.ui.exercise.practice.PracticeScreen
import com.github.jan222ik.desktop.ui.exercise.results.ResultsScreen
import com.github.jan222ik.desktop.ui.exercise.selection.ExerciseSelection
import com.github.jan222ik.desktop.ui.exercise.selection.ExerciseSelectionIntent
import com.github.jan222ik.desktop.ui.general.WindowRouter
import com.github.jan222ik.desktop.ui.general.window.container.WindowContainer
import com.github.jan222ik.desktop.ui.general.window.container.impl.CloseBtn
import com.github.jan222ik.desktop.ui.goals.GoalComposeScreen
import com.github.jan222ik.desktop.ui.history.HistoryScreen
import com.github.jan222ik.desktop.ui.util.debug.Debug
import com.github.jan222ik.desktop.ui.util.debug.DebugWithAllRoutes
import com.github.jan222ik.desktop.ui.util.i18n.LanguageConfiguration
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import com.github.jan222ik.desktop.util.FingerMatcher
import com.github.jan222ik.desktop.util.KeyboardUtil
import com.github.jan222ik.desktop.util.RelativeKey
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import javax.imageio.ImageIO

@ExperimentalAnimationApi
object DesktopApplication {

    @ExperimentalCoroutinesApi
    @ExperimentalFoundationApi
    @ExperimentalStdlibApi
    @KtorExperimentalAPI
    fun start(initIsUxTest: Boolean = true) {
        val initSize = IntSize(width = 1920, height = 1080)
        val icon = try {
            ImageIO.read(this::class.java.getResourceAsStream("/logoRounded.png"))
        } catch (_: Throwable) {
            null
        }
        println("icon = ${icon}")
        val windowEvents = WindowEvents(
            onClose = {
                // Save
                println("On Close")
            }
        )
        Window(
            size = initSize,
            undecorated = !Debug.isDebug,
            icon = icon,
            events = windowEvents
        ) {
            val isUxTest = mutableStateOf(initIsUxTest)
            CompositionLocalProvider(
                UXTest.LocalIsUXTest provides isUxTest,
                UXTest.LocalUXTestRun provides mutableStateOf(UXTest.TestRun(variant = 0, step = 0, testDirPath = System.getProperty("user.home").plus("\\uxtest")))
            ) {
                TypeTrainerTheme {
                    StartupApplication { server, lang ->
                        LanguageConfiguration(lang) {
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
                                            ApplicationRoutes.Dashboard -> if (isUxTest.value) UXTest.Screen() else DashboardContent()
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
    }


    @ExperimentalAnimationApi
    @ExperimentalCoroutinesApi
    @KtorExperimentalAPI
    @Composable
    fun StartupApplication(afterStartUp: @Composable (server: Server, lang: LanguageDefinition) -> Unit) {
        val server = remember { Server() }
        val lang = remember { mutableStateOf<LanguageDefinition>(LanguageDefinition.English) }
        val startUpScope = rememberCoroutineScope()
        val loadingStateFlow = remember { MutableStateFlow<StartUpLoading>(StartUpLoading.START) }
        val localError = remember { mutableStateOf<Throwable?>(null) }
        DisposableEffect(server) {
            var engine: NettyApplicationEngine? = null
            startUpScope.launch(Dispatchers.IO) {
                try {
                    loadingStateFlow.emit(StartUpLoading.DATABASE)
                    //DatabaseFactory.init()
                    //DEMO.demoTrainingEntries()
                    DatabaseFactory.start()
                    transaction {
                        UserSettings.select {
                            UserSettings.id eq UserSettings.CONST_ID
                        }
                            .single()
                            .let { lang.value = when (it[UserSettings.locale]) {
                                "ger" -> LanguageDefinition.German
                                else -> LanguageDefinition.English
                            } }

                    }
                    loadingStateFlow.emit(StartUpLoading.NETWORK)
                    engine = embeddedServer(Netty, port = ServerConfig.PORT) {
                        ServerApplication(server).apply { create() }
                    }.start(wait = false)
                    loadingStateFlow.emit(StartUpLoading.DONE)
                } catch (t: Throwable) {
                    localError.value = t
                    loadingStateFlow.emit(StartUpLoading.ERROR)
                }
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
                Box(
                    modifier = Modifier.align(Alignment.TopEnd)
                ) {
                    CloseBtn(window::close)
                }
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
                    val style = MaterialTheme.typography.h5
                    when (loading.value) {
                        StartUpLoading.START -> Text(text = "Typetrainer is starting.", style = style)
                        StartUpLoading.NETWORK -> Text(text = "Starting the servers.", style = style)
                        StartUpLoading.DATABASE -> Text(text = "Spinning up database.", style = style)
                        StartUpLoading.DONE -> {
                            if (animOnce.value) {
                                afterStartUp.invoke(server, lang.value)
                            }
                        }
                        StartUpLoading.ERROR -> {

                            Column {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    text = "The application could not be opened due to an error.",
                                    style = style.copy(color = MaterialTheme.colors.error)
                                )
                                val showError = remember { mutableStateOf(false) }
                                TextButton(
                                    modifier = Modifier.align(Alignment.CenterHorizontally),
                                    onClick = { showError.value = !showError.value }
                                ) {
                                    val text = if (showError.value) "Hide" else "Show"
                                    Text("$text Error")
                                }
                                if (showError.value) {
                                    Text(text = localError.value?.stackTraceToString() ?: "No stack trace available!")
                                }
                            }

                        }
                    }
                }
            }
        }
    }


    enum class StartUpLoading {
        START, NETWORK, DATABASE, DONE, ERROR
    }

    @Composable
    fun FingerCanvas(server: Server, fingerMatcher: FingerMatcher?) {
        fingerMatcher ?: return
        val keys = remember { KeyboardUtil.getRelativeKeyboard() }
        LaunchedEffect(fingerMatcher, server) {
            Window {
                TypeTrainerTheme {
                    Surface(color = MaterialTheme.colors.surface) {
                        val hands = server.handLandMarks.receiveAsFlow().collectAsState(listOf())
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            clipRect {
                                keys.forEach { row ->
                                    row.forEach {
                                        drawKey(it)
                                    }
                                }
                                val (w, h) = this.size
                                val colors = listOf(
                                    Color.Red,
                                    Color.Green,
                                    Color.Cyan
                                )
                                hands.value.forEachIndexed { idx, it ->
                                    //println("Hand $idx")
                                    val c = colors[idx.rem(colors.size)]
                                    //println("Finger ${it.finger} x: $x y: $y")
                                    it.fingerLandmarks.values.mapNotNull {
                                        val point = with(fingerMatcher) { it.toKeyBoardRef() }
                                        point?.let {
                                            val x = it.x * w
                                            val y = it.y * h
                                            Offset(x, y)
                                        }
                                        //println("Finger ${it.finger} x: $x y: $y")
                                    }.let {
                                        drawPoints(
                                            points = it,
                                            brush = SolidColor(c),
                                            pointMode = PointMode.Points,
                                            strokeWidth = 4f,
                                            cap = StrokeCap.Round
                                        )
                                    }

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        Window {
            val keyboard = remember {
                KeyboardUtil.getRelativeKeyboard()
            }
            BoxWithConstraints(
                modifier = Modifier.fillMaxSize()
            ) {
                val density = LocalDensity.current

                Canvas(
                    modifier = Modifier.fillMaxSize()
                ) {
                    clipRect {
                        keyboard.forEach { row ->
                            row.forEach { key ->
                                println("it.strChars = ${key.ch}")
                                drawKey(key)
                            }
                        }
                    }
                }
                /*
                keys.forEach { row ->
                    row.fold(0f) { acc, it ->
                        val (w, h) = it.getBounds()
                        val x = it.xCoord.times(hScale).toFloat()
                        if (!it.isSpacer()) {
                            Text(
                                text = it.strChars,
                                modifier = Modifier.offset {
                                    IntOffset.Zero.copy(
                                        x = (x.plus(acc) + w.times(hScale).toFloat().div(2)).toInt(),
                                        y = (it.yCoord.times(vScale).toFloat() + h.times(vScale).toFloat().div(2)).toInt()
                                    )
                                }
                            )
                        }
                        acc.plus(x)
                    }
                }
                 */
            }

        }
    }

    fun DrawScope.drawKey(key: RelativeKey) {
        val (w, h) = size
        drawRect(
            brush = SolidColor(Color.White),
            topLeft = Offset(
                x = w * key.x,
                y = h * key.y
            ),
            size = Size(
                width = w * key.w,
                height = h * key.h
            )
        )
        drawRect(
            brush = SolidColor(Color.Black),
            topLeft = Offset(
                x = w * key.x,
                y = h * key.y
            ),
            size = Size(
                width = w * key.w,
                height = h * key.h
            ),
            style = Stroke(width = 1f)
        )
    }
}





