package util

import androidx.compose.desktop.Window
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.drawscope.Stroke
import com.github.jan222ik.common.FingerEnum
import com.github.jan222ik.common.FingerTipLandmark
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
    @Deprecated("RelativeKeyboard should be used instead")
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

    fun getRelativeKeyboard(): List<List<RelativeKey>> {
        val yl0 = 0f
        val l0 = listOf(
            K(x = 0f, y = yl0, ch = "Esc"),
            // Space w = 1 -> x + 1
            K(x = 2f, y = yl0, ch = "F1"),
            K(x = 3f, y = yl0, ch = "F2"),
            K(x = 4f, y = yl0, ch = "F3"),
            K(x = 5f, y = yl0, ch = "F4"),
            // Space w = 0.5 -> x + 0.5
            K(x = 6.5f, y = yl0, ch = "F5"),
            K(x = 7.5f, y = yl0, ch = "F6"),
            K(x = 8.5f, y = yl0, ch = "F7"),
            K(x = 9.5f, y = yl0, ch = "F8"),
            // Space w = 0.5 -> x + 0.5
            K(x = 11f, y = yl0, ch = "F9"),
            K(x = 12f, y = yl0, ch = "F10"),
            K(x = 13f, y = yl0, ch = "F11"),
            K(x = 14f, y = yl0, ch = "F12"),
        )
        val yl1 = 1f + 0.5f
        val l1 = listOf(
            K(x = 0f, y = yl1, ch = "°\n^"),
            K(x = 1f, y = yl1, ch = "!\n1"),
            K(x = 2f, y = yl1, ch = "\"\n2\n\n²"),
            K(x = 3f, y = yl1, ch = "§\n3\n\n³"),
            K(x = 4f, y = yl1, ch = "$\n4"),
            K(x = 5f, y = yl1, ch = "%\n5"),
            K(x = 6f, y = yl1, ch = "&\n6"),
            K(x = 7f, y = yl1, ch = "/\n7\n\n{"),
            K(x = 8f, y = yl1, ch = "(\n8\n\n["),
            K(x = 9f, y = yl1, ch = ")\n9\n\n]"),
            K(x = 10f, y = yl1, ch = "=\n0\n\n}"),
            K(x = 11f, y = yl1, ch = "?\nß\n\n\\"),
            K(x = 12f, y = yl1, ch = "`\n´"),
            K(x = 13f, y = yl1, w = 2f, ch = "Backspace"),
        )
        val yl2 = yl1 + 1f
        val l2 = listOf(
            K(x = 0f, y = yl2, w = 1.5f, ch = "Tab"),
            K(x = 1.5f, y = yl2, ch = "Q"),
            K(x = 2.5f, y = yl2, ch = "W"),
            K(x = 3.5f, y = yl2, ch = "E"),
            K(x = 4.5f, y = yl2, ch = "R"),
            K(x = 5.5f, y = yl2, ch = "T"),
            K(x = 6.5f, y = yl2, ch = "Y"),
            K(x = 7.5f, y = yl2, ch = "U"),
            K(x = 8.5f, y = yl2, ch = "I"),
            K(x = 9.5f, y = yl2, ch = "O"),
            K(x = 10.5f, y = yl2, ch = "P"),
            K(x = 11.5f, y = yl2, ch = "Ü"),
            K(x = 12.5f, y = yl2, ch = "*\n+\n\n~"),
            K(x = 13.5f, y = yl2, w = 1.5f, ch = "Enter"),
        )
        val yl3 = yl2 + 1f
        val l3 = listOf(
            K(x = 0f, y = yl3, w = 1.75f, ch = "Caps Lock"),
            K(x = 1.75f, y = yl3, ch = "A"),
            K(x = 2.75f, y = yl3, ch = "S"),
            K(x = 3.75f, y = yl3, ch = "D"),
            K(x = 4.75f, y = yl3, ch = "F"),
            K(x = 5.75f, y = yl3, ch = "G"),
            K(x = 6.75f, y = yl3, ch = "H"),
            K(x = 7.75f, y = yl3, ch = "J"),
            K(x = 8.75f, y = yl3, ch = "K"),
            K(x = 9.75f, y = yl3, ch = "L"),
            K(x = 10.75f, y = yl3, ch = "Ö"),
            K(x = 11.75f, y = yl3, ch = "Ä"),
            K(x = 12.75f, y = yl3, ch = "'\n#"),
            K(x = 13.75f, y = yl3, w = 1.25f, ch = "Enter"),
        )
        val yl4 = yl3 + 1f
        val l4 = listOf(
            K(x = 0f, y = yl4, w = 1.25f, ch = "Shift"),
            K(x = 1.25f, y = yl4, ch = ">\n<\n\n|"),
            K(x = 2.25f, y = yl4, ch = "Z"),
            K(x = 3.25f, y = yl4, ch = "X"),
            K(x = 4.25f, y = yl4, ch = "C"),
            K(x = 5.25f, y = yl4, ch = "V"),
            K(x = 6.25f, y = yl4, ch = "B"),
            K(x = 7.25f, y = yl4, ch = "N"),
            K(x = 8.25f, y = yl4, ch = "M"),
            K(x = 9.25f, y = yl4, ch = ";\n,"),
            K(x = 10.25f, y = yl4, ch = ":\n."),
            K(x = 11.25f, y = yl4, ch = "_\n-"),
            K(x = 12.25f, y = yl4, w = 2.75f, ch = "Shift"),
        )
        val yl5 = yl4 + 1f
        val l5 = listOf(
            K(x = 0f, y = yl5, w = 1.25f, ch = "Ctrl"),
            K(x = 1.25f, y = yl5, w = 1.25f, ch = "Win"),
            K(x = 2.50f, y = yl5, w = 1.25f, ch = "Alt"),
            K(x = 3.75f, y = yl5, w = 6.25f, ch = "Space"),
            K(x = 10.0f, y = yl5, w = 1.25f, ch = "AltGr"),
            K(x = 11.25f, y = yl5, w = 1.25f, ch = "Win"),
            K(x = 12.50f, y = yl5, w = 1.25f, ch = "Menu"),
            K(x = 13.75f, y = yl5, w = 1.25f, ch = "Ctrl"),
        )
        return listOf(l0, l1, l2, l3, l4, l5).let { lists ->
            val maxWidth = lists.maxOf { it.last().let { it.x + it.w } }
            val maxHeight = lists.maxOf { it.last().let { it.y + it.h } }
            println("maxWidth = $maxWidth, maxHeight = $maxHeight")
            lists.map {
                it.map {
                    it.rel(1 / maxWidth, heightScale = 1 / maxHeight)
                }
            }
        }
    }


}

