package textgen.error

import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import ui.exercise.AbstractTypingOptions

@Serializable
data class ExerciseEvaluation(
    val texts: MutableList<TextEvaluation> = mutableListOf(),
    val options: AbstractTypingOptions
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

    val wps: Float by lazy {
        wordsTyped / options.durationMillis.div(1000f)
    }

    val timesBetweenKeyStrokes: List<DataPoint> by lazy {
        val times = texts.map { it.chars.map { c -> c.timeRemaining } }.flatten()
        var prev: Long = options.durationMillis
        times.mapIndexed { idx, time ->
            val tween = prev - time
            prev = time
            DataPoint(x = idx.toFloat(), y = tween.toFloat())
        }
    }

    val longestTimeTweenStrokes: Float? by lazy {
        timesBetweenKeyStrokes.maxByOrNull { it.y }?.y
    }

    val shortestTimeTweenStrokes: Float? by lazy {
        timesBetweenKeyStrokes.minByOrNull { it.y.takeUnless { it == 0f } ?: Float.MAX_VALUE  }?.y
    }

    val averageTimeTweenStrokes: Float? by lazy {
        timesBetweenKeyStrokes
            .filter { it.y != 0f }
            .let {
                it.sumByDouble { item -> item.y.toDouble() }.div(it.size).toFloat()
            }
    }

    val errorRateOfKeys: List<ChartErrorKey> by lazy {
        texts
            .flatMap { txt ->
                txt.chars
                    .filterIsInstance<CharEvaluation.TypingError>()
                    .onEach { it.getExpectedChar(txt.text) }
            }
            .let { errors ->
                errors
                    .groupBy { it.expectedChar }
                    .entries
                    .sortedByDescending { it.value.size }
                    .mapIndexed{index: Int, entry: Map.Entry<Char?, List<CharEvaluation.TypingError>> ->
                        ChartErrorKey(
                            idx = index,
                            char = entry.key.toString(),
                            amount = entry.value.size,
                            sum = errors.size
                        )
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
data class ChartErrorKey(
    val idx: Int,
    val char: String,
    val amount: Int,
    val sum: Int
) {
    @Transient
    val dataPoint = DataPoint(idx.toFloat(), amount.toFloat().div(sum.toFloat()).times(100f))
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

    @Transient
    var expectedChar: Char? = null

    fun getExpectedChar(text: String): Char {
        val c = text[expected]
        expectedChar = c
        return c
    }
}
