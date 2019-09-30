//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.item.inventory

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.net.PacketReceptionContext
import xyz.chunkstories.api.voxel.components.VoxelComponent
import xyz.chunkstories.api.voxel.components.VoxelInventoryComponent
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

/* Helper functions to translate inventories across hosts. The remote server has
 * no concept of an object reference from **your** heap, so you have to put a
 * little more work to describe what inventory you are talking about in terms
 * the both of you can understand.
 *
 *
 *  * For null inventories, you send a magic byte
 *
 *  * For Entity-relative inventories you send the entity UUID and the
 * component
 *
 *  * For Voxel-relative inventories, you send the xyz position and the
 * component id
 *
 *  * For other snowflake inventories made out of pixie dust, returns an
 * error
 *
 */

@Throws(IOException::class)
fun writeInventoryHandle(stream: DataOutputStream, inventory: Inventory?) {
    if (inventory == null) {
        stream.writeByte(0x00)
        return
    }

    when (val owner = inventory.owner) {
        null -> stream.writeByte(0x00)
        is Entity -> {
            //TraitInventory entityInventory = (TraitInventory) inventory;
            val trait = owner.traits.all().find { it is TraitInventory && it.inventory == inventory }
                    ?: throw Exception("This inventory is attached to an entity but that entity does not have a TraitInventory linking it back !")

            stream.writeByte(0x01)
            stream.writeLong((inventory.owner as Entity).UUID)
            stream.writeShort(trait.id)
        }
        is VoxelComponent -> {
            val component = inventory.owner as VoxelComponent?

            stream.writeByte(0x02)
            stream.writeInt(component!!.holder.x)
            stream.writeInt(component.holder.y)
            stream.writeInt(component.holder.z)
            stream.writeUTF(component.name)
        }
        else -> {
            stream.writeByte(0x00)
        }
    }
}

@Throws(IOException::class)
fun obtainInventoryByHandle(stream: DataInputStream, context: PacketReceptionContext): Inventory? {
    val holderType = stream.readByte()
    if (holderType.toInt() == 0x01) {
        val uuid = stream.readLong()
        val traitId = stream.readShort()
        val entity = context.world!!.getEntityByUUID(uuid)
        val trait = entity!!.traits.byId()[traitId.toInt()]

        if (trait is TraitInventory) {
            return trait.inventory
        }
    } else if (holderType.toInt() == 0x02) {
        val x = stream.readInt()
        val y = stream.readInt()
        val z = stream.readInt()

        val traitName = stream.readUTF()

        try {
            val voxelContext = context.world!!.tryPeek(x, y, z)
            val component = voxelContext.components.getVoxelComponent(traitName)
            if (component is VoxelInventoryComponent) {
                return component.inventory
            }
        } catch (e: WorldException) {
            // TODO log as warning
            // Ignore and return null
        }

    }

    return null
}

