@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.practice

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowSize
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.rememberWindowState
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.desktop.textgen.error.CharEvaluation
import com.github.jan222ik.desktop.ui.exercise.result.TypingErrorTreeMap
import com.github.tehras.charts.line.LineChart
import com.github.tehras.charts.line.LineChartData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("EXPERIMENTAL_IS_NOT_ENABLED")
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PracticeScreenDebuggable(
    intend: IDebugPracticeIntend,
    isOpen: MutableState<Boolean>,
    parentWindowState: WindowState,
) {
    val pPos = parentWindowState.position
    val pSize = parentWindowState.size

    val state = rememberWindowState(
        size = WindowSize(width = 400.dp, height = pSize.height),
        position = WindowPosition(pPos.x + pSize.width, pPos.y)
    )
    androidx.compose.ui.window.Window(
        title = "PracticeScreenDebuggable",
        state = state,
        onCloseRequest = { isOpen.value = false }
    ) {
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
                                    Text(text = intend.typingOptions.exerciseMode.toString())
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
                        val scopeForceNextText = rememberCoroutineScope()
                        BaseDashboardCard {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text("Actions:")
                                    OutlinedButton(
                                        onClick = { scopeForceNextText.launch(Dispatchers.IO) { intend.forceNextText() } }
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
                                val clockState = intend.typingClockStateStateFlow.collectAsState()
                                if (clockState.value == IPracticeIntend.TypingClockState.FINISHED) {
                                    println(intend.exerciseEvaluation)
                                    val lineChartWindowOpen = remember { mutableStateOf(true) }
                                    if (lineChartWindowOpen.value) {
                                        androidx.compose.ui.window.Window(
                                            onCloseRequest = { lineChartWindowOpen.value = false }
                                        ) {
                                            val data = LineChartData(
                                                points = intend.exerciseEvaluation.texts.map { eval ->
                                                    eval.chars
                                                        .filterIsInstance<CharEvaluation.TypingError>()
                                                        .map {
                                                            LineChartData.Point(
                                                                value = it.expected.toFloat(),
                                                                label = it.timeRemaining.toString()
                                                            )
                                                        }
                                                }.flatten(),
                                                startAtZero = true
                                            )
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxSize()
                                                    //.background(Color.Cyan)
                                                    .horizontalScroll(rememberScrollState())
                                            ) {
                                                LineChart(
                                                    modifier = Modifier
                                                        .width(20.dp.times(data.points.size)),
                                                    //.background(Color.Red)
                                                    lineChartData = data,
                                                )
                                            }
                                        }
                                    }
                                    val treeMapWindowOpen = remember { mutableStateOf(true) }
                                    if (treeMapWindowOpen.value) {
                                        androidx.compose.ui.window.Window(
                                            onCloseRequest = { treeMapWindowOpen.value = false }
                                        ) {
                                            Column {
                                                val list = intend.exerciseEvaluation.texts
                                                println("list = $list")
                                                TypingErrorTreeMap(
                                                    modifier = Modifier.fillMaxSize(),
                                                    list = list
                                                )
                                            }
                                        }
                                    }
                                }
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
                                        Text("Period ${1f.div(periodMs.toLong().div(1000f))}Hz")
                                    }
                                }
                                if (!isStarted) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedButton(
                                            enabled = clockState.value != IPracticeIntend.TypingClockState.FINISHED,
                                            onClick = {
                                                scope.launch(Dispatchers.IO) {
                                                    intend.start()
                                                    intend.startConstantSpeedTypeDemo(periodMs.toLong())
                                                }
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
