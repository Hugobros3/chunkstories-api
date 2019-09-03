//
// This file is a part of the Chunk Stories API codebase
// Check out README.md for more information
// Website: http://chunkstories.xyz
//

package xyz.chunkstories.api.voxel

import org.joml.Vector3d
import xyz.chunkstories.api.Location
import xyz.chunkstories.api.content.Content
import xyz.chunkstories.api.content.json.*
import xyz.chunkstories.api.entity.Entity
import xyz.chunkstories.api.entity.EntityGroundItem
import xyz.chunkstories.api.entity.traits.serializable.TraitControllable
import xyz.chunkstories.api.entity.traits.serializable.TraitInventory
import xyz.chunkstories.api.events.player.voxel.PlayerVoxelModificationEvent
import xyz.chunkstories.api.events.voxel.WorldModificationCause
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.item.ItemVoxel
import xyz.chunkstories.api.loot.LootTable
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.voxel.materials.VoxelMaterial
import xyz.chunkstories.api.voxel.textures.VoxelTexture
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.cell.EditableCell
import xyz.chunkstories.api.world.cell.FutureCell
import xyz.chunkstories.api.world.chunk.ChunkCell
import xyz.chunkstories.api.world.chunk.FreshChunkCell

/** Defines the behavior for associated with a voxel type declaration  */
open class Voxel(val definition: VoxelDefinition) {
    val name: String
        get() = definition.name

    /** Returns true only if this voxel is the 'void' air type  */
    fun isAir() = store.air.sameKind(this)

    /** The textures used for rendering this block. Goes unused with custom models ! */
    var voxelTextures = Array<VoxelTexture>(6) { store.textures.get(name) }

    /** The material this block uses */
    var voxelMaterial: VoxelMaterial = store.materials.defaultMaterial

    /** Can entities pass through this block ? */
    var solid = definition["solid"].asBoolean ?: true //definition.resolveProperty("solid", "true") == "true"
        @JvmName("isSolid")
        get

    /** Can entities swim through this block ? */
    var liquid = definition["liquid"].asBoolean ?: false //.resolveProperty("liquid", "false") == "true"
        @JvmName("isLiquid")
        get

    /** Does this block completely hides adjacent ones (and can we just skip rendering those hidden faces) ? */
    var opaque = definition["opaque"].asBoolean ?: true //definition.resolveProperty("opaque", "true") == "true"
        @JvmName("isOpaque")
        get

    /** Should adjacent copies of this block have the faces in between them rendered ? */
    var selfOpaque = definition["selfOpaque"].asBoolean ?: false //definition.resolveProperty("selfOpaque", "false") == "true"
        @JvmName("isSelfOpaque")
        get

    /** The light level the block emmits */
    var emittedLightLevel = 0

    /** How much, on top of the normal attenuation, does the light level of light passing through this block is reduced ? */
    var shadingLightLevel = 0

    var lootLogic: LootTable

    var customRenderingRoutine: (ChunkMeshRenderingInterface.(Cell) -> Unit)? = null

    val variants: List<ItemDefinition>
        get() = definition.variants

