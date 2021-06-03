package ui.exercise.practice

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DatabaseFactory
import textgen.database.DbHistory
import textgen.error.CharEvaluation
import textgen.error.ExerciseEvaluation
import textgen.error.TextEvaluation
import ui.exercise.AbstractTypingOptions
import util.RandomUtil
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.concurrent.fixedRateTimer

class MovingTextTypingIntend(
    typingOptions: AbstractTypingOptions
) : PracticeIntendImpl(typingOptions = typingOptions), ITextDisplayPracticeIntend {

    override val exerciseEvaluation = ExerciseEvaluation(options = typingOptions)
    lateinit var textEvaluation: TextEvaluation

    override val result: ExerciseEvaluation
        get() = exerciseEvaluation


    override fun update() {

    }

    private val _currentIsError = MutableStateFlow(false)
    override val currentIsError: StateFlow<Boolean>
        get() = _currentIsError

    private val _textTyped = MutableStateFlow("")
    override val textTyped: StateFlow<String>
        get() = _textTyped

    override val textTypedIndex: StateFlow<Int>
        get() = throw NotImplementedError()

    private val _textCurrent = MutableStateFlow("")
    override val textCurrent: StateFlow<String>
        get() = _textCurrent

    private val _textFuture = MutableStateFlow("")
    override val textFuture: StateFlow<String>
        get() = _textFuture



    var inTextIndex: Int = 1
    var currSpace = ""
    var endOfTextIndex = 0

    override suspend fun checkChar(char: Char) {
        val currentChar = textCurrent.value.last()
        println("Char: $char - Current Expected: $currentChar")
        if (char == currentChar) {
            _currentIsError.compareAndSet(expect = true, update = false)
            textEvaluation.chars.add(
                CharEvaluation.Correct(
                    timeRemaining = timerStateFlow.value,
                    expected = inTextIndex.dec()
                )
            )
            _textTyped.emit(_textTyped.value + currentChar)
            _textFuture.emit(_textFuture.value.removeRange(0, 1))
            _textCurrent.emit(currSpace + textStateFlow.value[inTextIndex])
            currSpace += " "
            inTextIndex++
            println("inTextIndex = $inTextIndex endOfTextIndex = $endOfTextIndex")
            if (inTextIndex == endOfTextIndex) {
                println("True")
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
        currSpace = ""
        endOfTextIndex = textStateFlow.value.length
        GlobalScope.launch {
            _currentIsError.emit(false)
            _textTyped.emit("")
            _textCurrent.emit(text.first().toString())
            _textFuture.emit(text.removeRange(0, 1))
        }
    }

    override fun onTimerFinished() {
        transaction {
            DbHistory.new {
                timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                dataJson = ExposedBlob(
                    Json{serializersModule = DatabaseFactory.serializer}
                        .encodeToString(exerciseEvaluation).toByteArray()
                )
            }
        }
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
                    } else textCurrent.value.last()
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
