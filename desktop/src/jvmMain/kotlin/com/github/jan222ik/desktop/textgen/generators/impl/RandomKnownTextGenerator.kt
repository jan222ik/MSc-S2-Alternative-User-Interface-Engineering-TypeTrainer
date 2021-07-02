package com.github.jan222ik.desktop.textgen.generators.impl

import com.github.jan222ik.common.HasDoc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import com.github.jan222ik.desktop.textgen.database.schema.DbTextsEnglish
import com.github.jan222ik.desktop.textgen.database.schema.DbTextsGerman
import com.github.jan222ik.desktop.textgen.generators.AbstractGeneratorOptions
import com.github.jan222ik.desktop.textgen.generators.ContinuousGenerator
import com.github.jan222ik.desktop.textgen.generators.IGenerator
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import com.github.jan222ik.desktop.util.RandomUtil

@HasDoc
object RandomKnownTextGenerator : IGenerator<RandomKnownTextOptions> {

    private var fullText: String? = null
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
                if (fullText.isNullOrEmpty()) {
                    fullText = table.select {
                        (table.id eq randomNextText())
                    }.single()[table.content]
                }
                val displayText = StringBuilder()
                val remainingText = StringBuilder()
                var appendDisplay = true
                fullText!!.forEach {
                    if (appendDisplay && !(displayText.isEmpty() && it == ' ')) {
                        displayText.append(it)
                    }
                    if (!appendDisplay) {
                        remainingText.append(it)
                    }
                    if (displayText.length > 650 && it == '.') {
                        appendDisplay = false
                    }

                }
                if (!firstExec) {
                    fullText = remainingText.toString()
                } else {
                    firstExec = false
                }
                return@transaction displayText.toString()
            }
        }
    }

}

@Serializable
@SerialName("RandomKnownTextOptions")
@HasDoc
data class RandomKnownTextOptions(
    override val seed: Long,
    val language: LanguageDefinition
) : AbstractGeneratorOptions()
