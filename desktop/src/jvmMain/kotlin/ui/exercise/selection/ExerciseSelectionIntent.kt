package ui.exercise.selection

import androidx.compose.runtime.mutableStateOf
import textgen.generators.impl.RandomCharOptions
import textgen.generators.impl.RandomKnownTextOptions
import textgen.generators.impl.RandomKnownWordOptions
import ui.exercise.AbstractTypingOptions
import ui.exercise.ExerciseMode
import ui.exercise.TypingOptions
import ui.exercise.TypingType
import ui.util.i18n.LanguageDefinition
import ui.util.i18n.i18n
import util.weightedAt
import kotlin.random.Random

class ExerciseSelectionIntent(initData: AbstractTypingOptions?) {
    val withFingerTracking = mutableStateOf( false)
    val textModeSelection = mutableStateOf(0)
    val exerciseModeSelection = mutableStateOf(0)
    val typingTypeSelection = mutableStateOf(0)
    val languageSelection = mutableStateOf(0)
    val durationSelection = mutableStateOf(0)
    val customDurationSelection = mutableStateOf("")

    init {
        initData?.let {
            withFingerTracking.value = it.isCameraEnabled
            textModeSelection.value = when (it.generatorOptions) {
                is RandomCharOptions -> 2
                is RandomKnownWordOptions -> 1
                is RandomKnownTextOptions -> 0
                else -> throw RuntimeException("Invalid option in text-mode while selecting an exercise")
            }
            exerciseModeSelection.value = when (it.exerciseMode) {
                ExerciseMode.Timelimit -> 0
                ExerciseMode.NoTimelimit -> 1
            }
            typingTypeSelection.value = when (it.typingType) {
                TypingType.MovingCursor -> 0
                TypingType.MovingText -> 1
                else -> throw RuntimeException("Invalid option in typing-type while selecting an exercise")
            }
            when (val op = it.generatorOptions) {
                is RandomKnownWordOptions -> op.language
                is RandomKnownTextOptions -> op.language
                else -> null
            }?.let { lang ->
                languageSelection.value = when (lang) {
                LanguageDefinition.English -> 0
                LanguageDefinition.German -> 1
            } }
            it.durationMillis.let { millis ->
                customDurationSelection.value = millis.div(60000f).toString()
                durationSelection.value = 2
            }
        }
    }

    fun generateTypingOptions(): TypingOptions {
        val seed = Random.nextLong()
        val language = when (languageSelection.value) {
            0 -> LanguageDefinition.English
            1 -> LanguageDefinition.German
            else -> throw RuntimeException("Invalid language option while selecting an exercise")
        }
        val duration = when (durationSelection.value) {
            0, 1 -> (durationSelection.value + 1L).times(60000L)
            else -> customDurationSelection.value.toDoubleOrNull()?.times(60000L) ?: 0L
        } // Min to millis
        return TypingOptions(
            generatorOptions = when (textModeSelection.value) {
                0 -> RandomKnownTextOptions(
                    seed = seed,
                    language = language
                )
                1 -> RandomKnownWordOptions(
                    seed = seed,
                    language = language,
                    minimalSegmentLength = 300
                )
                2 -> RandomCharOptions(
                    seed = seed,
                    segmentMinimumLength = 300,
                    wordLengthWeightMap = englishContentWordLengthDistribution,
                    characterWeightMap = englishAlphabetDistribution
                )
                else -> throw RuntimeException("Invalid option in text-mode while selecting an exercise")
            },
            durationMillis = duration.toLong(),
            exerciseMode = when (exerciseModeSelection.value) {
                0 -> ExerciseMode.Timelimit
                1 -> ExerciseMode.NoTimelimit
                else -> throw RuntimeException("Invalid option in exercise-mode while selecting an exercise")
            },
            isCameraEnabled = withFingerTracking.value,
            typingType = when (typingTypeSelection.value) {
                0 -> TypingType.MovingCursor
                1 -> TypingType.MovingText
                else -> throw RuntimeException("Invalid option in typing-type while selecting an exercise")
            }
        )
    }

    companion object {
        val textModeSelectionOptions = i18n.str.exercise.selection.textMode.getAll()
        val exerciseModeSelectionOptions = i18n.str.exercise.selection.exerciseMode.getAll()
        val typingTypeSelectionOptions = i18n.str.exercise.selection.typingType.getAll()
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
