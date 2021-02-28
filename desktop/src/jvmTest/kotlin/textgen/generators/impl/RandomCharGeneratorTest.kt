package textgen.generators.impl

import org.junit.jupiter.api.Test
import util.weightedAt

import org.junit.jupiter.api.Assertions.assertEquals

class RandomCharGeneratorTest {

    @Test
    fun repeatedInvocationProducesSameResult() {
        val options = RandomCharGenerator.RandomCharOptions(
            seed = 1L,
            segmentMinimumLength = 10,
            wordLengthWeightMap = mapOf(
                1 weightedAt 1,
                2 weightedAt 3,
                3 weightedAt 2,
                4 weightedAt 5,
                5 weightedAt 7,
                6 weightedAt 5,
                7 weightedAt 3
            ),
            characterWeightMap = mapOf(
                'a' weightedAt 15,
                'b' weightedAt 9,
                'c' weightedAt 5,
                'd' weightedAt 1,
                'x' weightedAt 70
            )
        )
        val result = RandomCharGenerator.create(options = options).generateSegment()
        repeat(50) {
            val otherResult = RandomCharGenerator.create(options = options).generateSegment()
            assertEquals(result, otherResult)
        }
    }

    @Test
    fun stableGenerationWithDifferntMinimumSegmentLength() {
        val options = RandomCharGenerator.RandomCharOptions(
            seed = 1L,
            segmentMinimumLength = 20,
            wordLengthWeightMap = mapOf(
                1 weightedAt 1,
                2 weightedAt 3,
                3 weightedAt 2,
                4 weightedAt 5,
                5 weightedAt 7,
                6 weightedAt 5,
                7 weightedAt 3
            ),
            characterWeightMap = mapOf(
                'a' weightedAt 15,
                'b' weightedAt 9,
                'c' weightedAt 5,
                'd' weightedAt 1,
                'x' weightedAt 70
            )
        )
        val result = RandomCharGenerator.create(options = options).generateSegment()

        val otherResult = RandomCharGenerator.create(options = options.copy(segmentMinimumLength = 10)).let {
            it.generateSegment() + it.generateSegment()
        }

        assertEquals(result, otherResult)

    }


}

fun main() {
    RandomCharGeneratorTest().apply {
        println("Test 1")
        repeatedInvocationProducesSameResult()
        println("Test 2")
        stableGenerationWithDifferntMinimumSegmentLength()
    }
}
