//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.json

/** Tight, type-safe representation of Json for use in engine internals */
sealed class Json {
    sealed class Value : Json() {
        data class Text(val text: String) : Value()
        data class Number(val double: Double) : Value()
        data class Bool(val value: Boolean) : Value()
        object Null : Value()
    }

    data class Dict(val elements: Map<String, Json>) : Json() {
        operator fun get(s: String) = elements[s]
    }

    data class Array(val elements: List<Json>) : Json() {
        operator fun get(i: Int) = elements[i]
    }
}

val Json?.asBoolean: Boolean?
    get() = (this as? Json.Value.Bool)?.value

val Json?.asInt: Int?
    get() = (this as? Json.Value.Number)?.double?.toInt()

val Json?.asDouble: Double?
    get() = (this as? Json.Value.Number)?.double?.toDouble()

val Json?.asFloat: Float?
    get() = (this as? Json.Value.Number)?.double?.toFloat()

val Json?.asString: String?
    get() = (this as? Json.Value)?.let {
        when (it) {
            is Json.Value.Bool -> it.value.toString()
            is Json.Value.Text -> it.text
            is Json.Value.Number -> it.double.toString()
            Json.Value.Null -> "null"
        }
    }

val Json?.asDict: Json.Dict?
    get() = (this as? Json.Dict)

val Json?.asArray: Json.Array?
    get() = (this as? Json.Array)