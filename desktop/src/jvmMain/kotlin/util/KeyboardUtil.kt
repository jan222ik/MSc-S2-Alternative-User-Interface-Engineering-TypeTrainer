package util

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.type
import com.github.jan222ik.common.FingerEnum
import com.github.jan222ik.common.FingerTipLandmark
import com.github.jan222ik.common.HandLandmark
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonPrimitive
import java.io.InputStream
import java.nio.charset.Charset
import java.util.stream.Collectors

object KeyboardUtil {

    private fun InputStream.readLinesAndClose(charset: Charset = Charsets.UTF_8): String {
        return this.bufferedReader(charset).use { it.lines().collect(Collectors.joining()) }
    }

    private fun String.getResourceAsFile(): InputStream? {
        return KeyboardUtil::class.java.classLoader.getResourceAsStream(this)
    }

    /* http://www.keyboard-layout-editor.com/#/ */
    /* !ATTENTION! Space will be "" -> therefore change file to contain "Space" instead! */
    fun readKeyboard(): Keyboard {
        val keyboardLines =
            "keyboards/keyboard-relevant-layout_iso-105_de.json".getResourceAsFile()!!.readLinesAndClose()
        val array: JsonArray = Json.decodeFromString(keyboardLines)
        var keyboard = Keyboard()

        array.forEach { row ->
            val keys = mutableListOf<Keyboard.Key>()

            val it = row.jsonArray.iterator()
            while (it.hasNext()) {
                var elem = it.next()
                when (elem) {
                    is JsonObject -> {
                        when {
                            elem.count() > 3 -> keys.add(Keyboard.Key(strChars = elem.toString()))
                            elem.containsKey("w") -> {
                                if (elem.containsKey("x"))
                                    keys.add(Keyboard.Key(x = elem["x"].toString().toDouble()))
                                val temp = elem
                                elem = it.next()
                                keys.add(
                                    Keyboard.Key(
                                        strChars = elem.jsonPrimitive.content,
                                        w = temp["w"].toString().toDouble()
                                    )
                                )
                            }
                            elem.containsKey("x") -> keys.add(Keyboard.Key(x = elem["x"].toString().toDouble()))
                            elem.containsKey("y") -> {
                                val singleRow = mutableListOf<Keyboard.Key>()
                                singleRow.add(Keyboard.Key(y = elem["y"].toString().toDouble()))
                                keyboard.keys.add(singleRow)
                            }
                        }
                    }
                    else -> keys.add(Keyboard.Key(strChars = elem.jsonPrimitive.content))
                }
            }
            keyboard.keys.add(keys)
        }

        Keyboard.Instance = keyboard
        Keyboard.Instance.init()
        return Keyboard.Instance
    }

    val fingerMap: MutableMap<String, FingerEnum> = mutableMapOf<String, FingerEnum>().also { map ->
        listOf("^", "1", "2", "Tab", "Caps", "A", "Shift", "<", "Y").forEach {
            map[it] = FingerEnum.PINKY
        }

        listOf("3", "W", "S", "X", "LALT").forEach {
            map[it] = FingerEnum.RING
        }

        listOf("4", "E", "D", "C").forEach {
            map[it] = FingerEnum.MIDDLE
        }

        listOf("5", "R", "F", "V") + listOf("6", "T", "G", "B").forEach {
            map[it] = FingerEnum.INDEX
        }

        listOf("SPACE").forEach {
            map[it] = FingerEnum.THUMB
        }

        listOf("7", "Z", "H", "N") + listOf("8", "U", "J", "M").forEach {
            map[it] = FingerEnum.INDEX
        }

        listOf("9", "I", "K", ",").forEach {
            map[it] = FingerEnum.MIDDLE
        }

        listOf("0", "O", "L", ".").forEach {
            map[it] = FingerEnum.RING
        }

        (listOf("ß", "P", "Ö", "-", "RALT")
                + listOf("´", "Ü", "Ä", "RSHIFT", "RCTRL")
                + listOf("BACKSPACE", "+", "#", "ENTER"))
            .forEach {
                map[it] = FingerEnum.PINKY
            }

    }


}

class FingerMatcher(
    private val fingerMap: Map<String, FingerEnum> = KeyboardUtil.fingerMap,
    private val keyboard: Keyboard = KeyboardUtil.readKeyboard(),
    private val channel: ReceiveChannel<List<HandLandmark>>
) {
    private val mulSyncStep = mutableStateOf(0)
    val syncStep: State<Int>
        get() = mulSyncStep

    private val escPoints = mutableListOf<Offset>()
    private var topLeft: Offset? = null

    fun syncInput(evt: KeyEvent) {
        if (evt.type == KeyEventType.KeyDown) {
            val nativeKeyEvt = evt.nativeKeyEvent
            when {
                // Right Control
                nativeKeyEvt.keyCode == 17 && nativeKeyEvt.keyLocation == java.awt.event.KeyEvent.KEY_LOCATION_RIGHT -> {
                    if (mulSyncStep.value == 1) {
                        // TODO Save Location or something
                        // TODO IF OTHER STEPS MOVE TO LAST STEP
                        topLeft = escPoints.minByOrNull { it.x }
                        mulSyncStep.value = 2 // Useless unless above necessary
                    }
                }
                // Escape
                nativeKeyEvt.keyCode == java.awt.event.KeyEvent.VK_ESCAPE
                        && nativeKeyEvt.keyLocation == java.awt.event.KeyEvent.KEY_LOCATION_STANDARD -> {
                    if (mulSyncStep.value == 0) {
                        escPoints.addAll(
                            channel.poll()!!
                                .mapNotNull {
                                    it.fingerLandmarks[FingerEnum.PINKY]?.let { Offset(it.x, it.y) }
                                }
                        )
                        mulSyncStep.value = 1
                    }
                }
                else -> {
                    println("Other button pressed! -> $evt")
                }
            }
        }

    }

    fun matchFingerOverKey(char: String): FingerUsed? {
        val hands = channel.poll()!!
        val expectedFinger = fingerMap[char.toUpperCase()]
        if (expectedFinger == null) {
            return null
        }
        val fingerPosToCheck: List<FingerTipLandmark> = hands.mapNotNull {
            it.fingerLandmarks[expectedFinger]
                ?.toKeyBoardRef(topLeft = topLeft ?: Offset.Zero)
        }
        val correctFingerUsed = fingerPosToCheck.any { fingerTip ->
            keyboard.keys.any { l ->
                l.any {
                    !it.isSpacer() && it.contains(fingerTip)
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
                    it.fingerLandmarks[finger]?.toKeyBoardRef(topLeft = topLeft ?: Offset.Zero)
                }.forEach { fingerTip ->
                    if (keyboard.keys.any { l ->
                            l.any {
                                !it.isSpacer() && it.contains(fingerTip)
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

@Serializable
data class FingerUsed(
    val expected: @Contextual FingerEnum,
    val actual: @Contextual FingerEnum
)

fun main() {
    val keyboard = KeyboardUtil.readKeyboard()
    println(keyboard.getBounds())
}
