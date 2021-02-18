//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.math

fun parseHexValue(text: String): Int {
    // Get rid of the x
    var text = text
    if (text.contains("x")) text = text.substring(text.indexOf('x') + 1, text.length)
    return hexToInt(text)
}

private var hexTable = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')
fun hexToInt(hex: String): Int {
    var value = 0
    for (c in hex.toCharArray()) {
        value *= 16
        var index = 0
        for (d in hexTable) {
            if (c == d) {
                value += index
            }
            index++
        }
    }
    return value
}

fun byteArrayAsHexString(array: ByteArray): String {
    var s = ""
    for (b in array) {
        s += hexTable[(b.toInt() shr 4) and 0xF]
        s += hexTable[(b.toInt() shr 0) and 0xF]
    }
    return s.toLowerCase()
}

fun intToHex(i: Int): String {
    // System.out.println("wtf "+i);
    return if (i < 0) "" else "" + hexTable[i / 16 % 16] + hexTable[i % 16]
}

fun isHexOnly(str: String): Boolean {
    var yes = true
    for (c in str.toCharArray()) {
        var found = false
        for (d in hexTable) {
            if (c == d) found = true
        }
        if (!found) yes = false
    }
    return yes
}