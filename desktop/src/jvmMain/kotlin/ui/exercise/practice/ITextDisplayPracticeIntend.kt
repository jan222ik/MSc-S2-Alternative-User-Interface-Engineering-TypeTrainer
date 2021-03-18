package ui.exercise.practice

import kotlinx.coroutines.flow.StateFlow

interface ITextDisplayPracticeIntend : IPracticeIntend {
    val textStateFlow: StateFlow<String>
    val currentIsError: StateFlow<Boolean>

    val textTyped: StateFlow<String>
    val textCurrent: StateFlow<String>
    val textFuture: StateFlow<String>

    suspend fun addCharToQueue(char: Char)

    suspend fun checkChar(char: Char)
}
