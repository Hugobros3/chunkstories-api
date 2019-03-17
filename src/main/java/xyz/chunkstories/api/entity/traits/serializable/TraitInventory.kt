//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.item.inventory.*
import xyz.chunkstories.api.net.packets.PacketInventoryPartialUpdate
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget

open class TraitInventory(entity: Entity, width: Int, height: Int) : TraitSerializable(entity), InventoryCallbacks {
    val inventory: Inventory //private set

    init {
        if (entity !is InventoryOwner)
            throw RuntimeException("You can only add an inventory to an entity if it implements the InventoryHolder interface.")

        inventory = Inventory(width, height, entity, this)
    }

    override fun refreshItemSlot(x: Int, y: Int, pileChanged: ItemPile?) {
        entity.traits[TraitControllable::class]?.controller?.pushPacket(PacketInventoryPartialUpdate(entity.world, inventory, x, y, pileChanged))
    }

    override fun refreshCompleteInventory() {
        pushComponentController()
    }

    override fun isAccessibleTo(entity: Entity): Boolean {
        return entity === this@TraitInventory.entity
    }

    override val inventoryName: String
        get() = entity.traits[TraitName::class]?.name ?: entity::class.java.simpleName

    // Room for expansion
    enum class UpdateMode {
        TOTAL_REFRESH, NEVERMIND
    }

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, stream: DataOutputStream) {
        // Check that person has permission
        if (destinator is Player) {
            val entity = destinator.controlledEntity ?: return

            // Abort if the entity don't have access
            if (!inventory.isAccessibleTo(entity)) {
                // System.out.println(player + "'s " + entity + " don't have access to "+this);
                stream.writeByte(UpdateMode.NEVERMIND.ordinal)
                return
            }
        }
        stream.writeByte(UpdateMode.TOTAL_REFRESH.ordinal)
        inventory.saveToStream(stream, entity.world.contentTranslator)
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, stream: DataInputStream) {
        // Unused
        val b = stream.readByte()

        // Ignore NVM stuff
        if (b.toInt() == UpdateMode.NEVERMIND.ordinal)
            return

        inventory.loadFromStream(stream, entity.world.contentTranslator)
        //inventory = Inventory(stream, entity.world.contentTranslator, entity as InventoryOwner, this)
    }
}
