@file:Suppress("FunctionName")

package com.github.jan222ik.desktop.ui.exercise.connection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.github.jan222ik.common.HasDoc
import com.github.jan222ik.common.ui.dashboard.BaseDashboardCard
import kotlinx.coroutines.time.delay
import com.github.jan222ik.desktop.network.Server
import com.github.jan222ik.desktop.ui.dashboard.ApplicationRoutes
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.ui.general.WindowRouterAmbient
import com.github.jan222ik.desktop.ui.util.i18n.LocalTranslationI18N
import com.github.jan222ik.desktop.ui.util.i18n.i18n
import com.github.jan222ik.desktop.ui.util.span_parse.parseForSpans
import com.github.jan222ik.desktop.util.FingerMatcher
import java.time.Duration
import java.time.temporal.ChronoUnit

/**
 * Screen for Keyboard location in camera view.
 * @param trainingOptions [AbstractTypingOptions] to be used for the exercise.
 */
@HasDoc
@Composable
fun KeyboardSynchronisationScreen(
    trainingOptions: AbstractTypingOptions,
    server: Server
) {
    val router = WindowRouterAmbient.current
    val synchronisationIntent = remember(server) {
        val fingerMatcher = FingerMatcher(
            channel = server.handLandMarks
        )
        SynchronisationIntent(
            onFinishSync = {
                router.navTo(
                    ApplicationRoutes.Exercise.Training(
                        trainingOptions = trainingOptions,
                        fingerMatcher = fingerMatcher
                    )
                )
            },
            fingerMatcher = fingerMatcher
        )
    }

    KeyboardSynchronisationScreenContent(
        synchronisationIntent = synchronisationIntent
    )
}

@HasDoc
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
                    text = +LocalTranslationI18N("Synchronising Keyboard with Finger positions from the camera.",
                    "Synchronisieren der Tastatur mit den Fingerpositionen der Kamera")
                )
                Text(text = "DEBUG: Current STEP ${synchronisationIntent.step.value}")

                val focusRequester = remember { FocusRequester() }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .background(Color.Red)
                        .clickable { focusRequester.requestFocus() }
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