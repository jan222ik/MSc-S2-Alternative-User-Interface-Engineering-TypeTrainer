package ui.exercise

import kotlinx.serialization.Serializable
import textgen.generators.AbstractGeneratorOptions

@Serializable
abstract class AbstractTypingOptions {

    abstract val generatorOptions: AbstractGeneratorOptions

    abstract val durationMillis: Long
    abstract val exerciseMode: ExerciseMode

    abstract val isCameraEnabled: Boolean

    abstract fun copyOptions(
        generatorOptions: AbstractGeneratorOptions = this.generatorOptions,
        durationMillis: Long = this.durationMillis,
        exerciseMode: ExerciseMode = this.exerciseMode,
        isCameraEnabled: Boolean = this.isCameraEnabled
    ): AbstractTypingOptions
}
