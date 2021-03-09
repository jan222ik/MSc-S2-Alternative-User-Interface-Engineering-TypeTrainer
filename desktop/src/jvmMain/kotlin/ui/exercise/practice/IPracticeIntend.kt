package ui.exercise.practice

import kotlinx.coroutines.flow.StateFlow
import ui.exercise.ITypingOptions

interface IPracticeIntend {
    val typingOptions: ITypingOptions
    val textStateFlow: StateFlow<String>
    val timerStateFlow: StateFlow<Long>
    val typingClockStateStateFlow: StateFlow<TypingClockState>
    val timerUpdateCycleCountStateFlow: StateFlow<Long>

    fun start()
    fun cancelRunningJobs()

    enum class TypingClockState {
        PREVIEW, ACTIVE, FINISHED
    }
}
