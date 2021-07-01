package ui.exercise.connection

import androidx.compose.runtime.State
import androidx.compose.ui.input.key.KeyEvent
import com.github.jan222ik.common.HasDoc
import util.FingerMatcher


/**
 * Intent handling the Synchronisation of the keyboards positions in relation to the camera viewport.
 *
 * @param onFinishSync Callback provided by the UI to navigate to the next screen.
 * @property step [State] providing feedback for the UI at which step the synchronisation is.
 */
@HasDoc
class SynchronisationIntent(
    private val onFinishSync: () -> Unit,
    private val fingerMatcher: FingerMatcher
) {


    val step: State<Int>
        get() = fingerMatcher.syncStep


    /**
     * Handles input by the UI, the event is analysed to progress the synchronisation.
     * @param evt [KeyEvent] that was fired by the UI.
     */
    fun handleInput(evt: KeyEvent) {
        fingerMatcher.syncInput(evt)
        if (step.value >= 2) {
            onFinishSync.invoke()
        }
    }
}
