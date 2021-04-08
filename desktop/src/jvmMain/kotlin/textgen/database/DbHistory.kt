package textgen.database

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import textgen.error.ExerciseEvaluation
import java.time.LocalDateTime

class DbHistory(
    val histId: EntityID<Int>
) : IntEntity(histId) {
    companion object : IntEntityClass<DbHistory>(DbHistorys)

    var timestamp: String by DbHistorys.timestamp
    var dataJson: ExposedBlob by DbHistorys.dataJson

    @Transient
    val timestampDate = lazy { LocalDateTime.parse(timestamp) }

    @Transient
    val data = lazy { Json.decodeFromString<ExerciseEvaluation>(String(dataJson.bytes, Charsets.UTF_8)) }
}