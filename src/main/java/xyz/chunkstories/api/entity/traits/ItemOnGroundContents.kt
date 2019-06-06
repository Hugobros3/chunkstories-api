//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.serializable.TraitSerializable
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.item.inventory.obtainItemPileFromStream
import xyz.chunkstories.api.item.inventory.saveIntoStream
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class ItemOnGroundContents(entity: Entity) : TraitSerializable(entity) {
    /*var : ItemPile? = null
        set(value) {
            field = value
            if (entity.world is WorldMaster)
                this.pushComponentEveryone()
        }*/
    var item: Item? = null
    var amount: Int = 0

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        item?.saveIntoStream(amount, entity.world.contentTranslator, dos) ?: dos.writeInt(0)
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, dis: DataInputStream) {
        try {
            val (item, amount) = obtainItemPileFromStream(entity.world.contentTranslator, dis)
            this.item = item
            this.amount = amount
        } catch (e: NullItemException) {
            this.item = null
            this.amount = 0
        } catch (e: UndefinedItemTypeException) {
            e.printStackTrace()
        }
    }
}
