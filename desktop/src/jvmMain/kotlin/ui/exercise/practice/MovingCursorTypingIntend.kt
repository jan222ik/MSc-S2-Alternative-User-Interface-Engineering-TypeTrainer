package ui.exercise.practice

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import ui.exercise.ITypingOptions

class MovingCursorTypingIntend(
    typingOptions: ITypingOptions
) : PracticeIntendImpl(typingOptions = typingOptions), ITextDisplayPracticeIntend {
    private val _charChannel: Channel<Char> = Channel { println("Undelivered: $it") }
    private val charChannel: ReceiveChannel<Char>
        get() = _charChannel

    override fun update() {

    }

    private val _currentIsError = MutableStateFlow(false)
    override val currentIsError: StateFlow<Boolean>
        get() = _currentIsError

    private val _textTyped = MutableStateFlow("")
    override val textTyped: StateFlow<String>
        get() = _textTyped

    private val _textCurrent = MutableStateFlow(textStateFlow.value.first().toString())
    override val textCurrent: StateFlow<String>
        get() = _textCurrent

    private val _textFuture = MutableStateFlow(textStateFlow.value.removeRange(0,1))
    override val textFuture: StateFlow<String>
        get() = _textFuture

    override suspend fun addCharToQueue(char: Char) {
        _charChannel.send(char)
    }

    var inTextIndex: Int = 1
    var currSpace = ""

    override suspend fun checkChar(char: Char) {
        val currentChar = textCurrent.value.last()
        println("Char: $char - Current Expected: $currentChar")
        if (char == currentChar) {
            _currentIsError.compareAndSet(expect = true, update = false)
            _textTyped.emit(_textTyped.value + currentChar)
            _textFuture.emit( " " + _textFuture.value.removeRange(0,1))
            _textCurrent.emit(currSpace + textStateFlow.value[inTextIndex])
            currSpace += " "
            inTextIndex++
        } else {
            _currentIsError.compareAndSet(expect = false, update = true)
        }
    }



}
