@file:Suppress("FunctionName")

package ui.exercise.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import kotlinx.coroutines.time.delay
import ui.dashboard.ApplicationRoutes
import ui.exercise.AbstractTypingOptions
import ui.general.WindowRouterAmbient
import ui.util.i18n.RequiresTranslationI18N
import ui.util.i18n.i18n
import ui.util.span_parse.SpanStringParser.parseForSpans
import java.awt.event.KeyEvent.KEY_LOCATION_RIGHT
import java.awt.event.KeyEvent.KEY_LOCATION_STANDARD
import java.time.Duration
import java.time.temporal.ChronoUnit

@Composable
fun KeyboardSynchronisationScreen(
    trainingOptions: AbstractTypingOptions
) {
    val router = WindowRouterAmbient.current
    val synchronisationIntent = remember {
        SynchronisationIntent(
            onFinishSync = {
                router.navTo(ApplicationRoutes.Exercise.Training(trainingOptions = trainingOptions))
            }
        )
    }

    KeyboardSynchronisationScreenContent(
        synchronisationIntent = synchronisationIntent
    )
}

@Composable
private fun KeyboardSynchronisationScreenContent(
    synchronisationIntent: SynchronisationIntent
) {
    val highlightSpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colors.primary
    )
    val normalSpanStyle = SpanStyle(
        fontWeight = FontWeight.Bold,
        color = Color.Unspecified
    )
    val strRes = i18n.str.exercise.keyboard_sync
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.TopCenter
    ) {
        BaseDashboardCard(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 24.dp)
                .fillMaxHeight()
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(vertical = 16.dp)
                    .fillMaxSize()
            ) {
                Text(
                    text = +RequiresTranslationI18N("Synchronising Keyboard with Finger positions from the camera.")
                )
                Text(text = "DEBUG: Current STEP ${synchronisationIntent.step.value}")

                val focusRequester = remember { FocusRequester() }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Red)
                        //.clickable { focusRequester.requestFocus() }
                        .focusRequester(focusRequester)
                        .focusable()
                        .onPreviewKeyEvent { evt ->
                            synchronisationIntent.handleInput(evt)
                            true
                        },
                ) { /* Target of focus to obtain presses */ }
                LaunchedEffect(null) {
                    delay(Duration.of(200, ChronoUnit.MILLIS))
                    focusRequester.requestFocus()
                }
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(text = +strRes.step1)
                        Text(text = (+strRes.step2).parseForSpans(highlightSpanStyle))
                        Text(
                            text = (+strRes.step3).parseForSpans(
                                spanStyle = when (synchronisationIntent.step.value < 1) {
                                    true -> normalSpanStyle
                                    false -> highlightSpanStyle
                                }
                            ),
                            color = when (synchronisationIntent.step.value < 1) {
                                true -> Color.Unspecified.copy(alpha = 0.7f)
                                false -> Color.Unspecified
                            }
                        )
                    }
                }

            }
        }
    }
}

class SynchronisationIntent(
    private val onFinishSync: () -> Unit
) {

    private val mutableStep: MutableState<Int> = mutableStateOf(0)
    val step: State<Int>
        get() = mutableStep

    fun handleInput(evt: KeyEvent) {
        if (evt.type == KeyEventType.KeyDown) {
            val nativeKeyEvt = evt.nativeKeyEvent
            when {
                // Right Control
                nativeKeyEvt.keyCode == 17 && nativeKeyEvt.keyLocation == KEY_LOCATION_RIGHT -> {
                    if (step.value == 1) {
                        // TODO Save Location or something
                        // TODO IF OTHER STEPS MOVE TO LAST STEP
                        mutableStep.value = 2 // Useless unless above necessary
                        onFinishSync.invoke()
                    }
                }
                // Escape
                nativeKeyEvt.keyCode == 27 && nativeKeyEvt.keyLocation == KEY_LOCATION_STANDARD -> {
                    if (step.value == 0) {
                        // TODO Save Location or something
                        mutableStep.value = 1
                    }
                }
                else -> {
                    println("Other button pressed! -> $evt")
                }
            }
        }
    }
}


