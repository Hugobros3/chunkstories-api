//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.json

fun Json.stringSerialize(): String {
    val sb = StringBuilder()
    this.stringSerialize(sb)
    return sb.toString()
}

fun Json.stringSerialize(builder: StringBuilder) {
    when (this) {
        is Json.Value.Text -> builder.append('"').append(text.escape()).append('"')
        is Json.Value.Number -> builder.append(double.toString())
        is Json.Value.Bool -> if (value) builder.append("true") else builder.append("false")
        Json.Value.Null -> builder.append("null")
        is Json.Dict -> {
            builder.append("{")
            var first = true
            for ((name, value) in elements) {
                if (!first)
                    builder.append(',')
                else
                    first = false
                builder.append('"')
                builder.append(name.escape())
                builder.append('"')
                builder.append(':')
                value.stringSerialize(builder)
            }
            builder.append("}")
        }
        is Json.Array -> {
            builder.append('[')
            var first = true
            for (value in elements) {
                if (!first)
                    builder.append(',')
                else
                    first = false
                value.stringSerialize(builder)
            }
            builder.append(']')
        }
    }
}

private fun String.escape(): String {
    var acc = this
    acc = if (contains('\\')) acc.replace("\\", "\\\\") else acc
    acc = if (contains('"'))
        acc.replace("\"", "\\\"")
    else acc
    return acc
}

fun String.toJson(): Json = StringParserHelper(this).readToken()

internal class StringParserHelper(@JvmField val text: String, index: Int = 0) {
    @JvmField
    var index = index

    private fun matchToken(token: String): Boolean {
        if (text.regionMatches(index, token, 0, token.length)) {
            index += token.length
            //println("matched $token ${token.length} $index")
            return true
        }
        return false
    }

    private fun matchToken(token: Char): Boolean {
        if (text[index] == token) {
            index++
            return true
        }
        return false
    }

    private fun assume(token: String) {
        if (!matchToken(token))
            throw Exception("Expected $token, got:${text.substring(index)}")
    }

    private fun assume(token: Char) {
        if (!matchToken(token))
            throw Exception("Expected $token, got:${text.substring(index)}")
    }

    private fun consumeUntil(token: String): String {
        val sb = StringBuilder()

        loop@ while(true) {
            when {
                matchToken("\\\\") -> sb.append('\\')
                matchToken("\\\"") -> sb.append('"')
                matchToken("\\n") -> sb.append('\n')
                matchToken("\\u") -> {
                    val code = text.substring(index, index + 4).parseHex()
                    sb.append(code)
                    index +=4
                }
                matchToken(token) -> break@loop
                else -> {
                    sb.append(text[index])
                    index++
                }
            }
        }

        val cs = sb.toString()

        return cs
    }

    private fun skipWhitespace() {
        while(whitespacechars.indexOf(text[index]) != -1) {
            index++
        }
    }

    fun readToken(): Json {
        skipWhitespace()

        return when {
            matchToken("\"") -> {
                val text = consumeUntil("\"")
                Json.Value.Text(text)
            }
            matchToken("[") -> {
                val elements = mutableListOf<Json>()
                while (true) {
                    skipWhitespace()
                    if (matchToken("]"))
                        break

                    elements += readToken()
                    skipWhitespace()

                    if (matchToken("]"))
                        break
                    assume(",")
                }
                Json.Array(elements)
            }
            matchToken("{") -> {
                val elements = mutableMapOf<String, Json>()
                while (true) {
                    skipWhitespace()
                    if (matchToken("}"))
                        break
                    assume("\"")
                    val name = consumeUntil("\"")
                    skipWhitespace()
                    assume(":")
                    val value = readToken()

                    elements[name] = value

                    skipWhitespace()
                    if (matchToken("}"))
                        break
                    assume(",")
                }
                Json.Dict(elements)
            }
            matchToken("null") -> Json.Value.Null
            matchToken("true") -> Json.Value.Bool(true)
            matchToken("false") -> Json.Value.Bool(false)
            // Assume a number
            else -> {
                skipWhitespace()
                if(matchToken("NaN")) {
                    Json.Value.Number(Double.NaN)
                } else {
                    var i2 = index
                    while (numberChars.indexOf(text[i2]) != -1) {
                        i2++
                    }
                    val number = text.substring(index, i2).toDouble()
                    index = i2
                    Json.Value.Number(number)
                }
            }
        }
    }
}

const val whitespacechars = " \n\t\r"
const val numberChars = "0123456789-.E"
const val hex = "0123456789abcdef"

private fun String.parseHex(): Int {
    val lower = this.toLowerCase()
    var acc = 0
    for((i, char) in lower.withIndex()) {
        val place = this.length - i - 1
        val value = hex.indexOf(char)
        acc = acc or (value shl (place * 4))
    }
    return acc
}