package com.github.jan222ik.common

import androidx.compose.ui.geometry.Offset
import kotlinx.serialization.Serializable

@Serializable
class FingerTipLandmark(
    val finger: FingerEnum,
    val x: Float,
    val y: Float,
    val z: Float
) {
    override fun toString(): String {
        return "FingerTipLandmark(finger=$finger, x=$x, y=$y, z=$z)"
    }


    fun toKeyBoardRef(topLeft: Offset): FingerTipLandmark {
       return FingerTipLandmark(
           finger = finger,
           x = x.plus(topLeft.x),
           y = y.plus(topLeft.y),
           z = z
       )
    }
}

@Serializable
enum class FingerEnum {
    NONE, INDEX, MIDDLE, RING, PINKY, THUMB
}
