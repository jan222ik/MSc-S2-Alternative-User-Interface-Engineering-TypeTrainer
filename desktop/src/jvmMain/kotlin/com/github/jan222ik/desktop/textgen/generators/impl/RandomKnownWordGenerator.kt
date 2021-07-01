package com.github.jan222ik.desktop.textgen.generators.impl

import com.github.jan222ik.common.HasDoc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.github.jan222ik.desktop.textgen.generators.ContinuousGenerator
import com.github.jan222ik.desktop.textgen.generators.IGenerator
import com.github.jan222ik.desktop.textgen.generators.AbstractGeneratorOptions
import com.github.jan222ik.desktop.ui.util.i18n.LanguageDefinition
import com.github.jan222ik.desktop.util.RandomUtil
import java.io.InputStream
import java.nio.charset.Charset

@HasDoc
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
@HasDoc
data class RandomKnownWordOptions(
    override val seed: Long,
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
