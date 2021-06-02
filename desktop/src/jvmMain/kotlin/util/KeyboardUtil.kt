package util

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
        val keyboardLines = "keyboards/keyboard-relevant-layout_iso-105_de.json".getResourceAsFile()!!.readLinesAndClose()
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

        keyboard.init()
        return keyboard
    }
}

fun main() {
    val keyboard = KeyboardUtil.readKeyboard()
    println(keyboard.getBounds())
}
