package com.github.jan222ik.common.dto

import com.github.jan222ik.compose_mpp_charts.core.data.DataPoint
import kotlinx.serialization.Serializable

const val SHARED_STATS_PREF_KEY = "weeklyJson"

@Serializable
data class PointDTO(val x: Float, val y: Float) {
    fun toDataPoint(): DataPoint = DataPoint(x, y)
}

fun DataPoint.toPointDTO(): PointDTO = PointDTO(x, y)

@Serializable
data class MobileStatsData(
    val weeklyDataPoints: List<PointDTO>,
    val totalExercises: Int,
    val streak: Int
)
