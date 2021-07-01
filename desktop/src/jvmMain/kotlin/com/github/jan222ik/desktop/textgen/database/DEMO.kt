package com.github.jan222ik.desktop.textgen.database

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import com.github.jan222ik.desktop.textgen.error.CharEvaluation
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import com.github.jan222ik.desktop.textgen.error.TextEvaluation
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordOptions
import com.github.jan222ik.desktop.ui.exercise.ExerciseMode
import com.github.jan222ik.desktop.ui.exercise.TypingOptions
import com.github.jan222ik.desktop.ui.exercise.TypingType
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import kotlin.random.Random

object DEMO {
    val demoOptions = TypingOptions(
        generatorOptions = RandomKnownWordOptions(
            seed = 1L,
            language = LanguageDefinition.English,
            minimalSegmentLength = 450
        ),
        durationMillis = 60_000,
        exerciseMode = ExerciseMode.Timelimit,
        isCameraEnabled = false,
        typingType = TypingType.MovingCursor
    )

    val demoData = ExerciseEvaluation(
        options = demoOptions
    ).apply {
        texts.add(
            TextEvaluation(text = "a".repeat(200)).apply {
                chars.add(CharEvaluation.TypingError(60_000, 0, 'b'))
                chars.add(CharEvaluation.TypingError(59_530, 0, 'b'))
                chars.add(CharEvaluation.TypingError(59_103, 0, 'b'))
                chars.add(CharEvaluation.TypingError(58_810, 0, 'b'))
            }
        )
        texts.add(
            TextEvaluation(text = "b".repeat(200)).apply {
                chars.add(CharEvaluation.TypingError(58_401, 0, 'c'))
                chars.add(CharEvaluation.TypingError(57_993, 0, 'c'))
                chars.add(CharEvaluation.TypingError(57_333, 0, 'c'))
                chars.add(CharEvaluation.TypingError(56_931, 0, 'c'))
            }
        )
    }

    fun demoTrainingEntries() {
        transaction {
            for (i in 0..45) {
                if (Random.nextInt(100) < 25) {
                    DbHistoryDAO.new {
                        val today = LocalDate.now()
                        timestamp = LocalDateTime.of(today.minusDays(i.toLong()), LocalTime.MIDNIGHT)
                        dataJson =
                            ExposedBlob(
                                Json() {
                                    serializersModule = DatabaseFactory.serializer
                                }.encodeToString(value = demoData)
                                    .toByteArray()
                            )
                    }
                }
            }
        }
    }
}
