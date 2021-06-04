package ui.exercise

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import textgen.generators.AbstractGeneratorOptions

@Serializable
@SerialName("@typeTypingOptions")
data class TypingOptions(
    override val generatorOptions: AbstractGeneratorOptions,
    override val durationMillis: Long,
    override val exerciseMode: ExerciseMode,
    override val isCameraEnabled: Boolean,
    override val typingType: TypingType,
) : AbstractTypingOptions() {
    override fun copyOptions(
        generatorOptions: AbstractGeneratorOptions,
        durationMillis: Long,
        exerciseMode: ExerciseMode,
        isCameraEnabled: Boolean
    ) = this.copy(
        generatorOptions = generatorOptions,
        durationMillis = durationMillis,
        exerciseMode = exerciseMode,
        isCameraEnabled = isCameraEnabled
    )
}
