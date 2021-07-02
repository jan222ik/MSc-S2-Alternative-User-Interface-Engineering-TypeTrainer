package com.github.jan222ik.android

import androidx.lifecycle.LifecycleCoroutineScope
import com.github.jan222ik.android.network.WSClient
import com.github.jan222ik.common.FingerEnum
import com.github.jan222ik.common.FingerTipLandmark
import com.github.jan222ik.common.HandLandmark
import com.github.jan222ik.common.HasDoc
import com.google.mediapipe.formats.proto.LandmarkProto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

@HasDoc
class FingerTipExtractor {
    companion object {
        private const val THUMB_TIP = 4
        private const val INDEX_TIP = 8
        private const val MIDDLE_TIP = 12
        private const val RING_TIP = 16
        private const val PINKY_TIP = 20

        var lifecycle: LifecycleCoroutineScope? = null
            set(value) {
                field = value
                WSClient.connect(value!!)
            }

        @OptIn(ExperimentalCoroutinesApi::class)
        fun extractAndSend(multiHandLandmarks: MutableList<LandmarkProto.NormalizedLandmarkList>) {
            if (multiHandLandmarks.isEmpty()) return

            val hands = mutableListOf<HandLandmark>()

            for ((handIndex, landmarks) in multiHandLandmarks.withIndex()) {
                val hand = HandLandmark()
                for ((landmarkIndex, landmark) in landmarks.landmarkList.withIndex()) {
                    val fingerTip = when (landmarkIndex) {
                        THUMB_TIP -> FingerTipLandmark(FingerEnum.THUMB, landmark.x, landmark.y, landmark.z)
                        INDEX_TIP -> FingerTipLandmark(FingerEnum.INDEX, landmark.x, landmark.y, landmark.z)
                        MIDDLE_TIP -> FingerTipLandmark(FingerEnum.MIDDLE, landmark.x, landmark.y, landmark.z)
                        RING_TIP -> FingerTipLandmark(FingerEnum.RING, landmark.x, landmark.y, landmark.z)
                        PINKY_TIP -> FingerTipLandmark(FingerEnum.PINKY, landmark.x, landmark.y, landmark.z)
                        else -> FingerTipLandmark(FingerEnum.NONE, 0f, 0f, 0f)
                    }

                    if (fingerTip.finger != FingerEnum.NONE)
                        hand.fingerLandmarks[fingerTip.finger] = fingerTip
                }
                hands.add(hand)
            }

            lifecycle!!.launch (Dispatchers.IO) {
                WSClient.landmarks.emit(hands)
            }
        }
    }
}
