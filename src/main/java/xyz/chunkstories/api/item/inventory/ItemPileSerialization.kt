//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.content.ContentTranslator
import xyz.chunkstories.api.content.json.Json
import xyz.chunkstories.api.content.json.asDict
import xyz.chunkstories.api.content.json.stringSerialize
import xyz.chunkstories.api.content.json.toJson
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.Item
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

@Throws(IOException::class, UndefinedItemTypeException::class, NullItemException::class)
fun obtainItemPileFromStream(translator: ContentTranslator, stream: DataInputStream): Pair<Item, Int> {
    val itemId = stream.readInt()
    if (itemId == 0)
        throw NullItemException(stream)

    val itemType = translator.getItemForId(itemId) ?: throw UndefinedItemTypeException(itemId)
    val amount = stream.readInt()
    val item = itemType.newItem<Item>()

    item.deserialize(stream.readUTF().toJson().asDict ?: Json.Dict(emptyMap()))
    //item.load(stream)

    //val itemPile = ItemPile(itemType, amount)
    //itemPile.item.load(stream)

    return Pair(item, amount)
}

@Throws(IOException::class)
fun ItemPile.saveIntoStream(translator: ContentTranslator, stream: DataOutputStream) {
    stream.writeInt(translator.getIdForItem(item))
    stream.writeInt(amount)

    stream.writeUTF(item.serialize().stringSerialize())
    //item.save(stream)
}

fun Item.saveIntoStream(amount: Int, translator: ContentTranslator, stream: DataOutputStream) {
    stream.writeInt(translator.getIdForItem(this))
    stream.writeInt(amount)
    //this.save(stream)
    stream.writeUTF(this.serialize().stringSerialize())
}