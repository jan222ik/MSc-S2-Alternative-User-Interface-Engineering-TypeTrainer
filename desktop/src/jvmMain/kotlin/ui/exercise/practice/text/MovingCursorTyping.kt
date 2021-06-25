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
import ui.exercise.practice.ITextDisplayPracticeIntend

@Composable
fun MovingCursorTyping(intend: ITextDisplayPracticeIntend) {
    val scope = rememberCoroutineScope()
    val focusRequester = FocusRequester()
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused = interactionSource.collectIsFocusedAsState()
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
        val text = intend.textStateFlow.collectAsState()
        val isError = intend.currentIsError.collectAsState()
        val textTyped = intend.textTypedIndex.collectAsState()

        val annotatedString = remember(text.value, isError.value, textTyped.value) {
            buildAnnotatedString {
                val idx = textTyped.value
                val str = text.value
                val incIdx = idx.inc()
                val content = when {
                    str[idx] == '\n' -> str.replaceRange(idx..idx, "\u2BB0\n")
                    str.length != incIdx && str[incIdx] == '\n' -> str.replaceRange(incIdx..incIdx, "\u2BB0\n")
                    else -> str
                }
                this.append(content)
                this.addStyle(SpanStyle(color = Color.Green), 0, idx)
                if (isError.value) {
                    this.addStyle(SpanStyle(color = onError, background = error), idx, incIdx)
                }
            }
        }
        Text(text = annotatedString, style = MaterialTheme.typography.h6)
        if (!isFocused.value) {
            TypingSetup(intend)
        }
    }
}
