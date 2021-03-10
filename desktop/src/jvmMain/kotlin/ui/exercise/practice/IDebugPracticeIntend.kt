package ui.exercise.practice

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State

interface IDebugPracticeIntend : IPracticeIntend {
    fun modifyRemainingTimeByAmount(amountMs: Long)
    val timeSkip: State<Boolean>

    val _isCameraEnabled: MutableState<Boolean>

    suspend fun forceNextText()
}
