@file:Suppress("FunctionName")

package ui.exercise.practice.text

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.exercise.practice.IPracticeIntend
import ui.exercise.practice.ITextDisplayPracticeIntend
import ui.util.debug.ifDebug
import ui.util.i18n.RequiresTranslationI18N

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
                    str[idx] == '\n' -> str.replaceRange(idx .. idx, "\u2BB0\n")
                    str[incIdx] == '\n' -> str.replaceRange(incIdx..incIdx, "\u2BB0\n")
                    else -> str
                }
                this.append(content)
                this.addStyle(SpanStyle(color = Color.Green), 0, idx)
                if (isError.value) {
                    this.addStyle(SpanStyle(color = onError, background = error), idx, incIdx)
                }
            }
        }
        Text(text = annotatedString)
        if (!isFocused.value) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(color = MaterialTheme.colors.background.copy(alpha = 0.8f))
                    .padding(all = 5.dp),
            ) {
                Text(modifier = Modifier.align(Alignment.Center), text = +RequiresTranslationI18N("Click to enable keyboard!"))
            }
        }
    }
}

@Composable
fun MovingCursorTyping2(intend: ITextDisplayPracticeIntend) {
    val text = intend.textStateFlow.collectAsState()
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
        val mod = Modifier.background(color = MaterialTheme.colors.error)
        val isError = intend.currentIsError.collectAsState()
        val textTyped = intend.textTyped.collectAsState()
        val textCurrent = intend.textCurrent.collectAsState()
        val textFuture = intend.textFuture.collectAsState()
        Box {
            Text(
                text = text.value
            )
            Text(
                modifier = if (isError.value) mod else Modifier,
                text = textCurrent.value
            )
            Text(
                text = textTyped.value,
                color = Color.Green
            )
        }
        if (!isFocused.value) {
            Box(
                modifier = Modifier
                    .matchParentSize()
                    .background(color = MaterialTheme.colors.background.copy(alpha = 0.8f))
                    .padding(all = 5.dp),
            ) {
                Text(modifier = Modifier.align(Alignment.Center), text = "Click to enable keyboard!")
            }
        }
    }
}


fun handleInput(
    intend: ITextDisplayPracticeIntend,
    scope: CoroutineScope,
    clockState: IPracticeIntend.TypingClockState,
    event: KeyEvent
) {
    val char = event.nativeKeyEvent.keyChar
    if (char != '￿') {
        when (clockState) {
            IPracticeIntend.TypingClockState.ACTIVE -> {
                scope.launch(Dispatchers.IO) {
                    intend.checkChar(char)
                }
            }
            IPracticeIntend.TypingClockState.PREVIEW -> {
                scope.launch(Dispatchers.IO) {
                    intend.start()
                    intend.checkChar(char)
                }
            }
            IPracticeIntend.TypingClockState.FINISHED -> Unit
        }
    } else {
        ifDebug {
            println("Unknown Key pressed!")
        }
    }
}

@Composable
fun t(text: MutableState<String>) {
    TextField( // TODO Change to Text with focus
        modifier = Modifier.onKeyEvent {
            if (it.type == KeyEventType.KeyDown) {
                println("Pressed $it")
            }
            false
        },
        value = TextFieldValue(text.value),
        onValueChange = {}
    )
}

