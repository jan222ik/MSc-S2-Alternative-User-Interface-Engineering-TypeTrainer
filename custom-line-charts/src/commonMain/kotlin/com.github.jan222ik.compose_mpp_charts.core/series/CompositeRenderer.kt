package com.github.jan222ik.compose_mpp_charts.core.series

fun compositeRenderer(vararg renderers: ISeriesRenderer) = ISeriesRenderer { series ->
    renderers.map { with(it) { render(series) } }.flatten()
}
