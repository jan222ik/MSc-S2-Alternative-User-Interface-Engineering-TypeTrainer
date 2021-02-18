package ui.exercise.selection

import androidx.compose.runtime.mutableStateOf

class ExerciseSelectionIntent {
    val textModeSelection = mutableStateOf(0)
    val exerciseModeSelection = mutableStateOf(0)

    companion object {
        // TODO Replace with i18nKeys
        val textModeSelectionOptions = listOf("Literature", "Random Words", "Random Characters")
        val exerciseModeSelectionOptions = listOf("Speed", "Accuracy", "No Timelimit")
    }
}
