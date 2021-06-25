@file:Suppress("FunctionName")

package ui.exercise.practice.text

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import ui.exercise.practice.IPracticeIntend
import ui.exercise.practice.ITextDisplayPracticeIntend

@Composable
fun MovingTextTyping(intend: ITextDisplayPracticeIntend) {
    val focusRequester = FocusRequester()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
    val scope = rememberCoroutineScope()
    val clockState = intend.typingClockStateStateFlow.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable(enabled = !isFocused.value) {
                focusRequester.requestFocus()
            }
            .focusRequester(focusRequester)
            .focusable(interactionSource = interactionSource)
            .onKeyEvent {
                if (it.type == KeyEventType.KeyDown) {
                    handleInput(intend, scope, clockState.value, it)
                }
                false
            },
    ) {
        val error = MaterialTheme.colors.error
        val onError = MaterialTheme.colors.onError
        val isError = intend.currentIsError.collectAsState()
        val textTyped = intend.textTyped.collectAsState()
        val textCurrent = intend.textCurrent.collectAsState()
        val textFuture = intend.textFuture.collectAsState()

        val annotatedString = remember(textTyped.value) {
            buildAnnotatedString {
                val typedTrunc = textTyped.value.takeLast(10)
                val futureTrunc = textFuture.value.take(30)
                append(typedTrunc)
                addStyle(SpanStyle(color = Color.Green), 0, typedTrunc.length)
                append(textCurrent.value.last())
                if (isError.value) {
                    this.addStyle(
                        SpanStyle(color = onError, background = error),
                        typedTrunc.length,
                        typedTrunc.length.inc()
                    )
                }
                append(futureTrunc)
            }
        }

        Text(text = annotatedString, style = MaterialTheme.typography.h5)

        DisposableEffect(Unit) {
            focusRequester.requestFocus()
            onDispose { }
        }

        if (clockState.value == IPracticeIntend.TypingClockState.PREVIEW)
            TypingSetup(intend)
    }
}
