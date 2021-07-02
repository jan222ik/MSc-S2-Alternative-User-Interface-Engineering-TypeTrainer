package com.github.jan222ik.desktop.textgen.generators.impl

import com.github.jan222ik.common.HasDoc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.github.jan222ik.desktop.textgen.generators.ContinuousGenerator
import com.github.jan222ik.desktop.textgen.generators.IGenerator
import com.github.jan222ik.desktop.textgen.generators.AbstractGeneratorOptions
import com.github.jan222ik.desktop.util.RandomUtil

@HasDoc
object RandomCharGenerator : IGenerator<RandomCharOptions> {

    override fun create(options: RandomCharOptions): ContinuousGenerator {
        val (seed, delimiter, segmentMinimumLength, wordLengthWeightMap, characterWeightMap) = options
        val (charsWeightSum, charsList) = characterWeightMap.toWeightedListWithSum()
        val (wordLengthWeightSum, wordLengthList) = wordLengthWeightMap.toWeightedListWithSum()

        val randomNextCharInt = RandomUtil.nextIntInRemBoundAsClosure(seed, charsWeightSum)
        val randomNextWordLenInt = RandomUtil.nextIntInRemBoundAsClosure(seed, wordLengthWeightSum)

        return ContinuousGenerator {
            val wordsOfLength = mutableListOf<Int>()
            var usedLength = 0
            while (usedLength < segmentMinimumLength) {
                val nextInt = randomNextWordLenInt()
                val wordLength = wordLengthList.find { nextInt <= it.first }!!.second
                wordsOfLength.add(wordLength)
                usedLength += wordLength
            }

            // Create Words
            val words = wordsOfLength.map { wordLen ->
                val contentString = IntRange(0, wordLen.dec()).map {
                    val nextInt = randomNextCharInt()
                    charsList.find { nextInt <= it.first }!!.second
                }.fold("") { acc, c -> acc + c }
                contentString
            }

            // Combine Words
            words.fold("") { acc, c -> acc + delimiter + c}
        }
    }

    private fun <T> Map<T, Int>.toWeightedListWithSum(): Pair<Int, List<Pair<Int, T>>> {
        var weightSum = 0
        val weightedList = this.map {
            weightSum += it.value
            return@map Pair(weightSum, it.key)
        }.toList()
        return Pair(weightSum, weightedList)
    }

}

@Serializable
@SerialName("RandomCharOptions")
@HasDoc
data class RandomCharOptions(
    override val seed: Long,
    val delimiter: String = " ",
    val segmentMinimumLength: Int,
    val wordLengthWeightMap: Map<Int, Int>,
    val characterWeightMap: Map<Char, Int>
) : AbstractGeneratorOptions()
