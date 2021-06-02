package ui.exercise.practice.text

import androidx.compose.ui.input.key.KeyEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ui.exercise.practice.IPracticeIntend
import ui.exercise.practice.ITextDisplayPracticeIntend
import ui.util.debug.ifDebug

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
                    TODO("Get starting position of fingers here and launch keyboard")
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
