package textgen.generators.impl

import textgen.generators.ContinuousGenerator
import textgen.generators.IGenerator
import textgen.generators.IGeneratorOptions
import ui.util.i18n.LanguageDefinition
import util.RandomUtil
import java.io.File

object RandomKnownWordGenerator : IGenerator<RandomKnownWordGenerator.RandomKnownWordOptions> {

    private val eng: List<String> by lazy { File("desktop/src/jvmMain/resources/eng.txt").readLines() }
    private val ger: List<String> by lazy { File("desktop/src/jvmMain/resources/ger2.txt").readLines() }

    data class RandomKnownWordOptions(
        val seed: Long,
        val delimiter: String = " ",
        val language: LanguageDefinition,
        val minimalSegmentLength: Int
    ): IGeneratorOptions

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
