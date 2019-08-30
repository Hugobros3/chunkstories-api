//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity.traits.serializable

import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.exceptions.NullItemException
import xyz.chunkstories.api.exceptions.UndefinedItemTypeException
import xyz.chunkstories.api.item.inventory.Inventory
import xyz.chunkstories.api.item.inventory.ItemPile
import xyz.chunkstories.api.item.inventory.obtainItemPileFromStream
import xyz.chunkstories.api.item.inventory.saveIntoStream
import xyz.chunkstories.api.world.WorldMaster
import xyz.chunkstories.api.world.serialization.OfflineSerializedData
import xyz.chunkstories.api.world.serialization.StreamSource
import xyz.chunkstories.api.world.serialization.StreamTarget
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.IOException

class TraitSelectedItem(entity: Entity, traitInventory: TraitInventory) : TraitSerializable(entity) {
    internal var inventory: Inventory

    internal var selectedSlot = 0

    /** Returns the selected item
     *
     * @return
     */
    val selectedItem: ItemPile?
        get() = inventory.getItemPileAt(selectedSlot, 0)

    init {
        this.inventory = traitInventory.inventory
    }

    /** Selects the slot given
     *
     * @param newSlot
     */
    fun setSelectedSlot(newSlot: Int) {
        var newSlot = newSlot
        while (newSlot < 0)
            newSlot += inventory.width
        selectedSlot = newSlot % inventory.width

        // TODO permissions check
        this.pushComponentEveryone()
    }

    /** Returns the selected slot
     *
     * @return
     */
    fun getSelectedSlot(): Int {
        return selectedSlot
    }

    override fun tick() {
        // Tick item in hand if one such exists
        selectedItem?.let { it.item.tickInHand(entity, it) }
    }

    @Throws(IOException::class)
    override fun push(destinator: StreamTarget, dos: DataOutputStream) {
        dos.writeInt(selectedSlot)

        val pile = inventory.getItemPileAt(selectedSlot, 0)
        // don't bother writing the item pile if we're not master or if we'd be telling
        // the controller about his own item pile
        if (pile == null || entity.world !is WorldMaster || entity.traits[TraitControllable::class]?.controller == destinator)
            dos.writeBoolean(false)
        else {
            dos.writeBoolean(true)
            pile.saveIntoStream(entity.world.contentTranslator, dos)
        }
    }

    @Throws(IOException::class)
    override fun pull(from: StreamSource, dis: DataInputStream) {
        selectedSlot = dis.readInt()

        val itemIncluded = dis.readBoolean()
        if (itemIncluded) {
            // System.out.println("reading item from packet for entity"+entity);
            // ItemPile pile;

            val (item, amount) =
                    try {
                        obtainItemPileFromStream(entity.world.contentTranslator, dis)
                    } catch (e: NullItemException) {
                        // Don't do anything about it, no big deal
                        Pair(null, null)
                    } catch (e: UndefinedItemTypeException) {
                        // This is slightly more problematic
                        this.entity.world.gameContext.logger().info(e.message)
                        return
                    }

            // Ensures only client worlds accepts such pushes
            if (entity.world !is WorldMaster)
                inventory.setItemAt(selectedSlot, 0, item, amount ?: 0)
        }

        this.pushComponentEveryoneButController()
    }

    @Throws(IOException::class)
    override fun pushComponentInStream(to: StreamTarget, dos: DataOutputStream) {
        if (to is OfflineSerializedData)
            println("Not writing component SelectedItem to offline data")
        else
            super.pushComponentInStream(to, dos)
    }
}
