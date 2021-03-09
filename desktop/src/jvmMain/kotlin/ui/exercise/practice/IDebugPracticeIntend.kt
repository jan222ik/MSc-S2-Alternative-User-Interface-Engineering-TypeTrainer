package ui.exercise.practice

import androidx.compose.runtime.State

interface IDebugPracticeIntend : IPracticeIntend {
    fun modifyRemainingTimeByAmount(amountMs: Long)
    val timeSkip: State<Boolean>

    suspend fun forceNextText()
}
