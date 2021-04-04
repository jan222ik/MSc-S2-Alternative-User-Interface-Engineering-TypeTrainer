package textgen.error

data class ExerciseEvaluation(
    val texts: MutableList<TextEvaluation> = mutableListOf()
)

data class TextEvaluation(
    val chars: MutableList<CharEvaluation> = mutableListOf()
)

sealed class CharEvaluation(
    val timeRemaining: Long,
    val expected: Int
) {
    class Correct(
        timeRemaining: Long,
        expected: Int
    ) : CharEvaluation(timeRemaining = timeRemaining, expected = expected) {
        override fun toString(): String {
            return "CharEvaluation.Correct[time: $timeRemaining, expected: $expected]\n"
        }
    }

    class TypingError(
        timeRemaining: Long,
        expected: Int,
        val actual: Char
    ) : CharEvaluation(timeRemaining = timeRemaining, expected = expected) {
        override fun toString(): String {
            return "CharEvaluation.TypingError[time: $timeRemaining, expected: $expected, actual: $actual]\n"
        }
    }
}
