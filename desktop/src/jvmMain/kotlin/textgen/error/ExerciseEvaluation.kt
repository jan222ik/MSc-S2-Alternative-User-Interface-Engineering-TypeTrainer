package textgen.error

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseEvaluation(
    val texts: MutableList<TextEvaluation> = mutableListOf()
)

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
            return "CharEvaluation.Correct[time: $timeRemaining, expected: $expected]\n"
        }
    }

    @Serializable
    class TypingError(
        override val timeRemaining: Long,
        override val expected: Int,
        val actual: Char
    ) : CharEvaluation() {
        override fun toString(): String {
            return "CharEvaluation.TypingError[time: $timeRemaining, expected: $expected, actual: $actual]\n"
        }
    }

    fun getExpectedChar(text: String): Char {
        return text[expected]
    }
}
