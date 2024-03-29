//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.net.packets

import xyz.chunkstories.api.client.Client
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.obtainInventoryByHandle
import xyz.chunkstories.api.item.inventory.writeInventoryHandle
import xyz.chunkstories.api.net.*
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.player.entityIfIngame
import xyz.chunkstories.api.world.World
import java.io.DataInputStream
import java.io.DataOutputStream

class PacketOpenInventory : PacketWorld {
    protected var inventory: Inventory? = null

    @Suppress("unused")
    constructor(world: World) : super(world)

    @Suppress("unused")
    constructor(world: World, inventory: Inventory) : super(world) {
        this.inventory = inventory
    }

    override fun send(dos: DataOutputStream) {
        writeInventoryHandle(dos, inventory)
    }

    override fun receive(dis: DataInputStream, player: Player?) {
        val inventory = obtainInventoryByHandle(dis, world) ?: error("Can't find inventory to open!")

        val instance = world.gameInstance
        if (instance is Client) {
            val ingameClient = instance.ingame ?: return
            val currentControlledEntity = ingameClient.player.entityIfIngame ?: return

            val ownInventory = currentControlledEntity.traits[TraitInventory::class]?.inventory
            if (ownInventory != null)
                instance.gui.openInventories(ownInventory, inventory)
            else
                instance.gui.openInventories(inventory)
        }
    }

}
