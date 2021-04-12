package ui.exercise.selection

import androidx.compose.runtime.mutableStateOf
import network.Server
import textgen.generators.impl.RandomCharGenerator
import textgen.generators.impl.RandomKnownTextGenerator
import textgen.generators.impl.RandomKnownWordGenerator
import ui.exercise.ExerciseMode
import ui.exercise.TypingOptions
import ui.util.i18n.LanguageDefinition
import ui.util.i18n.i18n
import util.weightedAt
import kotlin.random.Random

class ExerciseSelectionIntent {
    val withFingerTracking = mutableStateOf(true)
    val textModeSelection = mutableStateOf(0)
    val exerciseModeSelection = mutableStateOf(0)
    val languageSelection = mutableStateOf(0)
    val durationSelection = mutableStateOf(0)
    val customDurationSelection = mutableStateOf("")

    fun generateTypingOptions(): TypingOptions {
        val seed = Random.nextLong()
        val language = when (languageSelection.value) {
            0 -> LanguageDefinition.English
            1 -> LanguageDefinition.German
            else -> throw RuntimeException("Invalid language option while selecting an exercise")
        }
        val duration = when(durationSelection.value) {
            0, 1 -> durationSelection.value + 1L
            else -> customDurationSelection.value.toLongOrNull() ?: 0L
        }.times(60000) // Min to millis
        return TypingOptions(
            generatorOptions = when (textModeSelection.value) {
                0 -> RandomKnownTextGenerator.RandomKnownTextOptions(
                    seed = seed,
                    language = language
                )
                1 -> RandomKnownWordGenerator.RandomKnownWordOptions(
                    seed = seed,
                    language = language,
                    minimalSegmentLength = 300
                )
                2 -> RandomCharGenerator.RandomCharOptions(
                    seed = seed,
                    segmentMinimumLength = 300,
                    wordLengthWeightMap = englishContentWordLengthDistribution,
                    characterWeightMap = englishAlphabetDistribution
                )
                else -> throw RuntimeException("Invalid option in text-mode while selecting an exercise")
            },
            durationMillis = duration,
            type = when (exerciseModeSelection.value){
                0 -> ExerciseMode.Speed
                1 -> ExerciseMode.Accuracy
                2 -> ExerciseMode.NoTimelimit
                else -> throw RuntimeException("Invalid option in exercise-mode while selecting an exercise")
            },
            isCameraEnabled = withFingerTracking.value
        )
    }

    companion object {
        val textModeSelectionOptions = i18n.str.exercise.selection.textMode.getAll()
        val exerciseModeSelectionOptions = i18n.str.exercise.selection.exerciseMode.getAll()
        val languageSelectionOptions = i18n.str.settings.languages.getAll()
        val durationSelectionOptions = i18n.str.exercise.selection.exerciseMode.getDurations()

        val englishContentWordLengthDistribution = mapOf(
            1 weightedAt 3,
            2 weightedAt 8,
            3 weightedAt 52,
            4 weightedAt 183,
            5 weightedAt 299,
            6 weightedAt 413,
            7 weightedAt 475,
            8 weightedAt 407,
            9 weightedAt 382,
            10 weightedAt 234,
            11 weightedAt 192,
            12 weightedAt 108,
            13 weightedAt 62,
            14 weightedAt 21,
            15 weightedAt 6,
            16 weightedAt 3,
        )

        // 100% -> 100_000
        private fun Float.shiftInt(): Int = (this * 1000).toInt()

        val englishAlphabetDistribution = mapOf(
            'a' weightedAt 8.2f.shiftInt(),
            'b' weightedAt 1.5f.shiftInt(),
            'c' weightedAt 2.8f.shiftInt(),
            'd' weightedAt 4.3f.shiftInt(),
            'e' weightedAt 13.0f.shiftInt(),
            'f' weightedAt 2.2f.shiftInt(),
            'g' weightedAt 2.0f.shiftInt(),
            'h' weightedAt 6.1f.shiftInt(),
            'i' weightedAt 7.0f.shiftInt(),
            'j' weightedAt 0.15f.shiftInt(),
            'k' weightedAt 0.77f.shiftInt(),
            'l' weightedAt 4.0f.shiftInt(),
            'm' weightedAt 2.4f.shiftInt(),
            'n' weightedAt 6.7f.shiftInt(),
            'o' weightedAt 7.5f.shiftInt(),
            'p' weightedAt 1.9f.shiftInt(),
            'q' weightedAt 0.095f.shiftInt(),
            'r' weightedAt 6.0f.shiftInt(),
            's' weightedAt 9.3f.shiftInt(),
            't' weightedAt 9.1f.shiftInt(),
            'u' weightedAt 2.8f.shiftInt(),
            'v' weightedAt 0.98f.shiftInt(),
            'w' weightedAt 2.4f.shiftInt(),
            'x' weightedAt 0.15f.shiftInt(),
            'y' weightedAt 2.0f.shiftInt(),
            'z' weightedAt 0.074f.shiftInt(),
        )
    }
}
