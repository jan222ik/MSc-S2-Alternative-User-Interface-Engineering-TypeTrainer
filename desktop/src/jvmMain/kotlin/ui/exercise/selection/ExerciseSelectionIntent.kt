package ui.exercise.selection

import androidx.compose.runtime.mutableStateOf
import ui.util.i18n.i18n

class ExerciseSelectionIntent {
    val textModeSelection = mutableStateOf(0)
    val exerciseModeSelection = mutableStateOf(0)
    val languageSelection = mutableStateOf(0)
    val durationSelection = mutableStateOf(0)
    val customDurationSelection = mutableStateOf("")

    companion object {
        val textModeSelectionOptions = i18n.str.exercise.selection.textMode.getAll()
        val exerciseModeSelectionOptions =  i18n.str.exercise.selection.exerciseMode.getAll()
        val languageSelectionOptions = i18n.str.settings.languages.getAll()
        val durationSelectionOptions = i18n.str.exercise.selection.exerciseMode.getDurations()
    }
}
