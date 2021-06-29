package ui.exercise

import kotlinx.serialization.Serializable

@Serializable
sealed class ExerciseMode {
    @Serializable
    object Timelimit : ExerciseMode()
    @Serializable
    object NoTimelimit : ExerciseMode()
}
