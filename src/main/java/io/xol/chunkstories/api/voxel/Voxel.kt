//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package io.xol.chunkstories.api.voxel

import io.xol.chunkstories.api.content.Content
import io.xol.chunkstories.api.dsl.LootRules
import io.xol.chunkstories.api.entity.Entity
import io.xol.chunkstories.api.events.voxel.WorldModificationCause
import io.xol.chunkstories.api.exceptions.world.WorldException
import io.xol.chunkstories.api.input.Input
import io.xol.chunkstories.api.item.ItemVoxel
import io.xol.chunkstories.api.item.inventory.ItemPile
import io.xol.chunkstories.api.physics.Box
import io.xol.chunkstories.api.voxel.materials.VoxelMaterial
import io.xol.chunkstories.api.voxel.textures.VoxelTexture
import io.xol.chunkstories.api.world.cell.CellData
import io.xol.chunkstories.api.world.cell.FutureCell
import io.xol.chunkstories.api.world.chunk.Chunk.ChunkCell
import io.xol.chunkstories.api.world.chunk.Chunk.FreshChunkCell
import org.joml.Vector3d

/** Defines the behavior for associated with a voxel type declaration  */
open class Voxel(val definition: VoxelDefinition) {
    val name: String
        get() = definition.name

    /** Returns true only if this voxel is the 'void' air type  */
    fun isAir() = store().air().sameKind(this)

    /** The textures used for rendering this block. Goes unused with custom models ! */
    var voxelTextures = Array<VoxelTexture>(6) { store().textures().defaultVoxelTexture }

    /** The material this block uses */
    var voxelMaterial: VoxelMaterial = store().materials().defaultMaterial

    /** Can entities pass through this block ? */
    var solid = definition.resolveProperty("solid", "false") == "true"
        @JvmName("isSolid")
        get

    /** Does this block completely hides adjacent ones (and can we just skip rendering those hidden faces) ? */
    var opaque = definition.resolveProperty("opaque", "false") == "true"
        @JvmName("isOpaque")
        get

    /** Should adjacent copies of this block have the faces in between them rendered ? */
    var selfOpaque = definition.resolveProperty("selfOpaque", "false") == "true"
        @JvmName("isSelfOpaque")
        get

    /** The light level the block emmits */
    var emittedLightLevel = 0

    /** How much, on top of the normal attenuation, does the light level of light passing through this block is reduced ? */
    var shadingLightLevel = 0

    var collisionBoxes = arrayOf(Box(Vector3d(0.0), Vector3d(1.0)))

    var lootLogic: LootRules? = null

    init {
        definition.resolveProperty("solid")?.let { solid = it.toBoolean() }
        definition.resolveProperty("opaque")?.let { opaque = it.toBoolean() }
        definition.resolveProperty("selfOpaque")?.let { selfOpaque = it.toBoolean() }

        /** Sets all 6 sides of the voxel with one texture */
        definition.resolveProperty("texture")?.let { voxelTextures.fill(store().textures().getVoxelTexture(it)) }
        /** Sets all 4 horizontal sides of the voxel with the same texture */
        definition.resolveProperty("textures.sides")?.let { voxelTextures.fill(store().textures().getVoxelTexture(it), 0, 4) }
        /** Code for setting each side */
        definition.resolveProperty("textures.top")?.let { voxelTextures[VoxelSide.TOP.ordinal] = store().textures().getVoxelTexture(it) }
        definition.resolveProperty("textures.left")?.let { voxelTextures[VoxelSide.LEFT.ordinal] = store().textures().getVoxelTexture(it) }
        definition.resolveProperty("textures.right")?.let { voxelTextures[VoxelSide.RIGHT.ordinal] = store().textures().getVoxelTexture(it) }
        definition.resolveProperty("textures.front")?.let { voxelTextures[VoxelSide.FRONT.ordinal] = store().textures().getVoxelTexture(it) }
        definition.resolveProperty("textures.back")?.let { voxelTextures[VoxelSide.BACK.ordinal] = store().textures().getVoxelTexture(it) }
        definition.resolveProperty("textures.bottom")?.let { voxelTextures[VoxelSide.BOTTOM.ordinal] = store().textures().getVoxelTexture(it) }

        /** Sets a custom voxel material */
        definition.resolveProperty("material")?.let { voxelMaterial = store().materials().getVoxelMaterial(it) }

        definition.resolveProperty("emittedLightLevel")?.let { emittedLightLevel = it.toIntOrNull()?.coerceIn(0..15) ?: 0 }
        definition.resolveProperty("shadingLightLevel")?.let { shadingLightLevel = it.toIntOrNull()?.coerceIn(0..15) ?: 0}
    }

    /** Called before setting a getCell to this Voxel type. Previous state is assumed
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

    /** Called *after* a getCell was successfully placed. Unlike onPlace you can
     * add your voxelComponents here.
     *
     * @param cell
     */
    open fun whenPlaced(cell: FreshChunkCell) {

    }

    /** Called before replacing a getCell contaning this voxel type with air.
     *
     * @param context Current data in this getCell.
     * @param cause The cause of this modification ( can be an Entity )
     * @throws Throw a IllegalBlockModificationException if you want to stop the
     * modification from happening.
     */
    @Throws(WorldException::class)
    open fun onRemove(cell: ChunkCell, cause: WorldModificationCause) {
        // Do nothing
    }

    /** Called when either the metadata, block_light or sun_light values of a getCell
     * of this Voxel type is touched.
     *
     * @param context The current data in this getCell.
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
     * @param `in` The getCell the light is going into (==this one) ( see [            CellData.class][CellData] )
     * @param out The getCell the light is coming from ( see [            CellData.class][CellData] )
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
     * @return An array of Box or null.
     */
    fun getTranslatedCollisionBoxes(cell: CellData): Array<Box>? {
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
     * @return An array of Box or null.
     */
    open fun getCollisionBoxes(info: CellData): Array<Box>? = collisionBoxes

    /** Two voxels are of the same kind if they share the same declaration.  */
    open fun sameKind(that: Voxel): Boolean {
        return this == that
    }

    open fun enumerateItemsForBuilding(): List<ItemPile> {
        return listOf(ItemPile(store().parent().items().getItemDefinition("item_voxel")).apply {
            with(this as ItemVoxel) {
                this.voxel = this@Voxel
            }
        })
    }

    /** Returns what's dropped when a getCell using this voxel type is destroyed  */
    open fun getLoot(cell: CellData, cause: WorldModificationCause): List<ItemPile> {
        /** If this block has custom logic for loot spawning, use that ! */
        val logic = lootLogic
        if (logic != null)
            return logic.spawn()

        /** Returns *one* of the variants for this block */
        return enumerateItemsForBuilding().shuffled().subList(0, 1)
    }

    override fun toString(): String {
        return "[Voxel name:$name]"
    }

    fun store(): Content.Voxels {
        return definition.store
    }
}