    init {
        //definition.resolveProperty("solid")?.let { solid = it.toBoolean() }
        //definition.resolveProperty("opaque")?.let { opaque = it.toBoolean() }
        //definition.resolveProperty("selfOpaque")?.let { selfOpaque = it.toBoolean() }

        /** Sets all 6 sides of the voxel with one texture */
        definition["texture"]?.asString?.let { voxelTextures.fill(store.textures.get(it)) }
        //definition.resolveProperty("texture")?.let { voxelTextures.fill(store.textures.get(it)) }
        val textures = definition["textures"]?.asDict
        if (textures != null) {
            /** Sets all 4 horizontal sides of the voxel with the same texture */
            textures["sides"]?.asString?.let { voxelTextures.fill(store.textures.get(it), 0, 4) }

            /** Code for setting each side */
            textures["top"]?.asString?.let { voxelTextures[VoxelSide.TOP.ordinal] = store.textures.get(it) }
            textures["left"]?.asString?.let { voxelTextures[VoxelSide.LEFT.ordinal] = store.textures.get(it) }
            textures["right"]?.asString?.let { voxelTextures[VoxelSide.RIGHT.ordinal] = store.textures.get(it) }
            textures["front"]?.asString?.let { voxelTextures[VoxelSide.FRONT.ordinal] = store.textures.get(it) }
            textures["back"]?.asString?.let { voxelTextures[VoxelSide.BACK.ordinal] = store.textures.get(it) }
            textures["bottom"]?.asString?.let { voxelTextures[VoxelSide.BOTTOM.ordinal] = store.textures.get(it) }
        }
        //definition.resolveProperty("textures.sides")?.let { voxelTextures.fill(store.textures.get(it), 0, 4) }
        /*definition.resolveProperty("textures.top")?.let { voxelTextures[VoxelSide.TOP.ordinal] = store.textures.get(it) }
        definition.resolveProperty("textures.left")?.let { voxelTextures[VoxelSide.LEFT.ordinal] = store.textures.get(it) }
        definition.resolveProperty("textures.right")?.let { voxelTextures[VoxelSide.RIGHT.ordinal] = store.textures.get(it) }
        definition.resolveProperty("textures.front")?.let { voxelTextures[VoxelSide.FRONT.ordinal] = store.textures.get(it) }
        definition.resolveProperty("textures.back")?.let { voxelTextures[VoxelSide.BACK.ordinal] = store.textures.get(it) }
        definition.resolveProperty("textures.bottom")?.let { voxelTextures[VoxelSide.BOTTOM.ordinal] = store.textures.get(it) }*/

        /** Sets a custom voxel material */
        (definition["material"]?.asString ?: name).let { voxelMaterial = store.materials.getVoxelMaterial(it) ?: store.materials.defaultMaterial }
        //(definition.resolveProperty("material") ?: name).let { voxelMaterial = store.materials.getVoxelMaterial(it) ?: store.materials.defaultMaterial }

        (definition["emittedLightLevel"].asInt ?: 0).let { emittedLightLevel = it.coerceIn(0..16) ?: 0 }
        (definition["shadingLightLevel"].asInt ?: 0).let { shadingLightLevel = it.coerceIn(0..16) ?: 0 }

        definition["model"].asString?.let {
            val model = definition.store.parent.models[it]

            customRenderingRoutine = { _ ->
                this.addModel(model)
            }
        }

        //variants = enumerateVariants(store.parent.items)

        //TODO TODO TODO
        lootLogic = LootTable.Nothing(0.0)
    }

    /** Called before setting a cell to this Voxel type. Previous state is assumed
     * to be air. If replacing a block, the previous block is actually removed first.
     *
     * @param cell The data we want to place here. You are welcome to modify it !
     * @param cause The data we want to place here. You are welcome to modify it !
     * @throws WorldException if you want to stop the modification from happening altogether.
     */
    @Throws(WorldException::class)
    open fun onPlace(cell: FutureCell, cause: WorldModificationCause?) {
        // Do nothing
    }

    /** Called after a voxel was successfully placed. Unlike onPlace you can
     * add your voxelComponents here.
     *
     * @param cell
     */
    open fun whenPlaced(cell: FreshChunkCell) {

    }

    /** Called when removing a voxel from the world and replacing it with air
     *
     * @param context Current data in this cell.
     * @param cause The cause of this modification ( can be an Entity )
     * @throws Throw a IllegalBlockModificationException if you want to stop the
     * modification from happening.
     */
    @Throws(WorldException::class)
    open fun onRemove(cell: ChunkCell, cause: WorldModificationCause?) {
        // Do nothing
    }

