package ui.exercise.connection

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type


/**
 * Intent handling the Synchronisation of the keyboards positions in relation to the camera viewport.
 *
 * @param onFinishSync Callback provided by the UI to navigate to the next screen.
 * @property step [State] providing feedback for the UI at which step the synchronisation is.
 */
class SynchronisationIntent(
    private val onFinishSync: () -> Unit
) {

    private val mutableStep: MutableState<Int> = mutableStateOf(0)
    val step: State<Int>
        get() = mutableStep

    /**
     * Handles input by the UI, the event is analysed to progress the synchronisation.
     * @param evt [KeyEvent] that was fired by the UI.
     */
    fun handleInput(evt: KeyEvent) {
        if (evt.type == KeyEventType.KeyDown) {
            val nativeKeyEvt = evt.nativeKeyEvent
            when {
                // Right Control
                nativeKeyEvt.keyCode == 17 && nativeKeyEvt.keyLocation == java.awt.event.KeyEvent.KEY_LOCATION_RIGHT -> {
                    if (step.value == 1) {
                        // TODO Save Location or something
                        // TODO IF OTHER STEPS MOVE TO LAST STEP
                        mutableStep.value = 2 // Useless unless above necessary
                        onFinishSync.invoke()
                    }
                }
                // Escape
                nativeKeyEvt.keyCode == 27 && nativeKeyEvt.keyLocation == java.awt.event.KeyEvent.KEY_LOCATION_STANDARD -> {
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
