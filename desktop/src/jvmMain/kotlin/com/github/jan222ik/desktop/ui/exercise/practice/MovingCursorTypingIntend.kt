package com.github.jan222ik.desktop.ui.exercise.practice

import androidx.compose.animation.ExperimentalAnimationApi
import com.github.jan222ik.desktop.UXTest
import com.github.jan222ik.desktop.textgen.database.DatabaseFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import com.github.jan222ik.desktop.textgen.database.DbHistoryDAO
import com.github.jan222ik.desktop.textgen.database.ExportExcelUtil
import com.github.jan222ik.desktop.textgen.error.CharEvaluation
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import com.github.jan222ik.desktop.textgen.error.TextEvaluation
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.ui.exercise.ExerciseMode
import com.github.jan222ik.desktop.util.FingerMatcher
import com.github.jan222ik.desktop.util.RandomUtil
import org.apache.poi.ss.usermodel.Workbook
import java.time.LocalDateTime
import kotlin.concurrent.fixedRateTimer

class MovingCursorTypingIntend @OptIn(ExperimentalAnimationApi::class) constructor(
    typingOptions: AbstractTypingOptions,
    fingerMatcher: FingerMatcher?,
    testRun: UXTest.TestRun?
) : PracticeIntendImpl(
    typingOptions = typingOptions,
    fingerMatcher = fingerMatcher,
    testRun = testRun,
), ITextDisplayPracticeIntend {

    override val exerciseEvaluation = ExerciseEvaluation(options = typingOptions)
    lateinit var textEvaluation: TextEvaluation

    override val result: ExerciseEvaluation
        get() = when (typingOptions.exerciseMode) {
            ExerciseMode.NoTimelimit -> exerciseEvaluation
                .copy(
                    texts = exerciseEvaluation.texts.map { text ->
                        text.copy(chars =
                            text.chars.map {
                                it.copy(timeRemaining = Long.MAX_VALUE - it.timeRemaining)
                            }.toMutableList()
                        )
                    }.toMutableList(),
                    options = exerciseEvaluation.options.copyOptions(durationMillis = Long.MAX_VALUE - timerStateFlow.value)
                )
            ExerciseMode.Timelimit -> exerciseEvaluation
        }


    override fun update() {

    }

    private val _currentIsError = MutableStateFlow(false)
    override val currentIsError: StateFlow<Boolean>
        get() = _currentIsError

    private val _textTypedIndex = MutableStateFlow(0)
    override val textTypedIndex: StateFlow<Int>
        get() = _textTypedIndex

    override val textTyped: StateFlow<String>
        get() = throw NotImplementedError()

    override val textCurrent: StateFlow<String>
        get() = throw NotImplementedError()

    override val textFuture: StateFlow<String>
        get() = throw NotImplementedError()



    var inTextIndex: Int = 1
    var endOfTextIndex = 0

    override suspend fun checkChar(char: Char) {
        val currentChar = textStateFlow.value[inTextIndex]
        println("Char: $char - Current Expected: $currentChar")
        if (char == currentChar) {
            _currentIsError.compareAndSet(expect = true, update = false)
            val res = if (hasFingerTracking) {
                val checkFingerForChar = checkFingerForChar(char = char.toString())
                if (checkFingerForChar == null) {
                    CharEvaluation.Correct(
                        timeRemaining = timerStateFlow.value,
                        expected = inTextIndex.dec()
                    )
                } else {
                    CharEvaluation.FingerError(
                        timeRemaining = timerStateFlow.value,
                        expected = inTextIndex.dec(),
                        fingerUsed = checkFingerForChar
                    )
                }
            } else {
                CharEvaluation.Correct(
                    timeRemaining = timerStateFlow.value,
                    expected = inTextIndex.dec()
                )
            }
            textEvaluation.chars.add(res)
            inTextIndex++
            _textTypedIndex.emit(inTextIndex)
            println("inTextIndex = $inTextIndex endOfTextIndex = $endOfTextIndex")
            if (inTextIndex == endOfTextIndex) {
                println("End of text reached!")
                nextText()
            }
        } else {
            _currentIsError.compareAndSet(expect = false, update = true)
            textEvaluation.chars.add(
                CharEvaluation.TypingError(
                    timeRemaining = timerStateFlow.value,
                    expected = inTextIndex.dec(),
                    actual = char
                )
            )
        }
    }

    override fun onNextText() {
        val text = textStateFlow.value
        textEvaluation = TextEvaluation(text = text)
        exerciseEvaluation.texts.add(textEvaluation)
        inTextIndex = 1
        endOfTextIndex = textStateFlow.value.length
        GlobalScope.launch {
            _currentIsError.emit(false)
            _textTypedIndex.emit(0)
        }
    }

    override fun onTimerFinished() {
        val eval = when (typingOptions.exerciseMode) {
            ExerciseMode.NoTimelimit -> exerciseEvaluation
                .copy(
                    options = exerciseEvaluation.options.copyOptions(durationMillis = Long.MAX_VALUE - timerStateFlow.value)
                )
            ExerciseMode.Timelimit -> exerciseEvaluation
        }
        transaction {
            DbHistoryDAO.new {
                timestamp = LocalDateTime.now()
                dataJson = ExposedBlob(
                    Json{serializersModule = DatabaseFactory.serializer}
                        .encodeToString(eval)
                        .toByteArray()
                )
            }
        }
        writeRun2Workbook(eval)
    }

    override suspend fun startConstantSpeedTypeDemo(period: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            var count = 0
            val rand = RandomUtil.nextIntInRemBoundAsClosure(1L, 8)
            var nextErrAt = 4 + rand()
            fixedRateTimer(
                name = "ConstantSpeedTypeDemo",
                period = period
            ) {
                if (typingClockStateStateFlow.value == IPracticeIntend.TypingClockState.ACTIVE) {
                    count++
                    val char = if (count == nextErrAt) {
                        count = 0
                        nextErrAt = 4 + rand()
                        'π'
                    } else textStateFlow.value[inTextIndex]
                    GlobalScope.launch(Dispatchers.IO) {
                        checkChar(char)
                    }
                } else {
                    this.cancel()
                }
            }
        }
    }

    init {
        // Displays the first text
        GlobalScope.launch(Dispatchers.IO) {
            nextText()
        }
    }

}
