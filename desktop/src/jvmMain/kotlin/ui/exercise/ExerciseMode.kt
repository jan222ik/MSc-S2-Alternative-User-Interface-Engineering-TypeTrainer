package ui.exercise

import kotlinx.serialization.Serializable

@Serializable
sealed class ExerciseMode {
    @Serializable
    object Speed : ExerciseMode()
    @Serializable
    object Accuracy : ExerciseMode()
    @Serializable
    object NoTimelimit : ExerciseMode()
}
