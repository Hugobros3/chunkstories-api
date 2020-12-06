//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//
package xyz.chunkstories.api.util

import xyz.chunkstories.api.math.hexToInt
import xyz.chunkstories.api.math.intToHex
import xyz.chunkstories.api.math.isHexOnly
import java.security.MessageDigest
import kotlin.math.abs

/// Some basic helper functions I wrote ages ago, basic RGB/Hex conversions functions and whatnot

fun parseHexadecimalColorCode(hex: String): IntArray {
    val rgb = IntArray(3)
    val bytes = hex.toCharArray()
    if (bytes.size != 6) throw Exception("Expected 6 characters long string")
    rgb[0] = hexToInt(hex.substring(0, 2))
    rgb[1] = hexToInt(hex.substring(2, 4))
    rgb[2] = hexToInt(hex.substring(4, 6))
    return rgb
}

fun rgbToHex(i: Int): String {
    var i = i
    i += 256 * 256 * 256
    var returnMeh = ""
    val r = i / (256 * 256)
    val v = i / 256 % 256
    val b = i % 256
    returnMeh += intToHex(r)
    returnMeh += intToHex(v)
    returnMeh += intToHex(b)
    return returnMeh
}

fun rgbSplit(i: Int): IntArray {
    var i = i
    i += 256 * 256 * 256
    val rgb = IntArray(3)
    rgb[0] = i / (256 * 256)
    rgb[1] = i / 256 % 256
    rgb[2] = i % 256
    return rgb
}

private var ansiRGB = arrayOf(intArrayOf(0, 0, 0), intArrayOf(187, 0, 0), intArrayOf(0, 187, 0), intArrayOf(187, 187, 0), intArrayOf(0, 0, 187), intArrayOf(187, 0, 187), intArrayOf(0, 187, 187), intArrayOf(187, 187, 187))
private var ansiEscape = arrayOf("\u001B[0m", "\u001B[31m", "\u001B[32m", "\u001B[33m", "\u001B[34m", "\u001B[35m", "\u001B[36m", "\u001B[37m")

fun convertToAnsi(text: String): String {
    val doConvert = true // !System.getProperty("os.name").toLowerCase().contains("windows");
    // // As windows don't support ansi codes in terminal we disable it.
    // Use of Jansi prevents us from having to hack arround this shit
    var result = ""
    var i = 0
    var skip = 0 // skips a few characters when it founds a hex code so it doesn't appear
    for (c in text.toCharArray()) {
        if (skip > 0) {
            skip--
        } else {
            if (c == '#' && text.length - i - 1 >= 6 && isHexOnly(text.substring(i + 1, i + 7))) {
                val colorCode = text.substring(i + 1, i + 7)
                val rgb = parseHexadecimalColorCode(colorCode)
                if (doConvert) result += getNearestAnsiOfRgb(rgb)
                skip = 6
            } else result += c
        }
        i++
    }
    if (doConvert) result += "\u001B[0m" // don't forget to reset the input !
    return result
}

private fun getNearestAnsiOfRgb(rgb: IntArray): String {
    var distance = Int.MAX_VALUE
    var best = 0
    for (i in ansiRGB.indices) {
        val distance2 = abs(ansiRGB[i][0] - rgb[0]) + Math.abs(ansiRGB[i][1] - rgb[1]) + Math.abs(ansiRGB[i][2] - rgb[2])
        if (distance2 < distance) {
            best = i
            distance = distance2
        }
    }
    return ansiEscape[best]
}

private var md = MessageDigest.getInstance("MD5")
fun getUniqueColorCode(text: String): Int {
    val digested = md.digest(text.toByteArray())
    val r: Int = digested[0].toInt() and 0xFF
    val g: Int = digested[1].toInt() and 0xFF
    val b: Int = digested[2].toInt() and 0xFF
    return r * 255 * 255 + g * 255 + b
}

fun getUniqueColorPrefix(text: String): String {
    val digested = md.digest(text.toByteArray())
    val r: Int = digested[0].toInt() and 0xFF
    val g: Int = digested[1].toInt() and 0xFF
    val b: Int = digested[2].toInt() and 0xFF
    return "#" + intToHex(r) + intToHex(g) + intToHex(b)
}