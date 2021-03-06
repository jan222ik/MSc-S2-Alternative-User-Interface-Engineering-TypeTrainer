package ui.exercise.practice

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import textgen.generators.ContinuousGenerator
import textgen.generators.impl.RandomCharGenerator
import textgen.generators.impl.RandomKnownTextGenerator
import textgen.generators.impl.RandomKnownWordGenerator
import ui.exercise.ITypingOptions

interface IPracticeIntend {
    val typingOptions: ITypingOptions
    val textStateFlow: StateFlow<String>
    val timerStateFlow: StateFlow<Long>
    val typingClockStateStateFlow: StateFlow<TypingClockState>
    val timerUpdateCycleCountStateFlow: StateFlow<Long>

    fun start()
    fun cancelRunningJobs()
}

interface IDebugPracticeIntend : IPracticeIntend {
    fun modifyRemainingTimeByAmount(amountMs: Long)
    val timeSkip: State<Boolean>
}

class PracticeIntendImpl(
    override val typingOptions: ITypingOptions
) : IPracticeIntend, IDebugPracticeIntend {
    private val generator: ContinuousGenerator
    private var typingClockJob: Job? = null

    init {
        val generatorOptions = typingOptions.generatorOptions
        generator = when (generatorOptions) {
            is RandomKnownWordGenerator.RandomKnownWordOptions -> RandomKnownWordGenerator.create(generatorOptions)
            is RandomKnownTextGenerator.RandomKnownTextOptions -> RandomKnownTextGenerator.create(generatorOptions)
            is RandomCharGenerator.RandomCharOptions -> RandomCharGenerator.create(generatorOptions)
            else -> throw IllegalStateException("The given Generator Options are not recognized. A Implementation might be missing.")
        }
    }

    private val _textStateFlow = MutableStateFlow(generator.generateSegment())
    override val textStateFlow: StateFlow<String>
        get() = _textStateFlow

    private var _startTimeMillis = 0L
    private val _timerStateFlow = MutableStateFlow(typingOptions.durationMillis)
    override val timerStateFlow: StateFlow<Long>
        get() = _timerStateFlow

    private val _typingClockStateStateFlow = MutableStateFlow(TypingClockState.PREVIEW)
    override val typingClockStateStateFlow: StateFlow<TypingClockState>
        get() = _typingClockStateStateFlow

    private val _timerUpdateCycleCountStateFlow = MutableStateFlow(0L)
    override val timerUpdateCycleCountStateFlow: StateFlow<Long>
        get() = _timerUpdateCycleCountStateFlow


    val isDebug = System.getProperty("debug") == "true"

    override fun start() {
        if (typingClockJob != null) return // Return if job is already assigned, could be invoked multiple times from UI.
        var i: Long = 0L
        _startTimeMillis = System.currentTimeMillis()
        typingClockJob = GlobalScope.launch(Dispatchers.IO) {
            _typingClockStateStateFlow.emit(TypingClockState.ACTIVE)
            while (typingClockJob?.isActive == true && timerUpdateCycle()) { /* Nothing to do here */
                if (isDebug) {
                    i += 1L
                    _timerUpdateCycleCountStateFlow.emit(i)
                }
            }
            _typingClockStateStateFlow.emit(TypingClockState.FINISHED)
            println("Iterations $i")
        }
    }

    private suspend fun timerUpdateCycle(): Boolean {
        val timeDiff = System.currentTimeMillis() - _startTimeMillis
        val remTimeMillis = typingOptions.durationMillis - timeDiff
        _timerStateFlow.emit(remTimeMillis)
        return remTimeMillis > 0
    }

    override fun cancelRunningJobs() {
        typingClockJob?.cancel()
    }

    override fun modifyRemainingTimeByAmount(amountMs: Long) {
        if (amountMs != 0L) {
            _startTimeMillis += amountMs
            _timeSkip.value = true
        }
    }


    val _timeSkip =  mutableStateOf(false)
    override val timeSkip: State<Boolean>
        get() = _timeSkip

}

enum class TypingClockState {
    PREVIEW, ACTIVE, FINISHED
}
