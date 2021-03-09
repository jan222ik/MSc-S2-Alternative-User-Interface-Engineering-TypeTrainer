package ui.exercise.selection

import ui.util.i18n.LanguageDefinition

class ExerciseSelectionOptions(
    intent: ExerciseSelectionIntent
) {
    val textMode: TextMode
    val exerciseMode: ExerciseMode
    val language: LanguageDefinition

    init {
        textMode = when (intent.textModeSelection.value){
            0 -> TextMode.Literature
            1 -> TextMode.RandomWords
            2 -> TextMode.RandomChars
            else -> throw RuntimeException("Invalid option in text-mode while selecting an exercise")
        }
        exerciseMode = when (intent.exerciseModeSelection.value){
            0 -> ExerciseMode.Speed
            1 -> ExerciseMode.Accuracy
            2 -> ExerciseMode.NoTimelimit
            else -> throw RuntimeException("Invalid option in exercise-mode while selecting an exercise")
        }
        language = when (intent.languageSelection.value){
            0 -> LanguageDefinition.English
            1 -> LanguageDefinition.German
            else -> throw RuntimeException("Invalid language option while selecting an exercise")
        }
    }

    sealed class TextMode {
        object Literature: TextMode()
        object RandomWords: TextMode()
        object RandomChars: TextMode()
    }
    sealed class ExerciseMode {
        object Speed: ExerciseMode()
        object Accuracy: ExerciseMode()
        object NoTimelimit: ExerciseMode()
    }
}