@file:Suppress("FunctionName")

package ui.exercise.training

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDirection
import androidx.compose.ui.text.style.TextIndent
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ui.dashboard.BaseDashboardCard
import ui.util.i18n.i18n
import kotlin.math.min

@Composable
fun TrainingScreen() {
    val training = remember { Training() }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        BaseDashboardCard(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .padding(vertical = 24.dp)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    val errorState = training.errorFlow.collectAsState()
                    Row {
                        val scrollingAheadText: State<TextFieldValue> = training.textStateFlow.collectAsState()
                        val scrollingBehindText: State<String> = training.behindTextStateFlow.collectAsState()
                        TextField(
                            value = "",
                            onValueChange = {},
                            trailingIcon = { Text(text = scrollingBehindText.value, textAlign = TextAlign.End) },
                            readOnly = true,
                        )

                        TextField(
                            value = scrollingAheadText.value,
                            onValueChange = training::handleInput,
                            isErrorValue = errorState.value
                        )
                    }
                    Text("Error".takeIf { errorState.value } ?: "")
                }
            }
        }
    }
}

class Training(
    private val letterBufferAhead: Int = 50,
    private val letterBufferBehind: Int = 20
) {
    private val scrollingText = (i18n.str.exercise.selection.textMode.literatureDescription.resolve() + " ").repeat(5)
    private var runningIndex = 0

    private val internalTextFlowState: MutableStateFlow<TextFieldValue> = MutableStateFlow(
        TextFieldValue(
            text = scrollingText.substring(0 until min(scrollingText.length, letterBufferAhead)),
            selection = TextRange(0)
        )
    )
    val textStateFlow: StateFlow<TextFieldValue>
        get() = internalTextFlowState

    private val internalBehindTextStateFlow: MutableStateFlow<String> = MutableStateFlow("")
    val behindTextStateFlow: StateFlow<String>
        get() = internalBehindTextStateFlow

    private val internalErrorFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val errorFlow: StateFlow<Boolean>
        get() = internalErrorFlow


    private val maxIndex = scrollingText.length

    private var currentChar: Char = scrollingText[0]

    private suspend fun progressTextScroll() {
        val oldBehind = internalBehindTextStateFlow.value
        val oldAhead = internalTextFlowState.value.text
        val newBehind =
            (if (oldBehind.length > letterBufferBehind) oldBehind.removeRange(0, 1) else oldBehind) + currentChar
        internalBehindTextStateFlow.emit(newBehind)
        val newAhead = oldAhead.removeRange(0, 1)
        currentChar = newAhead[0]
        val text = if (runningIndex < maxIndex) {
            newAhead + scrollingText[runningIndex]
        } else newAhead
        internalTextFlowState.emit(internalTextFlowState.value.copy(text = text, selection = TextRange.Zero))
    }

    fun handleInput(tfv: TextFieldValue) {
        GlobalScope.launch(Dispatchers.IO) {
            val isCorrect = tfv.text.first() == currentChar
            internalErrorFlow.compareAndSet(isCorrect, !isCorrect)
            if (isCorrect) {
                runningIndex++
                progressTextScroll()
            } else {
                println("Wrong")
            }
        }
    }
}