data class K(
    val x: Float,
    val y: Float,
    val w: Float = 1f,
    val h: Float = 1f,
    val ch: String
) {
    fun rel(widthScale: Float, heightScale: Float): RelativeKey {
        return RelativeKey(
            x = x * widthScale,
            y = y * heightScale,
            w = w * widthScale,
            h = h * heightScale,
            ch = ch
        )
    }
}

data class RelativeKey(
    val x: Float,
    val y: Float,
    val w: Float,
    val h: Float,
    val ch: String
) {
    fun contains(fingerTip: FingerTipLandmark): Boolean {
        val endX = x.plus(w)
        val endY = y.plus(h)
        val inX = (x..endX).contains(fingerTip.x)
        val inY = (x..endY).contains(fingerTip.y)
        return inX && inY
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

    Window {
        Canvas(Modifier.fillMaxSize().background(Color.DarkGray)) {
            val (w, h) = size
            KeyboardUtil.getRelativeKeyboard().forEach { row ->
                row.forEach { key ->
                    drawRect(
                        brush = SolidColor(Color.White),
                        topLeft = Offset(
                            x = w * key.x,
                            y = h * key.y
                        ),
                        size = Size(
                            width = w * key.w,
                            height = h * key.h
                        )
                    )
                    drawRect(
                        brush = SolidColor(Color.Black),
                        topLeft = Offset(
                            x = w * key.x,
                            y = h * key.y
                        ),
                        size = Size(
                            width = w * key.w,
                            height = h * key.h
                        ),
                        style = Stroke(width = 1f)
                    )
                }
            }

        }
    }
}
