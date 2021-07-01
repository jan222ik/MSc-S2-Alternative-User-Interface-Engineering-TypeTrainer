package com.github.jan222ik.desktop.textgen.database

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.util.CSVFieldNumDifferentException
import com.github.jan222ik.common.HasDoc
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.exists
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import com.github.jan222ik.desktop.textgen.database.schema.DbTexts
import com.github.jan222ik.desktop.textgen.database.schema.DbTextsEnglish
import com.github.jan222ik.desktop.textgen.database.schema.DbTextsGerman
import com.github.jan222ik.desktop.textgen.error.ExerciseEvaluation
import com.github.jan222ik.desktop.textgen.generators.AbstractGeneratorOptions
import com.github.jan222ik.desktop.textgen.generators.impl.RandomCharOptions
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownTextOptions
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordOptions
import com.github.jan222ik.desktop.ui.exercise.AbstractTypingOptions
import com.github.jan222ik.desktop.ui.exercise.ExerciseMode
import com.github.jan222ik.desktop.ui.exercise.TypingOptions
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset
import java.nio.file.Paths
import javax.imageio.ImageIO

@HasDoc
object DatabaseFactory {
    val serializer: SerializersModule = SerializersModule {
        polymorphic(baseClass = AbstractTypingOptions::class) {
            subclass(
                subclass = TypingOptions::class,
                serializer = TypingOptions.serializer()
            )
        }
        polymorphic(baseClass = AbstractGeneratorOptions::class) {
            subclass(
                subclass = RandomKnownWordOptions::class,
                serializer = RandomKnownWordOptions.serializer()
            )
            subclass(
                subclass = RandomCharOptions::class,
                serializer = RandomCharOptions.serializer()
            )
            subclass(
                subclass = RandomKnownTextOptions::class,
                serializer = RandomKnownTextOptions.serializer()
            )
        }
        polymorphic(baseClass = LanguageDefinition::class) {
            subclass(
                subclass = LanguageDefinition.English::class,
                serializer = LanguageDefinition.English.serializer()
            )
            subclass(
                subclass = LanguageDefinition.German::class,
                serializer = LanguageDefinition.German.serializer()
            )
        }
        polymorphic(baseClass = ExerciseMode::class) {
            subclass(
                subclass = ExerciseMode.Timelimit::class,
                serializer = ExerciseMode.Timelimit.serializer()
            )
            subclass(
                subclass = ExerciseMode.NoTimelimit::class,
                serializer = ExerciseMode.NoTimelimit.serializer()
            )
        }
    }

    var dataSource: HikariDataSource? = null

    fun init() {
        dataSource = hikari()
        Database.connect(dataSource!!)
        transaction {
            SchemaUtils.drop(DbHistorys, DbTextsEnglish, DbTextsGerman)
            SchemaUtils.create(DbHistorys)
            SchemaUtils.create(DbTextsEnglish)
            SchemaUtils.create(DbTextsGerman)

            val fileEng = File("desktop/src/jvmMain/resources/literature_eng.csv")
            readTextsFromFile(file = fileEng, table = DbTextsEnglish)
            val fileGer = File("desktop/src/jvmMain/resources/literature_ger.csv")
            readTextsFromFile(file = fileGer, table = DbTextsGerman)
        }
    }

    fun start() {
        dataSource = hikari()
        Database.connect(dataSource!!)
        val missingTables = transaction {
            listOf(DbHistorys, DbTextsEnglish, DbTextsGerman).any { !it.exists() }
        }
        if (missingTables) {
            println("Generating Tables")
            transaction {
                SchemaUtils.drop(DbHistorys, DbTextsEnglish, DbTextsGerman)
                SchemaUtils.create(DbHistorys)
                SchemaUtils.create(DbTextsEnglish)
                SchemaUtils.create(DbTextsGerman)

                try {
                    this::class.java.getResourceAsStream("/literature_eng.csv")
                        ?.let { readTextsFromStream(inStream = it, table = DbTextsEnglish) }
                    this::class.java.getResourceAsStream("/literature_ger.csv")
                        ?.let { readTextsFromStream(inStream = it, table = DbTextsGerman) }
                    println("Load success!")
                } catch (_: Throwable) {
                    val spEng = "src/jvmMain/resources/literature_eng.csv"
                    val spGer = "src/jvmMain/resources/literature_ger.csv"
                    val fileEng = File("desktop/$spEng").takeIf { it.exists() } ?: File(spEng)
                    val fileGer = File("desktop/$spGer").takeIf { it.exists() } ?: File(spGer)

                    println("Eng exists: " + fileEng.exists() + " " + fileEng.absolutePath)
                    println("Ger exists: " + fileGer.exists() + " " + fileEng.absolutePath)

                    readTextsFromFile(file = fileEng, table = DbTextsEnglish)
                    readTextsFromFile(file = fileGer, table = DbTextsGerman)
                }
            }
        }
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

    private fun <T> readTextsFromStream(
        inStream: InputStream,
        table: T,
        delimiter: Char = ';',
        escapeChar: Char = '\\'
    ) where T : Table, T : DbTexts {
        try {
            csvReader {
                this.delimiter = delimiter
                this.escapeChar = escapeChar
            }.open(inStream) {
                readAllWithHeaderAsSequence().forEach { csv ->
                    val toString = csv["Content"].toString()
                    table.insert {
                        it[content] = toString
                    }
                }
            }
        } catch (e: CSVFieldNumDifferentException) {
            e.printStackTrace()
        }
    }

    private fun <T> readTextsFromFile(
        file: File,
        table: T,
        delimiter: Char = ';',
        escapeChar: Char = '\\'
    ) where T : Table, T : DbTexts {
        require(file.exists()) { "The provided file does not exit." }
        require(file.isFile) { "The provided file is not a file." }
        try {
            csvReader {
                this.delimiter = delimiter
                this.escapeChar = escapeChar
            }.open(file) {
                readAllWithHeaderAsSequence().forEach { csv ->
                    //println("csv = ${csv}")
                    val toString = csv["Content"].toString().let {
                        it
                        //    .replace(regex = Regex("^$\r\n"), replacement = "")
                        //    .replace(regex = Regex("\r\n"), replacement = " ")
                    }

                    //if (toString.length <= 400) {
                    table.insert {
                        it[content] = toString
                    }
                    //}
                }
            }
        } catch (e: CSVFieldNumDifferentException) {
            e.printStackTrace()
        }
    }

}

fun main() {
    //DatabaseFactory.initWithDemoData()
    val json = Json() {
        this.serializersModule = DatabaseFactory.serializer
    }.encodeToString(value = DEMO.demoData)
    println("json = ${json}")
    Json() {
        this.serializersModule = DatabaseFactory.serializer
    }.decodeFromString<ExerciseEvaluation>(json).also { println("it = ${it}") }

    /*
    transaction {
        DbHistory.new {
            timestamp = LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            dataJson = ExposedBlob(Json.encodeToString(DatabaseFactory.demoData).toByteArray())
        }
        DbHistory.all().forEach {
            println(it)
            println(it.timestampDate.value)
            println(it.data.value)
        }
    }
     */
}
