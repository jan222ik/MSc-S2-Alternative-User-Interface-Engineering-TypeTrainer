package com.github.jan222ik.desktop.textgen.error

import com.github.jan222ik.common.FingerEnum
import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.util.FingerUsed

@Serializable
data class ExerciseEvaluation(
    val texts: MutableList<TextEvaluation> = mutableListOf(),
    val options: AbstractTypingOptions
) {
    // Use @Transient if sth has a backing field and should not be serialized

    val falseCharsTypedCase: Int by lazy {
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
                    .map { entry: Map.Entry<Char?, List<CharEvaluation.TypingError>> ->
                        entry.value.count {
                            entry.key?.equals(it.actual, ignoreCase = true) ?: false
                        }
                    }
            }.sum()
    }

    val falseCharsTypedWhitespace: Int by lazy {
        texts
            .flatMap { txt ->
                txt.chars
                    .filterIsInstance<CharEvaluation.TypingError>()
                    .onEach { it.getExpectedChar(txt.text) }
            }.count { it.expectedChar?.isWhitespace() ?: false }
    }

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

    val cps: Float by lazy {
        totalCharsTyped / options.durationMillis.div(1000f)
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
        timesBetweenKeyStrokes.minByOrNull { it.y.takeUnless { it == 0f } ?: Float.MAX_VALUE }?.y
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
                    .mapIndexed { index: Int, entry: Map.Entry<Char?, List<CharEvaluation.TypingError>> ->
                        ChartErrorKey(
                            idx = index,
                            char = entry.key.toString(),
                            amount = entry.value.size,
                            sum = errors.size
                        )
                    }
            }

    }

    val errorRateOfFingers: List<FingerErrorKey> by lazy {
        texts
            .flatMap { txt ->
                txt.chars
                    .filterIsInstance<CharEvaluation.FingerError>()
                    .onEach { it.getExpectedChar(txt.text) }
            }
            .let { errors ->
                errors
                    .groupBy { it.fingerUsed?.expected }
                    .entries
                    .sortedByDescending { it.value.size }
                    .mapIndexed { index: Int, entry: Map.Entry<FingerEnum?, List<CharEvaluation.FingerError>> ->
                        FingerErrorKey(
                            idx = index,
                            amount = entry.value.size,
                            sum = errors.size,
                            finger = entry.key
                        )
                    }
            }
    }

    val falseCharsTyped
        get() = totalCharsTyped - correctCharsTyped

    val totalErrors
        get() = falseCharsTyped + falseKeyFingerStrokes

    val totalAccuracy
        get() = 1f - (totalErrors / totalCharsTyped.toFloat())

    val falseKeyFingerStrokes: Int by lazy {
        texts.sumBy { it.chars.filterIsInstance<CharEvaluation.FingerError>().size }
    }
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
data class FingerErrorKey(
    val idx: Int,
    val amount: Int,
    val sum: Int,
    @Contextual
    val finger: FingerEnum?,
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
        override fun copy(timeRemaining: Long): CharEvaluation = Correct(timeRemaining, expected)

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
        override fun copy(timeRemaining: Long) = TypingError(timeRemaining, expected, actual)


        override fun toString(): String {
            return "CharEvaluation.TypingError[time: $timeRemaining, expected: $expected, actual: $actual]"
        }
    }

    @Serializable
    class FingerError(
        override val timeRemaining: Long,
        override val expected: Int,
        val fingerUsed: FingerUsed? = null
    ) : CharEvaluation() {
        override fun copy(timeRemaining: Long) = FingerError(timeRemaining, expected, fingerUsed)


        override fun toString(): String {
            return "CharEvaluation.FingerError[time: $timeRemaining, expected: $expected, fingerUsed: $fingerUsed]"
        }
    }

    @Transient
    var expectedChar: Char? = null

    fun getExpectedChar(text: String): Char {
        val c = text[expected]
        expectedChar = c
        return c
    }

    abstract fun copy(timeRemaining: Long): CharEvaluation
}
