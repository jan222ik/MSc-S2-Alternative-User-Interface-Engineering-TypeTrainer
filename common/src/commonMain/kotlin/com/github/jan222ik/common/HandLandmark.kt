package com.github.jan222ik.common

import kotlinx.serialization.Serializable

@Serializable
class HandLandmark {
    val fingerLandmarks: MutableMap<FingerEnum, FingerTipLandmark> = mutableMapOf()

    override fun toString(): String {
        return "HandLandmark(fingerLandmarks=$fingerLandmarks)"
    }
}
