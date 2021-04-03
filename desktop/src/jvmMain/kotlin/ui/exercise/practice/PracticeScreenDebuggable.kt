@file:Suppress("FunctionName")

package ui.exercise.practice

import TypeTrainerTheme
import androidx.compose.desktop.AppWindow
import androidx.compose.desktop.LocalAppWindow
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ui.dashboard.BaseDashboardCard

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
                val scope = rememberCoroutineScope()
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
                                        onClick = intend::start
                                    ) {
                                        Text("Start Timer")
                                    }
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
                                                text = timerUpdateCycles.value.div(timeRem.toFloat()).toString()
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
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.surface
                        ) {
                            Text(text = "Generator:")
                        }
                    }
                    item {
                        val scope = rememberCoroutineScope()
                        BaseDashboardCard {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Actions:")
                                    OutlinedButton(
                                        onClick = { scope.launch { intend.forceNextText() } }
                                    ) {
                                        Text("Force next text")
                                    }

                                }
                                Row {
                                    val textFlow = intend.textStateFlow.collectAsState()
                                    Text("Text Length: ")
                                    Text(text = textFlow.value.length.toString())
                                }
                            }
                        }
                    }
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.surface
                        ) {
                            Text(text = "UI:")
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
                                        onClick = { intend._isCameraEnabled.value = !intend.isCameraEnabled.value }
                                    ) {
                                        Text("Toggle CameraPreview")
                                    }

                                }
                            }
                        }
                    }
                    stickyHeader {
                        Surface(
                            modifier = Modifier.fillMaxSize(),
                            color = MaterialTheme.colors.surface
                        ) {
                            Text(text = "Typing:")
                        }
                    }
                    item {
                        val (isStarted, setStarted) = remember { mutableStateOf(false) }
                        BaseDashboardCard {
                            Column {
                                val (periodMs, setPeriodMs) = remember { mutableStateOf("30") }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    if (!isStarted) {
                                        Text("Period[ms]")
                                        TextField(
                                            value = periodMs,
                                            onValueChange = setPeriodMs
                                        )
                                    } else {
                                        Text("Period ${1f.div(periodMs.toLong().div(1000f))}kHz")
                                    }
                                }
                                if (!isStarted) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        val clockState = intend.typingClockStateStateFlow.collectAsState()
                                        OutlinedButton(
                                            enabled = clockState.value == IPracticeIntend.TypingClockState.ACTIVE,
                                            onClick = {
                                                scope.launch { intend.startConstantSpeedTypeDemo(periodMs.toLong()) }
                                                setStarted(true)
                                            }
                                        ) {
                                            Text("Start")
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
