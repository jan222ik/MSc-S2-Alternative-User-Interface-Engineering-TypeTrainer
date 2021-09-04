@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.practice

import androidx.compose.desktop.Window
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.components.TypeTrainerTheme
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import com.github.jan222ik.desktop.LocalWindowState
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordOptions
import com.github.jan222ik.desktop.ui.components.progress.practice.CountDownProgressBar
import com.github.jan222ik.desktop.ui.components.progress.practice.ProgressionProgressBar
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.ui.exercise.ExerciseMode
import com.github.jan222ik.desktop.ui.exercise.TypingOptions
import com.github.jan222ik.desktop.ui.exercise.TypingType
import com.github.jan222ik.desktop.ui.exercise.practice.text.MovingCursorTyping
import com.github.jan222ik.desktop.ui.exercise.practice.text.MovingTextTyping
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.debug.ifDebugCompose
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import com.github.jan222ik.desktop.util.FingerMatcher

@HasDoc
@Composable
fun PracticeScreen(typingOptions: AbstractTypingOptions, fingerMatcher: FingerMatcher?) {
    val intend = remember(typingOptions) {
        when (typingOptions.typingType) {
            TypingType.MovingCursor -> MovingCursorTypingIntend(
                typingOptions = typingOptions,
                fingerMatcher = fingerMatcher
            )
            TypingType.MovingText -> MovingTextTypingIntend(
                typingOptions = typingOptions,
                fingerMatcher = fingerMatcher
            )
        }
    }
    DisposableEffect(intend) {
        onDispose {
            intend.cancelRunningJobs()
        }
    }
    val localWindowState = LocalWindowState.current
    ifDebugCompose {
        DisposableEffect(intend) {
            var closeWindow: (() -> Unit)? = null
            PracticeScreenDebuggable(
                intend = intend as IDebugPracticeIntend,
                parentWindowState = localWindowState,
                windowClose = {
                    closeWindow = it
                }
            )

            onDispose {
                closeWindow?.invoke()
            }
        }
    }
    PracticeScreenContent(intend)
}

@Composable
private fun PracticeScreenContent(intend: ITextDisplayPracticeIntend) {
    val max = intend.typingOptions.durationMillis.div(1000).toFloat()
    val clockState = intend.typingClockStateStateFlow.collectAsState()
    if (clockState.value == IPracticeIntend.TypingClockState.FINISHED) {
        Popup {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.surface.copy(alpha = 0.8f))
            ) {
                Box {
                    val router = WindowRouterAmbient.current
                    Column(
                        modifier = Modifier.align(Alignment.Center)
                    ) {
                        Text(
                            text = "Time elapsed."
                        )
                        Button(
                            onClick = {
                                val route = ApplicationRoutes.Exercise.ExerciseResults(
                                    exerciseResults = intend.result
                                )
                                router.navTo(route)
                            }
                        ) {
                            Text(text = "See your results")
                        }
                    }
                }
            }
        }
    }
    Column(
        modifier = Modifier.fillMaxWidth().padding(25.dp)
    ) {
        //progress bar
        if (intend.typingOptions.exerciseMode == ExerciseMode.Timelimit) {
            val timer = intend.timerStateFlow.collectAsState()
            CountDownProgressBar(
                modifier = Modifier.fillMaxWidth(),
                value = max - timer.value.div(1000).toFloat(),
                max = max,
                trackColor = MaterialTheme.colors.background
            )
        } else {
            val otherMax = intend.textStateFlow.collectAsState("0")
            when (intend.typingOptions.typingType) {
                TypingType.MovingCursor -> {
                    val textTyped = intend.textTypedIndex.collectAsState(0)
                    ProgressionProgressBar(
                        modifier = Modifier.fillMaxWidth(),
                        value = textTyped.value.toFloat(),
                        max = otherMax.value.length.toFloat(),
                    )
                }
                TypingType.MovingText -> {
                    val textTyped = intend.textTyped.collectAsState("0")
                    ProgressionProgressBar(
                        modifier = Modifier.fillMaxWidth(),
                        value = textTyped.value.length.toFloat(),
                        max = otherMax.value.length.toFloat(),
                    )
                }
            }

        }

        BaseDashboardCard(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .fillMaxWidth(0.7f)
                .padding(vertical = 8.dp)
                .fillMaxHeight(0.5f)
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center).fillMaxSize()
            ) {
                when (intend) {
                    is MovingCursorTypingIntend -> MovingCursorTyping(intend)
                    is MovingTextTypingIntend -> MovingTextTyping(intend)
                }
            }
        }
        if (intend.isCameraEnabled.value) {
            // collapsable video feed
            VideoFeedExpanding(title = "Video Feed")
        }
    }

}

/**
 * Expanding Card fo collapsable Video feed
 * TODO
 */
@Composable
private fun ColumnScope.VideoFeedExpanding(
    title: String,
    bgColor: Color = MaterialTheme.colors.background,
    shape: Shape = RoundedCornerShape(16.dp) //TODO change to fit bottom of screen
) {
    var expanded by remember { mutableStateOf(false) }
    Box(
        modifier = Modifier
            .align(Alignment.CenterHorizontally)
            .fillMaxHeight()
    ) {
        Card(
            modifier = Modifier.align(Alignment.BottomCenter),
            shape = shape,
            backgroundColor = bgColor,
            elevation = 16.dp
        ) {
            Column(modifier = Modifier.padding(all = 5.dp)) {
                Text(text = title, textAlign = TextAlign.Center)
                if (expanded) {
                    //Text(text = body)
                    VideoFeedLive()
                    IconButton(onClick = { expanded = false }) {
                        Icon(Icons.Default.ExpandLess, contentDescription = "Collapse")
                    }
                } else {
                    IconButton(onClick = { expanded = true }) {
                        Icon(Icons.Default.ExpandMore, contentDescription = "Expand")
                    }
                }
            }
        }
    }
}

@Composable
private fun VideoFeedLive() { //placeholder for actual live feed
    Box(
        modifier = Modifier.fillMaxWidth().padding(50.dp)
            .background(Color.DarkGray)
    ) {
        Text(
            modifier = Modifier.padding(50.dp),
            text = "Live Feed Placeholder",
            color = Color.LightGray
        )
    }
}

fun main() {
    Window {
        TypeTrainerTheme {
            Surface(Modifier.fillMaxSize()) {
                PracticeScreen(
                    TypingOptions(
                        generatorOptions = RandomKnownWordOptions(
                            seed = 1L,
                            language = LanguageDefinition.German,
                            minimalSegmentLength = 300
                        ),
                        durationMillis = 60 * 1000,
                        exerciseMode = ExerciseMode.Timelimit,
                        isCameraEnabled = false,
                        typingType = TypingType.MovingCursor
                    ),
                    null
                )
            }
        }
    }
}

