package ui.exercise

import textgen.generators.IGeneratorOptions

data class TypingOptions(
    override val generatorOptions: IGeneratorOptions,
    override val durationMillis: Long,
    override val type: ExerciseMode,
    override val isCameraEnabled: Boolean,
) : ITypingOptions {
    override fun copyOptions(
        generatorOptions: IGeneratorOptions,
        durationMillis: Long,
        type: ExerciseMode,
        isCameraEnabled: Boolean
    ) = this.copy(
        generatorOptions = generatorOptions,
        durationMillis = durationMillis,
        type = type,
        isCameraEnabled = isCameraEnabled
    )
}
