package com.github.jan222ik.desktop.util

import kotlin.math.abs
import kotlin.random.Random

object RandomUtil {
    fun nextIntInRemBoundAsClosure(seed: Long, rem: Int): () -> Int {
        val rnd = Random(seed);
        return {
            abs(rnd.nextInt().rem(rem))
        }

    }
}

infix fun <A, B> A.weightedAt(that: B): Pair<A, B> = Pair(this, that)
