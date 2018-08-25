//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.entity.Entity
import io.xol.chunkstories.api.events.voxel.WorldModificationCause
import io.xol.chunkstories.api.exceptions.world.WorldException
import io.xol.chunkstories.api.dsl.RepresentationBuildingInstructions
import io.xol.chunkstories.api.input.Input
import io.xol.chunkstories.api.item.ItemVoxel
import io.xol.chunkstories.api.item.inventory.ItemPile
import io.xol.chunkstories.api.dsl.LootRules
import io.xol.chunkstories.api.physics.CollisionBox
import io.xol.chunkstories.api.util.kotlin.initOnce
import io.xol.chunkstories.api.voxel.materials.VoxelMaterial
import io.xol.chunkstories.api.voxel.textures.VoxelTexture
import io.xol.chunkstories.api.world.cell.CellData
import io.xol.chunkstories.api.world.cell.FutureCell
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell
import io.xol.chunkstories.api.world.chunk.Chunk.FreshChunkCell
import org.joml.Vector3d

/** Defines the behavior for associated with a voxel type declaration  */
open class Voxel(val store: Content.Voxels) {
    /** Returns the internal, non localized name of this voxel */
    var name: String by initOnce()

    val ext = mutableMapOf<String, String>()

    /** Returns true only if this voxel is the 'void' air type  */
    val isAir: Boolean
        get() = store().air().sameKind(this)

    var customRepresentation: RepresentationBuildingInstructions? = null
    var voxelTextures = Array<VoxelTexture>(6) { store.textures().defaultVoxelTexture }

    var opaque = true
    var solid = true

    var selfOpaque = false

    var emittedLightLevel = 0
    var shadingLightLevel = 0

    var collisionBoxes = arrayOf(CollisionBox(Vector3d(0.0), Vector3d(1.0)))

    /** Returns the VoxelMaterial used by this Voxel  */
    var voxelMaterial: VoxelMaterial = store.materials().defaultMaterial

    var lootLogic: LootRules? = null

    /** Called before setting a cell to this Voxel type. Previous state is assumed
     * to be air.
     *
     * @param newData The data we want to place here. You are welcome to modify it !
     * @throws Throw a IllegalBlockModificationException if you want to stop the
     * modification from happening altogether.
     */
    @Throws(WorldException::class)
    open fun onPlace(cell: FutureCell, cause: WorldModificationCause) {
        // Do nothing
    }

    /** Called *after* a cell was successfully placed. Unlike onPlace you can
     * add your voxelComponents here.
     *
     * @param cell
     */
    open fun whenPlaced(cell: FreshChunkCell) {

    }

    /** Called before replacing a cell contaning this voxel type with air.
     *
     * @param context Current data in this cell.
     * @param cause The cause of this modification ( can be an Entity )
     * @throws Throw a IllegalBlockModificationException if you want to stop the
     * modification from happening.
     */
    @Throws(WorldException::class)
    open fun onRemove(cell: ChunkCell, cause: WorldModificationCause) {
        // Do nothing
    }

    /** Called when either the metadata, block_light or sun_light values of a cell
     * of this Voxel type is touched.
     *
     * @param context The current data in this cell.
     * @param newData The future data we want to put there
     * @param cause The cause of this modification ( can be an Entity )
     * @throws IllegalBlockModificationException If we want to prevent it
     */
    @Throws(WorldException::class)
    open fun onModification(context: ChunkCell, newData: FutureCell, cause: WorldModificationCause) {
        // Do nothing
    }

    /** Called when an Entity's controller sends an Input while looking at this Cell
     *
     * @return True if the interaction was 'handled', and won't be passed to the
     * next stage of the input pipeline
     */
    open fun handleInteraction(entity: Entity, voxelContext: ChunkCell, input: Input): Boolean {
        return false
    }

    /** Gets the Blocklight level this voxel emits
     *
     * @return The aformentioned light level
     */
    open fun getEmittedLightLevel(info: CellData): Int {
        // By default the light output is the one defined in the type, you can change it
        // depending on the provided data
        return emittedLightLevel
    }

    /** Gets the texture for this voxel
     *
     * @param side The side of the block we want the texture of ( see
     * [VoxelSides.class][VoxelSide] )
     */
    open fun getVoxelTexture(cell: CellData, side: VoxelSide): VoxelTexture {
        // By default we don't care about context, we give the same texture to everyone
        return voxelTextures[side.ordinal]
    }

    /** Gets the reduction of the light that will transfer from this block to
     * another, based on data from the two blocks and the side from wich it's
     * leaving the first block from.
     *
     * @param `in` The cell the light is going into (==this one) ( see [            CellData.class][CellData] )
     * @param out The cell the light is coming from ( see [            CellData.class][CellData] )
     * @param side The side of the block light would come out of ( see
     * [VoxelSides.class][VoxelSide] )
     * @return The reduction to apply to the light level on exit
     */
    open fun getLightLevelModifier(cell: CellData, out: CellData, side: VoxelSide): Int {
        return if (opaque) 15 else shadingLightLevel
    }

    /** Used to fine-tune the culling system, allows for a precise, per-face
     * approach to culling.
     *
     * @param face The side of the block BEING DREW ( not the one we are asking ),
     * so in fact we have to answer for the opposite face, that is the
     * one that this voxel connects with. To get a reference on the sides
     * conventions, see [VoxelSides.class][VoxelSide]
     * @param metadata The 8 bits of metadata associated with the block we
     * represent.
     * @return Whether or not that face occlude a whole face and thus we can discard
     * it
     */
    open fun isFaceOpaque(side: VoxelSide, metadata: Int): Boolean {
        return opaque
    }

    /** Get the collision boxes for this object, centered as if the block was in
     * x,y,z
     *
     * @param data The full 4-byte data related to this voxel ( see
     * [VoxelFormat.class][VoxelFormat] )
     * @return An array of CollisionBox or null.
     */
    fun getTranslatedCollisionBoxes(cell: CellData): Array<CollisionBox>? {
        val boxes = getCollisionBoxes(cell)
        if (boxes != null)
            for (b in boxes)
                b.translate(cell.x.toDouble(), cell.y.toDouble(), cell.z.toDouble())
        return boxes
    }

    /** Get the collision boxes for this object, centered as if the block was in
     * 0,0,0
     *
     * @param The full 4-byte data related to this voxel ( see [VoxelFormat.class][VoxelFormat] )
     * @return An array of CollisionBox or null.
     */
    open fun getCollisionBoxes(info: CellData): Array<CollisionBox>? = collisionBoxes

    /** Two voxels are of the same kind if they share the same declaration.  */
    open fun sameKind(that: Voxel): Boolean {
        return this == that
    }

    open fun enumerateItemsForBuilding() : List<ItemPile> {
        return listOf(ItemPile(store.parent().items().getItemDefinition("item_voxel")).apply {
            with(this as ItemVoxel) {
                this.voxel = this@Voxel
            }
        })
    }

    /** Returns what's dropped when a cell using this voxel type is destroyed  */
    open fun getLoot(cell: CellData, cause: WorldModificationCause): List<ItemPile> {
        val logic = lootLogic
        if(logic != null)
            return logic.spawn()

        return enumerateItemsForBuilding()
    }

    override fun toString(): String {
        return "[Voxel name:$name]"
    }

    fun store(): Content.Voxels {
        return store
    }
}
