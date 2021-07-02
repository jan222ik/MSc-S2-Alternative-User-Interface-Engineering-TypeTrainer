package com.github.jan222ik.desktop.ui.exercise

import kotlinx.serialization.Serializable
import com.github.jan222ik.desktop.textgen.generators.AbstractGeneratorOptions

@Serializable
abstract class AbstractTypingOptions {

    abstract val generatorOptions: AbstractGeneratorOptions

    abstract val durationMillis: Long
    abstract val exerciseMode: ExerciseMode

    abstract val isCameraEnabled: Boolean

    abstract val typingType: TypingType

    abstract fun copyOptions(
        generatorOptions: AbstractGeneratorOptions = this.generatorOptions,
        durationMillis: Long = this.durationMillis,
        exerciseMode: ExerciseMode = this.exerciseMode,
        isCameraEnabled: Boolean = this.isCameraEnabled
    ): AbstractTypingOptions
}
