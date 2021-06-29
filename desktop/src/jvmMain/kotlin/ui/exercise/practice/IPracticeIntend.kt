package ui.exercise.practice

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import ui.exercise.AbstractTypingOptions

interface IPracticeIntend {
    val typingOptions: AbstractTypingOptions
    val timerStateFlow: StateFlow<Long>
    val typingClockStateStateFlow: StateFlow<TypingClockState>
    val timerUpdateCycleCountStateFlow: StateFlow<Long>

    val isCameraEnabled: State<Boolean>

    fun start()
    fun cancelRunningJobs()

    enum class TypingClockState {
        PREVIEW, ACTIVE, FINISHED
    }
}
