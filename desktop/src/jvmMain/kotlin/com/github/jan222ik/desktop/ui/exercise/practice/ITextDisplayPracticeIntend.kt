package com.github.jan222ik.desktop.ui.exercise.practice

import kotlinx.coroutines.flow.StateFlow
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation

interface ITextDisplayPracticeIntend : IPracticeIntend {
    val exerciseEvaluation: ExerciseEvaluation

    val textStateFlow: StateFlow<String>
    val currentIsError: StateFlow<Boolean>

    val textTyped: StateFlow<String>
    val textTypedIndex: StateFlow<Int>

    val textCurrent: StateFlow<String>

    val textFuture: StateFlow<String>

    val result: ExerciseEvaluation


    suspend fun checkChar(char: Char)

    suspend fun nextText()
}
