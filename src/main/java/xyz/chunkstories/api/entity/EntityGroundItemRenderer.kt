package xyz.chunkstories.api.entity

import org.joml.Matrix4f
import xyz.chunkstories.api.entity.traits.TraitRenderable
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler
import xyz.chunkstories.api.item.inventory.InventoryOwner
import xyz.chunkstories.api.util.kotlin.toVec3f

class EntityGroundItemRenderer(entity: EntityGroundItem) : TraitRenderable<EntityGroundItem>(entity), InventoryOwner {

    override fun buildRepresentation(representationsGobbler: RepresentationsGobbler) {
        val pile = entity.traits[TraitInventory::class]?.inventory?.getItemPileAt(0, 0) ?: return
        //val item = entity.traits[ItemOnGroundContents::class]?.i ?: return //TODO show some error

        val matrix = Matrix4f()
        matrix.translate(entity.location.toVec3f())
        pile.item.buildRepresentation(pile, matrix, representationsGobbler)
    }
}