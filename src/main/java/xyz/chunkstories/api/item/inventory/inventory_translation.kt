//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.block.BlockAdditionalData
import xyz.chunkstories.api.block.components.BlockInventory
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

fun writeInventoryHandle(stream: DataOutputStream, inventory: Inventory?) {
    if (inventory == null) {
        stream.writeByte(0x00)
        return
    }

    when (val owner = inventory.owner) {
        null -> stream.writeByte(0x00)
        is Entity -> {
            val trait = owner.traits.all().find { it is TraitInventory && it.inventory == inventory }
                    ?: throw Exception("This inventory is attached to an entity but that entity does not have a TraitInventory linking it back !")

            stream.writeByte(0x01)
            stream.writeLong((inventory.owner as Entity).id)
            stream.writeShort(trait.id)
        }
        is BlockAdditionalData -> {
            val cell = owner.cell

            stream.writeByte(0x02)
            stream.writeInt(cell.x)
            stream.writeInt(cell.y)
            stream.writeInt(cell.z)
            stream.writeUTF(owner.name)
        }
        else -> {
            stream.writeByte(0x00)
        }
    }
}

@Throws(IOException::class)
fun obtainInventoryByHandle(stream: DataInputStream, world: World): Inventory? {
    val holderType = stream.readByte()
    if (holderType.toInt() == 0x01) {
        val entityId = stream.readLong()
        val traitId = stream.readShort()
        val entity = world.getEntity(entityId)
        val trait = entity!!.traits.byId()[traitId.toInt()]

        if (trait is TraitInventory) {
            return trait.inventory
        }
    } else if (holderType.toInt() == 0x02) {
        val x = stream.readInt()
        val y = stream.readInt()
        val z = stream.readInt()

        val traitName = stream.readUTF()

        val cell = world.getCell(x, y, z) ?: return null
        val inventory = cell.data.additionalData.find { it.name == traitName }
        if (inventory is BlockInventory) {
            return inventory.inventory
        }
    }

    return null
}

