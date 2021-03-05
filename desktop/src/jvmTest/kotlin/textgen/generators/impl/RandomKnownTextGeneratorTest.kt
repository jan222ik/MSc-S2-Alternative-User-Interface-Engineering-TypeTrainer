package textgen.generators.impl

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import textgen.database.DatabaseFactory
import ui.util.i18n.LanguageDefinition

internal class RandomKnownTextGeneratorTest {

    @Test
    fun stableRngEnglish() {
        val gen0 = RandomKnownTextGenerator.create(
            options = RandomKnownTextGenerator.RandomKnownTextOptions(
                seed = 1L,
                language = LanguageDefinition.English
            )
        )
        val gen1 = RandomKnownTextGenerator.create(
            options = RandomKnownTextGenerator.RandomKnownTextOptions(
                seed = 1L,
                language = LanguageDefinition.English
            )
        )
        repeat(50) {
            assertEquals(gen0.generateSegment(), gen1.generateSegment())
        }
    }

    @Test
    fun stableRngGerman() {
        val gen0 = RandomKnownTextGenerator.create(
            options = RandomKnownTextGenerator.RandomKnownTextOptions(
                seed = 1L,
                language = LanguageDefinition.German
            )
        )
        val gen1 = RandomKnownTextGenerator.create(
            options = RandomKnownTextGenerator.RandomKnownTextOptions(
                seed = 1L,
                language = LanguageDefinition.German
            )
        )
        repeat(50) {
            assertEquals(gen0.generateSegment(), gen1.generateSegment())
        }
    }
}

fun main() {
    DatabaseFactory.initWithDemoData()
    RandomKnownTextGeneratorTest().apply {
        println("Test 1")
        stableRngEnglish()
        println("Test 2")
        stableRngGerman()
    }
}
