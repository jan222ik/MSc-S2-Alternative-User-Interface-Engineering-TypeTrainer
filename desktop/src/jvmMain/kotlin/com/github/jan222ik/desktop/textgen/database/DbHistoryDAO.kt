package com.github.jan222ik.desktop.textgen.database

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import java.time.LocalDateTime

class DbHistoryDAO(
    val histId: EntityID<Int>
) : IntEntity(histId) {
    companion object : IntEntityClass<DbHistoryDAO>(DbHistorys)

    var timestamp: LocalDateTime by DbHistorys.timestamp
    var dataJson: ExposedBlob by DbHistorys.dataJson

    fun toModel(): DbHistory {
        return DbHistory(histId = histId, timestamp = timestamp, dataJson = dataJson)
    }
}
