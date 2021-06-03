package textgen.database

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

interface DbTexts {
    val id: Column<Int>
    val content: Column<String>
}

object DbTextsEnglish : Table(), DbTexts {
    override val id = integer("id").autoIncrement()
    override val content = varchar("content", 50000)

    override val primaryKey = PrimaryKey(id)
}

object DbTextsGerman : Table(), DbTexts {
    override val id = integer("id").autoIncrement()
    override val content = varchar("content", 50000)

    override val primaryKey = PrimaryKey(id)
}


object DbHistorys : IntIdTable() {
    val histId = integer("histId").autoIncrement()
    val timestamp = varchar("timestamp", 45)
    val dataJson = blob("dataJson")
    //val optionsJson = blob("optionsJson")

    override val primaryKey = PrimaryKey(histId)
}
