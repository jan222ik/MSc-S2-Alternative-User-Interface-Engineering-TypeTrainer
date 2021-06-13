package util

import androidx.compose.ui.geometry.Offset
import com.github.jan222ik.common.FingerTipLandmark
import java.awt.event.KeyEvent.VK_BACK_SPACE
import java.awt.event.KeyEvent.VK_ENTER
import java.awt.event.KeyEvent.VK_ESCAPE
import java.awt.event.KeyEvent.VK_SHIFT
import java.awt.event.KeyEvent.VK_SPACE
import java.awt.event.KeyEvent.VK_TAB
import java.security.Key

class Keyboard {
    companion object {
        private const val EMPTY_KEY = "~|~"
        private const val DEFAULT_X = 0.0
        private const val DEFAULT_Y = 0.0
        private const val DEFAULT_W = 1.0
        private const val DEFAULT_H = 1.0

        private val KeyCodes: Map<String, Int> = mapOf(
            "ENTER" to VK_ENTER,
            "ESC" to VK_ESCAPE,
            "BACKSPACE" to VK_BACK_SPACE,
            "TAB" to VK_TAB,
            "SHIFT" to VK_SHIFT,
            "SPACE" to VK_SPACE
        ) as HashMap

        lateinit var Instance: Keyboard
    }

    data class Key(
        val strChars: String = EMPTY_KEY,
        val x: Double = DEFAULT_X,
        val y: Double = DEFAULT_Y,
        val w: Double = DEFAULT_W,
        val h: Double = DEFAULT_H,
    ) {
        var xCoord: Double = 0.0
        var yCoord: Double = 0.0
        var chars: MutableList<Char> = mutableListOf()
        fun isSpacer(): Boolean = strChars == EMPTY_KEY && x == DEFAULT_X
        fun getWidth(): Double = if (x == DEFAULT_X) w else x
        fun getHeight(): Double = if (y == DEFAULT_Y) h else y
        fun getBounds(): Pair<Double, Double> = Pair(getWidth(), getHeight())
        fun getCoords(): Pair<Double, Double> = Pair(xCoord, yCoord)

        fun contains(fingerTip: FingerTipLandmark): Boolean {
            val (startX, startY) = getCoords()
            val endX = startX.plus(w)
            val endY = startY.plus(h)
            val inX = (startX..endX).contains(fingerTip.x)
            val inY = (startY..endY).contains(fingerTip.y)
            return inX && inY
        }

    }

    val keys: MutableList<MutableList<Key>> = mutableListOf()

    fun init() {
        var actY = 0.0
        keys.forEach { row ->
            var actX = 0.0
            actY += row[0].h
            row.forEach { key ->
                key.xCoord = actX
                key.yCoord = actY
                actX += key.x

                key.strChars.split("\n").forEach { charString ->
                    if (KeyCodes.containsKey(charString.toUpperCase()))
                        KeyCodes[charString.toUpperCase()]?.let { key.chars.add(it.toChar()) }
                    else
                        if (charString.length == 1)
                            key.chars.add(charString[0])
                }
                print(key.strChars + " = ")
                key.chars.forEach {
                    print("$it, ")
                }
                println()
            }
        }
    }

    fun getBounds(): Pair<Double, Double> {
        return Pair(getWidth(), getHeight())
    }

    fun getHeight(): Double {
        return keys.sumOf { it.first().getHeight() }
    }

    fun getWidth(): Double {
        return keys.map { row ->
            println(row.sumOf { it.getWidth() })
            row.sumOf { it.getWidth() }
        }.maxOf { it }
    }

    fun getKey(char: Char): Key {
        return keys.flatten().first() {
            it.chars.contains(char)
        }
    }
}
