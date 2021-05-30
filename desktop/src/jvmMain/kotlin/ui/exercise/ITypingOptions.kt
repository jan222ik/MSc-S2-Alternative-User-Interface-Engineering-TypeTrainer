package ui.exercise

import textgen.generators.IGeneratorOptions

interface ITypingOptions {
    val generatorOptions: IGeneratorOptions

    val durationMillis: Long
    val type: ExerciseMode

    val isCameraEnabled: Boolean

    fun copyOptions(
        generatorOptions: IGeneratorOptions = this.generatorOptions,
        durationMillis: Long = this.durationMillis,
        type: ExerciseMode = this.type,
        isCameraEnabled: Boolean = this.isCameraEnabled
    ) : ITypingOptions
}