    /** Called when any of the metadata, block_light or sun_light values of the cell are modified.
     *
     * @param context The current data in this cell.
     * @param newData The future data we want to put there
     * @param cause The cause of this modification ( can be an Entity )
     * @throws IllegalBlockModificationException If we want to prevent it
     */
    @Throws(WorldException::class)
    open fun onModification(context: ChunkCell, newData: FutureCell, cause: WorldModificationCause?) {
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
    open fun getEmittedLightLevel(info: Cell): Int {
        // By default the light output is the one defined in the type, you can change it
        // depending on the provided data
        return emittedLightLevel
    }

    /** Gets the texture for this voxel
     *
     * @param side The side of the block we want the texture of ( see
     * [VoxelSides.class][VoxelSide] )
     */
    open fun getVoxelTexture(cell: Cell, side: VoxelSide): VoxelTexture {
        // By default we don't care about taskInstance, we give the same texture to everyone
        return voxelTextures[side.ordinal]
    }

    /** Gets the reduction of the light that will transfer from this block to
     * another, based on data from the two blocks and the side from wich it's
     * leaving the first block from.
     *
     * @param `in` The cell the light is going into (==this one) ( see [            CellData.class][Cell] )
     * @param out The cell the light is coming from ( see [            CellData.class][Cell] )
     * @param side The side of the block light would come out of ( see
     * [VoxelSides.class][VoxelSide] )
     * @return The reduction to apply to the light level on exit
     */
    open fun getLightLevelModifier(cell: Cell, out: Cell, side: VoxelSide): Int {
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
    fun getTranslatedCollisionBoxes(cell: Cell): Array<Box>? {
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
    open fun getCollisionBoxes(info: Cell): Array<Box>? = arrayOf(Box.fromExtents(Vector3d(1.0)))

    /** Two voxels are of the same kind if they share the same declaration.  */
    open fun sameKind(that: Voxel): Boolean {
        return this == that
    }

    internal fun enumerateVariants_(itemStore: Content.ItemsDefinitions) = enumerateVariants(itemStore)

    protected open fun enumerateVariants(itemStore: Content.ItemsDefinitions): List<ItemDefinition> {
        val definition = ItemDefinition(itemStore, name, Json.Dict(mapOf(
                "voxel" to Json.Value.Text(name),
                "class" to Json.Value.Text(ItemVoxel::class.java.canonicalName!!)
        )))

        return listOf(definition)
    }

    /** Returns the variant that best matches the cell */
    open fun getVariant(cell: Cell): ItemDefinition {
        return variants[0]
    }

    fun enumerateItemsForBuilding(): List<ItemVoxel> {
        return variants.map { it.newItem<Item>() }.filterIsInstance<ItemVoxel>()
    }

    open fun breakBlock(cell: Cell, tool: MiningTool, entity: Entity?) {
        val location = cell.location
        val world = location.world

        val future = FutureCell(cell)
        future.voxel = world.content.voxels.air

        var canBreak = true
        var modificationCause: WorldModificationCause? = null

        if (entity != null) {
            val entityController = entity.traits[TraitControllable::class]?.controller
            if (entityController != null && entityController is Player) {
                if (entity is WorldModificationCause) {
                    modificationCause = entity
                    val event = PlayerVoxelModificationEvent(cell, future, entity, entityController)
                    world.gameContext.pluginManager.fireEvent(event)
                    canBreak = !event.isCancelled
                }
            }
        }

        // Break the block
        if (canBreak) {
            //TODO
            //spawnBlockDestructionParticles(location, world)

            //TODO if cause world
            //if(entity != null)
            //    world.soundManager.playSoundEffect("sounds/gameplay/voxel_remove.ogg", SoundSource.Mode.NORMAL, location, 1.0f, 1.0f)

            val itemSpawnLocation = Location(world, location)
            itemSpawnLocation.add(0.5, 0.25, 0.5)

            // Drop loot !
            for (drop in getLoot(cell, tool)) {
                val thrownItem = world.content.entities.getEntityDefinition("groundItem")!!.newEntity<EntityGroundItem>(itemSpawnLocation.world)
                thrownItem.traitLocation.set(itemSpawnLocation)
                thrownItem.entityVelocity.setVelocity(Vector3d(Math.random() * 0.125 - 0.0625, 0.1, Math.random() * 0.125 - 0.0625))
                thrownItem.traits[TraitInventory::class]!!.inventory.addItem(drop.first, drop.second)
                world.addEntity(thrownItem)
            }

            try {
                world.poke(future, modificationCause)
            } catch (e: WorldException) {
                // Didn't work
                // TODO make some ingame effect so as to clue in the player why it failed
            }
        }
    }

    /** Returns what's dropped when a cell using this voxel type is destroyed  */
    open fun getLoot(cell: Cell, tool: MiningTool): List<Pair<Item, Int>> {
        if (isAir())
            return emptyList()

        ///** If this block has custom logic for loot spawning, use that ! */
        val logic = lootLogic
        //if (logic != null)
        return logic.spawn()

        ///** Returns *one* of the variants for this block */
        //return enumerateItemsForBuilding().shuffled().subList(0, 1).map { Pair(it, 1) }
    }

    open fun tick(cell: EditableCell) {

    }

    override fun toString(): String {
        return "[Voxel name:$name]"
    }

    val store: Content.Voxels
        get() {
            return definition.store
        }
}
