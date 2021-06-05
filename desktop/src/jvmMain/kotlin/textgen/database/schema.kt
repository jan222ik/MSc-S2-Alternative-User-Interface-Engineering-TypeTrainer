package textgen.database

import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.`java-time`.datetime
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDate
import java.time.LocalDateTime


object DbHistorys : IntIdTable() {
    val histId = integer("histId").autoIncrement()
    val timestamp: Column<LocalDateTime> = datetime("timestamp")
    val dataJson = blob("dataJson")

    override val primaryKey = PrimaryKey(histId)
}

fun DbHistorys.getWeekly(): List<DbHistory> {
    val now = LocalDateTime.now()
    val lastWeek = LocalDate.now().minusDays(8).atStartOfDay()
    return transaction {
        val select: Query = DbHistorys.select { (timestamp lessEq now) and (timestamp greaterEq lastWeek) }
        select.map {
            return@map DbHistory(
                histId = EntityID(it[histId], DbHistorys),
                timestamp = it[timestamp],
                dataJson = it[dataJson]
            )
        }
    }
}
