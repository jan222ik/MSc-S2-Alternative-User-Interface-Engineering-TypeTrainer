package com.github.jan222ik.desktop.ui.exercise

import kotlinx.serialization.Serializable

@Serializable
sealed class ExerciseMode {
    @Serializable
    object Timelimit : ExerciseMode()
    @Serializable
    object NoTimelimit : ExerciseMode()
}
