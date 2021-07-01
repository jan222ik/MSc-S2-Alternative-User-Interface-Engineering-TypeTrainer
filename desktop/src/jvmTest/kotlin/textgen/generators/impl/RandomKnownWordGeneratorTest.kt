package textgen.generators.impl

import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordGenerator
import com.github.jan222ik.desktop.textgen.generators.impl.RandomKnownWordOptions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition

internal class RandomKnownWordGeneratorTest {

    @Test
    fun stableRngEnglish() {
        val gen0 = RandomKnownWordGenerator.create(
            options = RandomKnownWordOptions(
                seed = 1L,
                language = LanguageDefinition.English,
                minimalSegmentLength = 100
            )
        )
        val gen1 = RandomKnownWordGenerator.create(
            options = RandomKnownWordOptions(
                seed = 1L,
                language = LanguageDefinition.English,
                minimalSegmentLength = 100
            )
        )
        repeat(50) {
            assertEquals(gen0.generateSegment(), gen1.generateSegment())
        }
    }

    @Test
    fun stableRngGerman() {
        val gen0 = RandomKnownWordGenerator.create(
            options = RandomKnownWordOptions(
                seed = 2L,
                language = LanguageDefinition.German,
                minimalSegmentLength = 100
            )
        )
        val gen1 = RandomKnownWordGenerator.create(
            options = RandomKnownWordOptions(
                seed = 2L,
                language = LanguageDefinition.German,
                minimalSegmentLength = 100
            )
        )
        repeat(50) {
            assertEquals(gen0.generateSegment(), gen1.generateSegment())
        }
    }

}
