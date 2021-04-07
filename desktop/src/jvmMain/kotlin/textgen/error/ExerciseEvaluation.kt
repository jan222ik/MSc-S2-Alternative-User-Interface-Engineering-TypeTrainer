package textgen.error

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseEvaluation(
    val texts: MutableList<TextEvaluation> = mutableListOf()
) {
    // Use @Transient if sth has a backing field and should not be serialized

    val totalCharsTyped: Int by lazy {
        texts.sumBy { it.chars.size }
    }

    val correctCharsTyped: Int by lazy {
        texts.sumBy { it.chars.filterIsInstance<CharEvaluation.Correct>().size }
    }

    val wordsTyped: Int by lazy {
        texts.sumBy { tEval ->
            tEval.chars.sumBy { cEval ->
                0.takeIf { cEval.getExpectedChar(tEval.text) != ' ' } ?: 1
            }
        }
    }

    val falseCharsTyped
        get() = totalCharsTyped - correctCharsTyped

    val totalErrors
        get() = falseCharsTyped + falseKeyStrokes

    val totalAccuracy
        get() = 1f - (totalErrors / totalCharsTyped.toFloat())

    val falseKeyStrokes: Int by lazy { 0 }
}

@Serializable
data class TextEvaluation(
    val chars: MutableList<CharEvaluation> = mutableListOf(),
    val text: String
)

@Serializable
sealed class CharEvaluation {
    abstract val timeRemaining: Long
    abstract val expected: Int

    @Serializable
    class Correct(
        override val timeRemaining: Long,
        override val expected: Int
    ) : CharEvaluation() {
        override fun toString(): String {
            return "CharEvaluation.Correct[time: $timeRemaining, expected: $expected]"
        }
    }

    /* TODO
    @Serializable
    sealed class Error() {
        //Typing
        //Finger
        //Combined
    }
     */

    @Serializable
    class TypingError(
        override val timeRemaining: Long,
        override val expected: Int,
        val actual: Char
    ) : CharEvaluation() {
        override fun toString(): String {
            return "CharEvaluation.TypingError[time: $timeRemaining, expected: $expected, actual: $actual]"
        }
    }

    fun getExpectedChar(text: String): Char {
        return text[expected]
    }
}
