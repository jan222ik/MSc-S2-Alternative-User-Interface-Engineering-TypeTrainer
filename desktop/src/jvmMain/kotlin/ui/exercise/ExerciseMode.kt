package ui.exercise

sealed class ExerciseMode {
    object Speed : ExerciseMode()
    object Accuracy : ExerciseMode()
    object NoTimelimit : ExerciseMode()
}
