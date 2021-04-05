package textgen.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.error.CharEvaluation
import textgen.error.ExerciseEvaluation
import textgen.error.TextEvaluation
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object DatabaseFactory {
    fun initWithDemoData() {
        Database.connect(hikari())
        transaction {
            SchemaUtils.create(DbHistorys)
            SchemaUtils.create(DbTextsEnglish)
            SchemaUtils.create(DbTextsGerman)
            for (i in 0 until 100) {
                DbTextsEnglish.insert {
                    it[content] = "ENG" + i.toString().repeat(15)
                }
                DbTextsGerman.insert {
                    it[content] = "GER" + i.toString().repeat(15)
                }
            }
        }
    }

    private fun hikari(): HikariDataSource {
        val config = HikariConfig().apply {
            driverClassName = "org.h2.Driver"
            //jdbcUrl = "jdbc:h2:mem:test"
            jdbcUrl = "jdbc:h2:./db"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block.invoke() }
}


fun main() {
    DatabaseFactory.initWithDemoData()
    val exercise = ExerciseEvaluation().apply {
        texts.add(
            TextEvaluation(text = "a".repeat(200)).apply {
                chars.add(CharEvaluation.TypingError(2000, 0, 'b'))
                chars.add(CharEvaluation.TypingError(1900, 0, 'b'))
                chars.add(CharEvaluation.TypingError(1700, 0, 'b'))
                chars.add(CharEvaluation.TypingError(900, 0, 'b'))
            }
        )
        texts.add(
            TextEvaluation(text = "b".repeat(200)).apply {
                chars.add(CharEvaluation.TypingError(3000, 0, 'c'))
                chars.add(CharEvaluation.TypingError(2900, 0, 'c'))
                chars.add(CharEvaluation.TypingError(1700, 0, 'c'))
                chars.add(CharEvaluation.TypingError(900, 0, 'c'))
            }
        )
    }
    transaction {
        DbHistory.new {
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            dataJson = ExposedBlob(Json.encodeToString(exercise).toByteArray())
        }
        DbHistory.all().forEach {
            println(it)
            println(it.timestampDate.value)
            println(it.data.value)
        }
    }
}
