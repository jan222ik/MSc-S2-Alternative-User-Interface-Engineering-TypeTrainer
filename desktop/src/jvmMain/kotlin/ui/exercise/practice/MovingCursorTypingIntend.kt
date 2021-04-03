package ui.exercise.practice

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import ui.exercise.ITypingOptions
import kotlin.concurrent.fixedRateTimer

class MovingCursorTypingIntend(
    typingOptions: ITypingOptions
) : PracticeIntendImpl(typingOptions = typingOptions), ITextDisplayPracticeIntend {


    override fun update() {

    }

    private val _currentIsError = MutableStateFlow(false)
    override val currentIsError: StateFlow<Boolean>
        get() = _currentIsError

    private val _textTyped = MutableStateFlow("")
    override val textTyped: StateFlow<String>
        get() = _textTyped

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
            _textTyped.emit(_textTyped.value + currentChar)
            _textFuture.emit(" " + _textFuture.value.removeRange(0, 1))
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
        }
    }

    override fun onNextText() {
        inTextIndex = 1
        currSpace = ""
        endOfTextIndex = textStateFlow.value.length
        GlobalScope.launch {
            _currentIsError.emit(false)
            _textTyped.emit("")
            val text = textStateFlow.value
            _textCurrent.emit(text.first().toString())
            _textFuture.emit(text.removeRange(0, 1))
        }
    }

    override suspend fun startConstantSpeedTypeDemo(period: Long) {
        GlobalScope.launch(Dispatchers.IO) {
            var count = 0
            fixedRateTimer(
                name = "ConstantSpeedTypeDemo",
                period = period
            ) {
                if (typingClockStateStateFlow.value == IPracticeIntend.TypingClockState.ACTIVE) {
                    count++
                    val char = if (count == 8) {
                        count = 0
                        '\r'
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

}
