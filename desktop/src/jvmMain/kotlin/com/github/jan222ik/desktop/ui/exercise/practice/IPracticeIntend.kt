package com.github.jan222ik.desktop.ui.exercise.practice

import androidx.compose.runtime.State
import kotlinx.coroutines.flow.StateFlow
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.util.FingerUsed

interface IPracticeIntend {
    val typingOptions: AbstractTypingOptions
    val timerStateFlow: StateFlow<Long>
    val typingClockStateStateFlow: StateFlow<TypingClockState>
    val timerUpdateCycleCountStateFlow: StateFlow<Long>

    val isCameraEnabled: State<Boolean>

    fun start()
    fun cancelRunningJobs()


    val hasFingerTracking: Boolean
    fun checkFingerForChar(char: String) : FingerUsed?

    enum class TypingClockState {
        PREVIEW, ACTIVE, FINISHED
    }
}
