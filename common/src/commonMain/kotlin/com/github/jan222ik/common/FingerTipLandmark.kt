package com.github.jan222ik.common

import kotlinx.serialization.Serializable

@HasDoc
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

}
