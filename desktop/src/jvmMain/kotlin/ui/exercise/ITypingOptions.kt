package ui.exercise

import textgen.generators.IGeneratorOptions

interface ITypingOptions {
    val generatorOptions: IGeneratorOptions

    val durationMillis: Long
    val type: ExerciseMode
}
