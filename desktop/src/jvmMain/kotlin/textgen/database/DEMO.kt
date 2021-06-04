package textgen.database

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.error.CharEvaluation
import textgen.error.ExerciseEvaluation
import textgen.error.TextEvaluation
import textgen.generators.impl.RandomKnownWordOptions
import ui.exercise.ExerciseMode
import ui.exercise.TypingOptions
import ui.exercise.TypingType
import ui.util.i18n.LanguageDefinition
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object DEMO {
    val demoOptions = TypingOptions(
        generatorOptions = RandomKnownWordOptions(
            seed = 1L,
            language = LanguageDefinition.English,
            minimalSegmentLength = 450
        ),
        durationMillis = 60_000,
        exerciseMode = ExerciseMode.Accuracy,
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
                    DbHistory.new {
                        val today = LocalDate.now()
                        timestamp = LocalDateTime.of(today.minusDays(i.toLong()), LocalTime.MIDNIGHT)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
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
