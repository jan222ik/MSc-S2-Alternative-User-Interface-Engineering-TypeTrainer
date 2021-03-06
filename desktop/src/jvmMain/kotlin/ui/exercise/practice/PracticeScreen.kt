package ui.exercise.practice

import TypeTrainerTheme
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.desktop.Window
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
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
    PracticeScreenContent(intend)
}

@Composable
private fun PracticeScreenContent(intend: IPracticeIntend) {
    val localWindow = LocalAppWindow.current
    LaunchedEffect(intend) {
        if (System.getProperty("debug") == "true") {
            PracticeScreenDebugable(intend as IDebugPracticeIntend, IntOffset(localWindow.x, localWindow.y), localWindow.width)
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
fun PracticeScreenDebugable(intend: IDebugPracticeIntend, parentWindowLocation: IntOffset, parentWindowWidth: Int) {
    Window(
        title = "PracticeScreenDebugable",
        location = parentWindowLocation.copy(x = parentWindowLocation.x.plus(parentWindowWidth)),
        //TODO Check that it is still on screen
        centered = false
    ) {
        TypeTrainerTheme {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.surface
            ) {
                LazyColumn(modifier = Modifier.padding(8.dp).fillMaxSize()) {
                    stickyHeader {
                        Text(text = "Options:")
                    }
                    item {
                        val list = listOf(
                            "DurationMillis:" to intend.typingOptions.durationMillis,
                            "Type:" to intend.typingOptions.type
                        )
                        BaseDashboardCard {
                            Column {
                                list.forEach {
                                    Box(modifier = Modifier.fillMaxWidth()) {
                                        Text(
                                            modifier = Modifier.align(Alignment.TopStart),
                                            text = it.first
                                        )
                                        Text(
                                            modifier = Modifier.align(Alignment.TopEnd),
                                            text = it.second.toString()
                                        )
                                    }
                                }
                            }
                        }
                    }
                    item {
                        BaseDashboardCard {
                            Column {
                                Text(text = "Clock:")
                                Column(modifier = Modifier.padding(start = 8.dp)) {
                                    Row {
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
                                        Text(text = "Average Cycles:" + ("".takeUnless { intend.timeSkip.value } ?: " [Values not accurate due to time skip]"))
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
}

fun main() {
    System.setProperty("debug", "true")
    Window {
        TypeTrainerTheme {
            PracticeScreen()
        }
    }
}
