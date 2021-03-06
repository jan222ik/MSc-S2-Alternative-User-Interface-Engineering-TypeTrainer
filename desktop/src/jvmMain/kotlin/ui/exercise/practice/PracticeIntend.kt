package ui.exercise.practice

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

class PracticeIntend(
    val typingOptions: ITypingOptions
) {
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
    val textStateFlow: StateFlow<String>
        get() = _textStateFlow

    private var _startTimeMillis = 0L
    private val _timerStateFlow = MutableStateFlow(typingOptions.durationMillis)
    val timerStateFlow: StateFlow<Long>
        get() = _timerStateFlow

    private val _typingClockStateStateFlow = MutableStateFlow(TypingClockState.PREVIEW)
    val typingClockStateStateFlow: StateFlow<TypingClockState>
        get() = _typingClockStateStateFlow

    fun start() {
        if (typingClockJob != null) return // Return if job is already assigned, could be invoked multiple times from UI.
        var i: Long = 0L
        _startTimeMillis = System.currentTimeMillis()
        typingClockJob = GlobalScope.launch(Dispatchers.IO) {
            _typingClockStateStateFlow.emit(TypingClockState.ACTIVE)
            while (typingClockJob?.isActive == true && timerUpdateCycle()) { /* Nothing to do here */ i += 1L
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

    fun cancelRunningJobs() {
        typingClockJob?.cancel()
    }

}

enum class TypingClockState {
    PREVIEW, ACTIVE, FINISHED
}
