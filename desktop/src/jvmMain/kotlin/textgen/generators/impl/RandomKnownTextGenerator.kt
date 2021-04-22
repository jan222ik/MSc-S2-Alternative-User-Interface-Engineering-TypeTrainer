package textgen.generators.impl

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DatabaseFactory
import textgen.database.DbTextsEnglish
import textgen.database.DbTextsGerman
import textgen.generators.ContinuousGenerator
import textgen.generators.IGenerator
import textgen.generators.IGeneratorOptions
import ui.util.i18n.LanguageDefinition
import util.RandomUtil

object RandomKnownTextGenerator : IGenerator<RandomKnownTextGenerator.RandomKnownTextOptions> {
    override fun create(options: RandomKnownTextOptions): ContinuousGenerator {
        val table = when (options.language) {
            LanguageDefinition.English -> DbTextsEnglish
            LanguageDefinition.German -> DbTextsGerman
        }
        val entries = transaction { table.selectAll().count() }
        val randomNextText = RandomUtil.nextIntInRemBoundAsClosure(options.seed, entries.toInt())
        return ContinuousGenerator {
            return@ContinuousGenerator transaction {
                return@transaction table.select {
                    (table.id eq randomNextText())
                }.single()[table.content]
            }
        }
    }

    data class RandomKnownTextOptions(
        val seed: Long,
        val language: LanguageDefinition
    ) : IGeneratorOptions
}
