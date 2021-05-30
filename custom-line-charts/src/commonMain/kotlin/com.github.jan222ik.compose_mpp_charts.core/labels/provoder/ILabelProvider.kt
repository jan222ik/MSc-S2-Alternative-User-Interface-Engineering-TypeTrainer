package com.github.jan222ik.compose_mpp_charts.core.labels.provoder

fun interface ILabelProvider {
    fun provide(min: Float, max: Float) : List<Pair<String, Float>>
}
