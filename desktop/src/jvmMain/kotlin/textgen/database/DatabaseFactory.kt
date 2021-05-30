package textgen.database

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.util.CSVFieldNumDifferentException
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.error.CharEvaluation
import textgen.error.ExerciseEvaluation
import textgen.error.TextEvaluation
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

object DatabaseFactory {
    var dataSource: HikariDataSource? = null

    fun initWithDemoData() {
        dataSource = hikari()
        Database.connect(dataSource!!)
        transaction {
            SchemaUtils.drop(DbHistorys, DbTextsEnglish, DbTextsGerman)
            SchemaUtils.create(DbHistorys)
            SchemaUtils.create(DbTextsEnglish)
            SchemaUtils.create(DbTextsGerman)

            var file = File("desktop/src/jvmMain/resources/literature_eng.csv")
            try {
                val csvReader = csvReader {
                    delimiter = ';'
                    escapeChar = '\\'
                }
                csvReader.open(file) {
                    readAllWithHeaderAsSequence().forEach { csv ->
                        println("csv = ${csv}")
                        var toString = csv["Content"].toString()

//                        toString = toString.replace(regex = Regex("^$\r\n"), replacement = "")
//                        toString = toString.replace(regex = Regex("\r\n"), replacement = " ")

                        if (toString.length <= 400 || true) {
                            DbTextsEnglish.insert {
                                it[content] = toString
                            }
                        }
                    }
                }
            } catch (e: CSVFieldNumDifferentException) {
                e.printStackTrace()
            }

            file = File("desktop/src/jvmMain/resources/literature_ger.csv")
            try {
                val csvReader = csvReader {
                    delimiter = ';'
                    escapeChar = '\\'
                }
                csvReader.open(file) {
                    readAllWithHeaderAsSequence().forEach { csv ->
                        println("csv = ${csv}")
                        var toString = csv["Content"].toString()

//                        toString = toString.replace(regex = Regex("^$\r\n"), replacement = "")
//                        toString = toString.replace(regex = Regex("\r\n"), replacement = " ")

                        if (toString.length <= 400 || true) {
                            DbTextsGerman.insert {
                                it[content] = toString
                            }
                        }
                    }
                }
            } catch (e: CSVFieldNumDifferentException) {
                e.printStackTrace()
            }


            for(i in 0..45){
                if(Random.nextInt(100) < 25){
                    DbHistory.new {
                        val today = LocalDate.now()
                        timestamp = LocalDateTime.of(today.minusDays(i.toLong()), LocalTime.MIDNIGHT)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        dataJson = ExposedBlob("{}".toByteArray())
                    }
                }
            }
        }
    }

    fun start() {
        dataSource = hikari()
        Database.connect(dataSource!!)
    }

    fun stop() {
        dataSource?.close()
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
