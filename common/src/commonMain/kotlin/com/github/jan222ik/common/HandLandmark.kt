package com.github.jan222ik.common

class HandLandmark {
    val fingerLandmarks: MutableMap<FingerEnum, FingerTipLandmark> = mutableMapOf()

    override fun toString(): String {
        return "HandLandmark(fingerLandmarks=$fingerLandmarks)"
    }
}
