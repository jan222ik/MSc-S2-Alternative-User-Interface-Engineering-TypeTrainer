package textgen.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun initWithDemoData() {
        Database.connect(hikari())
        transaction {
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
            jdbcUrl = "jdbc:h2:mem:test"
            maximumPoolSize = 3
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }
        return HikariDataSource(config)
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction { block.invoke() }
}
