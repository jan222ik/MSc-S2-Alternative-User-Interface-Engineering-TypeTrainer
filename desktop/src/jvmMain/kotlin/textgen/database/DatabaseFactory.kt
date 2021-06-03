package textgen.database

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.util.CSVFieldNumDifferentException
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.statements.api.ExposedBlob
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.error.CharEvaluation
import textgen.error.ExerciseEvaluation
import textgen.error.TextEvaluation
import textgen.generators.AbstractGeneratorOptions
import textgen.generators.impl.RandomCharOptions
import textgen.generators.impl.RandomKnownTextOptions
import textgen.generators.impl.RandomKnownWordOptions
import ui.exercise.AbstractTypingOptions
import ui.exercise.ExerciseMode
import ui.exercise.TypingOptions
import ui.util.i18n.LanguageDefinition
import java.io.File
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.random.Random

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
                subclass = ExerciseMode.Accuracy::class,
                serializer = ExerciseMode.Accuracy.serializer()
            )
            subclass(
                subclass = ExerciseMode.Speed::class,
                serializer = ExerciseMode.Speed.serializer()
            )
            subclass(
                subclass = ExerciseMode.NoTimelimit::class,
                serializer = ExerciseMode.NoTimelimit.serializer()
            )
        }
    }

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


            for (i in 0..45) {
                if (Random.nextInt(100) < 25) {
                    DbHistory.new {
                        val today = LocalDate.now()
                        timestamp = LocalDateTime.of(today.minusDays(i.toLong()), LocalTime.MIDNIGHT)
                            .format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        dataJson =
                            ExposedBlob(
                                Json() {
                                    serializersModule = serializer
                                }.encodeToString(value = demoData)
                                    .toByteArray()
                            )
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

    val demoOptions = TypingOptions(
        generatorOptions = RandomKnownWordOptions(
            seed = 1L,
            language = LanguageDefinition.English,
            minimalSegmentLength = 450
        ),
        durationMillis = 60_000,
        exerciseMode = ExerciseMode.Accuracy,
        isCameraEnabled = false
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
}

fun main() {
    //DatabaseFactory.initWithDemoData()
    val json = Json() {
        this.serializersModule = DatabaseFactory.serializer
    }.encodeToString(value = DatabaseFactory.demoData)
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
