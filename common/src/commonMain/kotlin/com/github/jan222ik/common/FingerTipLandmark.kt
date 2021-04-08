package com.github.jan222ik.common

class FingerTipLandmark(
    val finger: FingerEnum,
    val x: Float,
    val y: Float,
    val z: Float
) {
    override fun toString(): String {
        return "FingerTipLandmark(finger=$finger, x=$x, y=$y, z=$z)"
    }
}

enum class FingerEnum {
    NONE, INDEX, MIDDLE, RING, PINKY, THUMB
}
