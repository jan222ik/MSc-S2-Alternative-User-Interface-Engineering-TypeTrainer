package com.github.jan222ik.compose_mpp_charts.core.labels.provoder

@OptIn(ExperimentalUnsignedTypes::class)
fun intLabelProvider(step: UInt = 1u) = ILabelProvider { min: Float, max: Float ->
    require(max > min) { "Max should be larger than min. min: $min, max: $max" }
    return@ILabelProvider (min.toInt()..(max.toInt() + step.toInt())).map { "$it" to it.toFloat() }
}
