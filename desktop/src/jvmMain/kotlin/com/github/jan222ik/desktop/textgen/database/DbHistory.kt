package com.github.jan222ik.desktop.textgen.database

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import java.time.LocalDate
import java.time.LocalDateTime


data class DbHistory(
    val histId: EntityID<Int>,
    val timestamp: LocalDateTime,
    val dataJson: ExposedBlob
) {
    val timestampDate: LocalDate by lazy { timestamp.toLocalDate() }

    @Transient
    val data = lazy {
        Json { serializersModule = DatabaseFactory.serializer }
            .decodeFromString<ExerciseEvaluation>(String(dataJson.bytes, Charsets.UTF_8))
    }
}
