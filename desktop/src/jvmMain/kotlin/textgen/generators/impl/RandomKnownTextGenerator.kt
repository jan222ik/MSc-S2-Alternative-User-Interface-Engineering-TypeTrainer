package textgen.generators.impl

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import textgen.database.DbTextsEnglish
import textgen.database.DbTextsGerman
import textgen.generators.ContinuousGenerator
import textgen.generators.IGenerator
import textgen.generators.IGeneratorOptions
import ui.util.i18n.LanguageDefinition
import util.RandomUtil

object RandomKnownTextGenerator : IGenerator<RandomKnownTextGenerator.RandomKnownTextOptions> {

    private var fullText : String? = null
    private var firstExec = true

    override fun create(options: RandomKnownTextOptions): ContinuousGenerator {
        val table = when (options.language) {
            LanguageDefinition.English -> DbTextsEnglish
            LanguageDefinition.German -> DbTextsGerman
        }
        val entries = transaction { table.selectAll().count() }
        val randomNextText = RandomUtil.nextIntInRemBoundAsClosure(options.seed, entries.toInt())
        return ContinuousGenerator {
            return@ContinuousGenerator transaction {
                if (fullText.isNullOrEmpty()){
                    fullText = table.select {
                        (table.id eq randomNextText())
                    }.single()[table.content]
                }
                val displayText = StringBuilder()
                val remainingText = StringBuilder()
                var appendDisplay = true
                fullText!!.forEach {
                    if (appendDisplay && !(displayText.isEmpty() && it == ' ') ){
                        displayText.append(it)
                    }
                    if(!appendDisplay){
                        remainingText.append(it)
                    }
                    if(displayText.length > 650 && it == '.'){
                        appendDisplay = false
                    }

                }
                if(!firstExec) {
                    fullText = remainingText.toString()
                }
                else{
                    firstExec = false
                }
                return@transaction displayText.toString()
            }
        }
    }

    data class RandomKnownTextOptions(
        val seed: Long,
        val language: LanguageDefinition
    ) : IGeneratorOptions
}
