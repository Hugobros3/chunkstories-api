//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.content.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter

class GsonJsonAdapter : TypeAdapter<Json>() {
    override fun write(writer: JsonWriter, value: Json?) {
        if(value != null) {
            when(value) {
                is Json.Value.Text -> writer.value(value.text)
                is Json.Value.Number -> writer.value(value.double)
                is Json.Value.Bool -> writer.value(value.value)
                is Json.Value.Null -> writer.nullValue()
                is Json.Dict -> {
                    writer.beginObject()
                    for(element in value.elements) {
                        writer.name(element.key)
                        write(writer, element.value)
                    }
                    writer.endObject()
                }
                is Json.Array -> {
                    writer.beginArray()
                    for(element in value.elements) {
                        write(writer, element)
                    }
                    writer.endArray()
                }
            }
        }
    }

    override fun read(reader: JsonReader): Json? {
        when(reader.peek()) {
            JsonToken.BEGIN_ARRAY -> {
                reader.beginArray()
                val list = mutableListOf<Json>()
                while(true) {
                    val read = read(reader) ?: break
                    list += read
                }
                return Json.Array(list)
            }
            JsonToken.END_ARRAY -> {
                reader.endArray()
                return null
            }
            JsonToken.BEGIN_OBJECT -> {
                reader.beginObject()
                val map = mutableMapOf<String, Json>()
                while(true) {
                    if(reader.peek() == JsonToken.END_OBJECT)
                        break
                    val name = reader.nextName()
                    val value = read(reader) ?: Json.Value.Null
                    map[name] = value
                }
                reader.endObject()
                return Json.Dict(map)
            }
            JsonToken.END_OBJECT -> {
                throw Exception("Unexpected end object")
                //reader.endObject()
                //return null
            }
            JsonToken.NAME -> {
                throw Exception("Unexpected name")
                //return Json.Value.Text(reader.nextString())
            }
            JsonToken.STRING -> {
                return Json.Value.Text(reader.nextString())
            }
            JsonToken.NUMBER -> {
                return Json.Value.Number(reader.nextDouble())
            }
            JsonToken.BOOLEAN -> {
                return Json.Value.Bool(reader.nextBoolean())
            }
            JsonToken.NULL -> {
                reader.nextNull()
                return Json.Value.Null
            }
            JsonToken.END_DOCUMENT -> return null
        }
    }

}