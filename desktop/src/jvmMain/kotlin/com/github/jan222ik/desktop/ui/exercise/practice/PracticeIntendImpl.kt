package com.github.jan222ik.desktop.ui.exercise.practice

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import com.github.jan222ik.desktop.UXTest
import com.github.jan222ik.desktop.textgen.database.ExportExcelUtil
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import com.github.jan222ik.desktop.textgen.generators.ContinuousGenerator
import com.github.jan222ik.desktop.textgen.generators.impl.RandomCharGenerator
import com.github.jan222ik.desktop.textgen.generators.impl.RandomCharOptions
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownTextGenerator
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownTextOptions
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordGenerator
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordOptions
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.ui.exercise.ExerciseMode
import com.github.jan222ik.desktop.util.FingerMatcher
import com.github.jan222ik.desktop.util.FingerUsed
import java.io.File
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit


abstract class PracticeIntendImpl @OptIn(ExperimentalAnimationApi::class) constructor(
    final override val typingOptions: AbstractTypingOptions,
    private val fingerMatcher: FingerMatcher?,
    val testRun: UXTest.TestRun?
) : IPracticeIntend, IDebugPracticeIntend {
    private val generator: ContinuousGenerator
    private var typingClockJob: Job? = null

    init {
        val generatorOptions = typingOptions.generatorOptions
        generator = when (generatorOptions) {
            is RandomKnownWordOptions -> RandomKnownWordGenerator.create(generatorOptions)
            is RandomKnownTextOptions -> RandomKnownTextGenerator.create(generatorOptions)
            is RandomCharOptions -> RandomCharGenerator.create(generatorOptions)
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

    private val _typingClockStateStateFlow = MutableStateFlow(IPracticeIntend.TypingClockState.PREVIEW)
    override val typingClockStateStateFlow: StateFlow<IPracticeIntend.TypingClockState>
        get() = _typingClockStateStateFlow

    private val _timerUpdateCycleCountStateFlow = MutableStateFlow(0L)
    override val timerUpdateCycleCountStateFlow: StateFlow<Long>
        get() = _timerUpdateCycleCountStateFlow


    private val isDebug = System.getProperty("debug") == "true"

    abstract fun onTimerFinished()

    fun writeRun2Workbook(exerciseEvaluation: ExerciseEvaluation) {
        testRun?.workbook?.let { work ->
            ExportExcelUtil.run2Sheet(
                workbook = work,
                localDateTime = LocalDateTime.now().minus(typingOptions.durationMillis, ChronoUnit.MILLIS),
                exerciseEvaluation = exerciseEvaluation
            )
            ExportExcelUtil.save(file = testRun.fileNameGen, workbook = work)
        }
    }

    override fun start() {
        if (typingClockJob != null) return // Return if job is already assigned, could be invoked multiple times from UI.
        var i = 0L
        _startTimeMillis = System.currentTimeMillis()
        typingClockJob = GlobalScope.launch(Dispatchers.IO) {
            _typingClockStateStateFlow.emit(IPracticeIntend.TypingClockState.ACTIVE)
            while (typingClockJob?.isActive == true && timerUpdateCycle()) { /* Nothing to do here */
                if (isDebug) {
                    i += 1L
                    update()
                    _timerUpdateCycleCountStateFlow.emit(i)
                }
            }
            _typingClockStateStateFlow.emit(IPracticeIntend.TypingClockState.FINISHED)
            onTimerFinished()
            println("Iterations $i")
        }
    }

    private suspend fun timerUpdateCycle(): Boolean {
        val timeDiff = System.currentTimeMillis() - _startTimeMillis
        val remTimeMillis = typingOptions.durationMillis - timeDiff
        _timerStateFlow.emit(remTimeMillis)
        return remTimeMillis > 0
    }

    abstract fun onNextText()

    var firstText = 0

    override suspend fun nextText() {
        if (firstText == 0 || typingOptions.exerciseMode == ExerciseMode.Timelimit) {
            val generateSegment = generator.generateSegment()
            _textStateFlow.value = generateSegment
            onNextText()
            firstText = 1
        } else {
            if (firstText == 1) {
                _typingClockStateStateFlow.emit(IPracticeIntend.TypingClockState.FINISHED)
                typingClockJob?.cancelAndJoin()
                //onTimerFinished()
                firstText = 2
            }
        }
    }

    abstract fun update()

    override fun cancelRunningJobs() {
        typingClockJob?.cancel()
    }

    override fun modifyRemainingTimeByAmount(amountMs: Long) {
        if (amountMs != 0L) {
            _startTimeMillis += amountMs
            _timeSkip.value = true
        }
    }


    private val _timeSkip = mutableStateOf(false)
    override val timeSkip: State<Boolean>
        get() = _timeSkip

    override val _isCameraEnabled = mutableStateOf(typingOptions.isCameraEnabled)
    override val isCameraEnabled: State<Boolean>
        get() = _isCameraEnabled

    override suspend fun forceNextText() {
        _textStateFlow.emit(generator.generateSegment())
    }

    override val hasFingerTracking: Boolean
        get() = fingerMatcher != null

    override fun checkFingerForChar(char: String): FingerUsed? {
        return fingerMatcher?.matchFingerOverKey(char)
    }
}
