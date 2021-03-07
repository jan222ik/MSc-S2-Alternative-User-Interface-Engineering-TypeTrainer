@file:Suppress("FunctionName")

package ui.exercise.practice

import TypeTrainerTheme
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import textgen.generators.impl.RandomKnownWordGenerator
import ui.components.progress.practice.CountDownProgressBar
import ui.dashboard.BaseDashboardCard
import ui.exercise.TypingOptions
import ui.exercise.TypingType
import ui.util.i18n.LanguageDefinition

@Composable
fun PracticeScreen() {
    val intend = PracticeIntendImpl(
        typingOptions = TypingOptions(
            generatorOptions = RandomKnownWordGenerator.RandomKnownWordOptions(
                seed = 1L,
                language = LanguageDefinition.German,
                minimalSegmentLength = 1000
            ),
            durationMillis = 60 * 1000,
            type = TypingType.PRACTICE_SPEED_ACCURACY
        )
    )
    DisposableEffect(intend) {
       onDispose {
           intend.cancelRunningJobs()
       }
    }
    PracticeScreenContent(intend)
}

@Composable
private fun PracticeScreenContent(intend: IPracticeIntend) {
    val localWindow = LocalAppWindow.current
    LaunchedEffect(intend) {
        if (System.getProperty("debug") == "true") {
            PracticeScreenDebuggable(
                intend = intend as IDebugPracticeIntend,
                parentWindowLocation = IntOffset(localWindow.x, localWindow.y),
                parentWindowWidth = localWindow.width,
                parentWindowHeight = localWindow.height,
                windowClose = {
                    localWindow.events.onClose = it
                }
            )
        }
    }
    val max = intend.typingOptions.durationMillis.div(1000).toFloat()
    Column(
        modifier = Modifier.fillMaxWidth(0.8f)
    ) {
        val timer = intend.timerStateFlow.collectAsState()
        CountDownProgressBar(
            modifier = Modifier.fillMaxWidth(),
            value = max - timer.value.div(1000).toFloat(),
            max = max
        )
        Text(text = "Generated text:")
        val text = intend.textStateFlow.collectAsState()
        Text(text = text.value)
        Button(
            onClick = intend::start
        ) {
            Text("Start Timer")
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
fun PracticeScreenDebuggable(
    intend: IDebugPracticeIntend,
    parentWindowLocation: IntOffset,
    parentWindowWidth: Int,
    parentWindowHeight: Int,
    windowClose: (() -> Unit) -> Unit
) {
    AppWindow(
        title = "PracticeScreenDebuggable",
        location = parentWindowLocation.copy(x = parentWindowLocation.x.plus(parentWindowWidth)),
        //TODO Check that it is still on screen
        centered = false,
        size = IntSize(width = 400, height = parentWindowHeight)
    ).show {
        val current = LocalAppWindow.current
        windowClose.invoke {
            try {
                current.close()
            } catch (e: IllegalStateException) {

            }
        }
        TypeTrainerTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.surface
            ) {
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.surface
                        ) {
                            Text(text = "Options:")
                        }
                    }
                    item {
                        BaseDashboardCard {
                            Column {
                                Row {
                                    Text("DurationMillis: ")
                                    Text(text = intend.typingOptions.durationMillis.toString())
                                }
                                Row {
                                    Text("Type: ")
                                    Text(text = intend.typingOptions.type.toString())
                                }
                            }
                        }
                    }
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.surface
                        ) {
                            Text(text = "Clock:")
                        }
                    }
                    item {
                        BaseDashboardCard {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Actions:")
                                    OutlinedButton(
                                        onClick = {
                                            intend.modifyRemainingTimeByAmount(5000)
                                        }
                                    ) {
                                        Text("+5s")
                                    }
                                    OutlinedButton(
                                        onClick = {
                                            intend.modifyRemainingTimeByAmount(-5000)
                                        }
                                    ) {
                                        Text("-5s")
                                    }
                                }
                                Row {
                                    val clock = intend.typingClockStateStateFlow.collectAsState()
                                    Text("State: ")
                                    Text(text = clock.value.toString())
                                }

                                val timer = intend.timerStateFlow.collectAsState()
                                Row {
                                    Text("Time: ")
                                    Text(text = timer.value.toString())
                                }
                                val timerUpdateCycles = intend.timerUpdateCycleCountStateFlow.collectAsState()
                                val timeRem = (intend.typingOptions.durationMillis - timer.value).coerceAtLeast(1)
                                Column {
                                    Row {
                                        Text("Update Cycles: ")
                                        Text(text = timerUpdateCycles.value.toString())
                                    }
                                    Text(text = "Average Cycles:" + ("".takeUnless { intend.timeSkip.value }
                                        ?: " [Values not accurate due to time skip]"))
                                    Column(modifier = Modifier.padding(start = 8.dp)) {
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(text = "cycles / ms:")
                                            Text(
                                                text = timerUpdateCycles.value.div(timeRem).toString()
                                            )
                                        }
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(text = "cycles / ns:")
                                            Text(
                                                text = timerUpdateCycles.value.div(timeRem)
                                                    .div(1000f).toString()
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
    }
}

fun main() {
    System.setProperty("debug", "true")
    Window {
        TypeTrainerTheme {
            PracticeScreen()
        }
    }
}
