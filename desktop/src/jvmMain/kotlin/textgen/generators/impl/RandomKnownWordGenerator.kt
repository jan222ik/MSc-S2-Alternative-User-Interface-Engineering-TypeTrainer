package textgen.generators.impl

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import textgen.generators.ContinuousGenerator
import textgen.generators.IGenerator
import textgen.generators.AbstractGeneratorOptions
import ui.util.i18n.LanguageDefinition
import util.RandomUtil
import java.io.InputStream
import java.nio.charset.Charset

object RandomKnownWordGenerator : IGenerator<RandomKnownWordOptions> {

    private fun InputStream.readLinesAndClose(charset: Charset = Charsets.UTF_8): List<String> {
        return this.bufferedReader(charset).use { it.readLines() }
    }

    private fun String.getResourceAsFile(): InputStream? {
        return RandomKnownWordGenerator::class.java.classLoader.getResourceAsStream(this)
    }
    val s = "desktop/src/jvmMain/resources/eng.txt"
    private val eng: List<String> by lazy { "eng.txt".getResourceAsFile()!!.readLinesAndClose() }
    private val ger: List<String> by lazy { "ger2.txt".getResourceAsFile()!!.readLinesAndClose() }

    override fun create(options: RandomKnownWordOptions): ContinuousGenerator {
        val (seed, delimiter, language, minSegLen) = options
        val list = eng.takeIf { language == LanguageDefinition.English } ?: ger
        val randomNextCharInt = RandomUtil.nextIntInRemBoundAsClosure(seed, list.size)
        return ContinuousGenerator {
            var rollingLength = 0
            var str = ""
            while (rollingLength < minSegLen) {
                val word = list[randomNextCharInt.invoke()]
                rollingLength+=word.length
                str += delimiter + word
            }
            return@ContinuousGenerator str
        }
    }
}

@Serializable
@SerialName("RandomKnownWordOptions")
data class RandomKnownWordOptions(
    val seed: Long,
    val delimiter: String = " ",
    val language: LanguageDefinition,
    val minimalSegmentLength: Int
): AbstractGeneratorOptions()



fun main() {
    val gen0 = RandomKnownWordGenerator.create(
        options = RandomKnownWordOptions(
            seed = 2L,
            language = LanguageDefinition.German,
            minimalSegmentLength = 100
        )
    )
    println(gen0.generateSegment())
}
