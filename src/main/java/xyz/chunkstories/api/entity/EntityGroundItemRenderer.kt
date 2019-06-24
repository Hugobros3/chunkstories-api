//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.entity

import org.joml.Matrix4f
import org.joml.Vector3d
import xyz.chunkstories.api.entity.traits.TraitRenderable
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.graphics.representation.PointLight
import xyz.chunkstories.api.graphics.systems.dispatching.RepresentationsGobbler
import xyz.chunkstories.api.item.ItemVoxel
import xyz.chunkstories.api.item.inventory.InventoryOwner
import xyz.chunkstories.api.util.kotlin.toVec3d
import xyz.chunkstories.api.util.kotlin.toVec3f

class EntityGroundItemRenderer(private val entity2: EntityGroundItem) : TraitRenderable<EntityGroundItem>(entity2), InventoryOwner {

    override fun buildRepresentation(representationsGobbler: RepresentationsGobbler) {
        val pile = entity.traits[TraitInventory::class]?.inventory?.getItemPileAt(0, 0) ?: return
        //val item = entity.traits[ItemOnGroundContents::class]?.i ?: return //TODO show some error

        val dt = (entity2.spawnTime - System.currentTimeMillis()).toInt() / 1000.0

        val matrix = Matrix4f()
        matrix.translate(entity.location.toVec3f())
        matrix.translate(0f, 0.5f + 0.25f * Math.sin(dt).toFloat(), 0f)
        matrix.rotate(dt.toFloat(), 0f, 1f, 0f)
        pile.item.buildRepresentation(matrix, representationsGobbler)
    }
}