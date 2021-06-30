package util

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type
import com.github.jan222ik.common.FingerEnum
import com.github.jan222ik.common.FingerTipLandmark
import com.github.jan222ik.common.HandLandmark
import com.github.jan222ik.common.HasDoc
import kotlinx.coroutines.channels.ReceiveChannel

@HasDoc
class FingerMatcher(
    private val fingerMap: Map<String, FingerEnum> = KeyboardUtil.fingerMap,
    private val keyboard: List<List<RelativeKey>> = KeyboardUtil.getRelativeKeyboard(),
    private val channel: ReceiveChannel<List<HandLandmark>>
) {
    private val mulSyncStep = mutableStateOf(0)
    val syncStep: State<Int>
        get() = mulSyncStep

    private val escPoints = mutableListOf<Offset>()
    var topLeft: Offset? = null
    var bottomRight: Offset? = null

    var roiSize: Size? = null

    fun isInROI(offset: Offset): Boolean {
        topLeft ?: return false
        roiSize ?: return false
        return ((topLeft!!.x)..(topLeft!!.x.plus(roiSize!!.width))).contains(offset.x)
                && ((topLeft!!.y)..(topLeft!!.y.plus(roiSize!!.height))).contains(offset.y)
    }

    fun syncInput(evt: KeyEvent) {
        if (evt.type == KeyEventType.KeyDown) {
            val nativeKeyEvt = evt.nativeKeyEvent
            when {
                // Right Control
                nativeKeyEvt.keyCode == 17 && nativeKeyEvt.keyLocation == java.awt.event.KeyEvent.KEY_LOCATION_RIGHT -> {
                    if (mulSyncStep.value == 1) {
                        topLeft = escPoints.minByOrNull { it.x }
                        if (topLeft == null) {
                            mulSyncStep.value = 1
                        }
                        val poll = channel.poll()
                        if (poll != null) {

                            poll
                                .mapNotNull {
                                    it.fingerLandmarks[FingerEnum.PINKY]?.let { Offset(it.x, it.y) }
                                }
                                .maxByOrNull { it.x }
                                ?.let {
                                    bottomRight = it
                                    roiSize = Size(
                                        width = it.x - topLeft!!.x,
                                        height = it.y - topLeft!!.y
                                    )
                                    mulSyncStep.value = 2
                                }
                        }
                    }
                }
                // Escape
                nativeKeyEvt.keyCode == java.awt.event.KeyEvent.VK_ESCAPE
                        && nativeKeyEvt.keyLocation == java.awt.event.KeyEvent.KEY_LOCATION_STANDARD -> {
                    if (mulSyncStep.value == 0) {
                        val poll = channel.poll()
                        if (poll != null) {
                            escPoints.addAll(
                                poll.mapNotNull {
                                    it.fingerLandmarks[FingerEnum.PINKY]?.let { Offset(it.x, it.y) }
                                }
                            )
                            mulSyncStep.value = 1
                        }
                    }
                }
                else -> {
                    println("Other button pressed! -> $evt")
                }
            }
        }

    }

    fun FingerTipLandmark.toKeyBoardRef(): FingerTipLandmark? {
        roiSize ?: return null
        return if (isInROI(Offset(x = this.x, y = this.y))) { // Over Keyboard
            FingerTipLandmark(
                finger = finger,
                x = x.minus(topLeft!!.x).times(1 / roiSize!!.width),
                y = y.minus(topLeft!!.y).times(1 / roiSize!!.height),
                z = z
            )
        } else null
    }

    fun matchFingerOverKey(char: String): FingerUsed? {
        val hands = channel.poll() ?: return null
        val expectedFinger = fingerMap[char.toUpperCase()] ?: return null
        val fingerPosToCheck: List<FingerTipLandmark> = hands.mapNotNull {
            it.fingerLandmarks[expectedFinger]?.toKeyBoardRef()
        }
        val correctFingerUsed = fingerPosToCheck.any { fingerTip ->
            this.keyboard.any { row ->
                row.any {
                    it.contains(fingerTip)
                }
            }
        }
        if (correctFingerUsed) {
            println("char = [${char}], expected = [${expectedFinger}]")
            return null
        } else {
            mutableListOf(*FingerEnum.values()).also {
                it.remove(expectedFinger)
            }.forEach { finger ->
                hands.mapNotNull {
                    it.fingerLandmarks[finger]?.toKeyBoardRef()
                }.forEach { fingerTip ->
                    if (this.keyboard.any { row ->
                            row.any {
                                it.contains(fingerTip)
                            }
                        }) {
                        println("char = [${char}], expected = [${expectedFinger}], actual = [${finger}]")
                        return FingerUsed(
                            expected = expectedFinger,
                            actual = finger
                        )
                    }
                }
            }
        }
        return null
    }
}
