package textgen.generators.impl

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.github.jan222ik.desktop.textgen.database.DEMO
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownTextGenerator
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownTextOptions
import textgen.database.DatabaseFactory
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import kotlin.test.BeforeTest

internal class RandomKnownTextGeneratorTest {

    @BeforeTest
    fun beforeEach() {
        DatabaseFactory.init()
        DEMO.demoTrainingEntries()
    }

    @Test
    fun stableRngEnglish() {
        val gen0 = RandomKnownTextGenerator.create(
            options = RandomKnownTextOptions(
                seed = 1L,
                language = LanguageDefinition.English
            )
        )
        val gen1 = RandomKnownTextGenerator.create(
            options = RandomKnownTextOptions(
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
            options = RandomKnownTextOptions(
                seed = 1L,
                language = LanguageDefinition.German
            )
        )
        val gen1 = RandomKnownTextGenerator.create(
            options = RandomKnownTextOptions(
                seed = 1L,
                language = LanguageDefinition.German
            )
        )
        repeat(50) {
            assertEquals(gen0.generateSegment(), gen1.generateSegment())
        }
    }
}
