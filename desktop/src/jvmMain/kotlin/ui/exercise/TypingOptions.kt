package ui.exercise

import textgen.generators.IGeneratorOptions

data class TypingOptions(
    override val generatorOptions: IGeneratorOptions,
    override val durationMillis: Long,
    override val type: ExerciseMode
) : ITypingOptions
