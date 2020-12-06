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
import xyz.chunkstories.api.entity.EntityDroppedItem
import xyz.chunkstories.api.entity.traits.serializable.TraitControllable
import xyz.chunkstories.api.entity.traits.serializable.TraitCreativeMode
import xyz.chunkstories.api.events.voxel.PlayerMineBlockEvent
import xyz.chunkstories.api.exceptions.world.WorldException
import xyz.chunkstories.api.input.Input
import xyz.chunkstories.api.item.Item
import xyz.chunkstories.api.item.ItemDefinition
import xyz.chunkstories.api.item.ItemVoxel
import xyz.chunkstories.api.physics.Box
import xyz.chunkstories.api.player.IngamePlayer
import xyz.chunkstories.api.player.Player
import xyz.chunkstories.api.voxel.materials.VoxelMaterial
import xyz.chunkstories.api.voxel.textures.VoxelTexture
import xyz.chunkstories.api.world.WorldCell
import xyz.chunkstories.api.world.cell.Cell
import xyz.chunkstories.api.world.cell.MutableCell
import xyz.chunkstories.api.world.chunk.ChunkCell
import xyz.chunkstories.api.world.chunk.FreshChunkCell

/** Defines the behavior for associated with a block type declaration  */
open class Voxel(val definition: VoxelDefinition) {
    val name: String
        get() = definition.name

    /** Returns true only if this voxel is the 'void' air type  */
    fun isAir() = store.air.sameKind(this)

    /** The textures used for rendering this block. Goes unused with custom models ! */
    var voxelTextures = Array(6) { store.textures.get(name) }
        protected set

    /** The material this block uses */
    var voxelMaterial: VoxelMaterial = store.materials.defaultMaterial
        protected set

    /** Can entities pass through this block ? */
    var solid = definition["solid"].asBoolean ?: true //definition.resolveProperty("solid", "true") == "true"
        @JvmName("isSolid")
        get
        protected set

    /** Can entities swim through this block ? */
    var liquid = definition["liquid"].asBoolean ?: false //.resolveProperty("liquid", "false") == "true"
        @JvmName("isLiquid")
        get
        protected set

    /** Does this block completely hides adjacent ones (and can we just skip rendering those hidden faces) ? */
    var opaque = definition["opaque"].asBoolean ?: true //definition.resolveProperty("opaque", "true") == "true"
        @JvmName("isOpaque")
        get
        protected set

    /** Should adjacent copies of this block have the faces in between them rendered ? */
    var selfOpaque = definition["selfOpaque"].asBoolean
            ?: false //definition.resolveProperty("selfOpaque", "false") == "true"
        @JvmName("isSelfOpaque")
        get
        protected set

    /** The light level the block emmits */
    var emittedLightLevel = 0
        protected set

    /** How much, on top of the normal attenuation, does the light level of light passing through this block is reduced ? */
    var shadingLightLevel = 0
        protected set

    lateinit var lootLogic: BlockLootTable protected set

    var customRenderingRoutine: (ChunkMeshRenderingInterface.(Cell) -> Unit)? = null

    //TODO remove variants from here and plainly assign them different ids?
    //ie: whool colors: different voxels
    //  !!stair orientations: same voxel, different metadata
    val variants: List<ItemDefinition>
        get() = definition.variants

    init {
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

        /** Sets a custom voxel material */
        (definition["material"]?.asString ?: name).let {
            voxelMaterial = store.materials.getVoxelMaterial(it) ?: store.materials.defaultMaterial
        }

        (definition["emittedLightLevel"].asInt ?: 0).let { emittedLightLevel = it.coerceIn(0..16) ?: 0 }
        (definition["shadingLightLevel"].asInt ?: 0).let { shadingLightLevel = it.coerceIn(0..16) ?: 0 }

        definition["model"].asString?.let {
            val model = definition.store.parent.models[it]

            customRenderingRoutine = { _ ->
                this.addModel(model)
            }
        }
    }

    /** Called after a voxel was successfully placed. Use to initialize additional VoxelComponents.
     * If you want to customize the behavior when attempting to place the block, please subclass ItemVoxel */
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
    open fun onRemove(cell: ChunkCell) {
        // Do nothing
    }

    /** Called when any of the metadata, block_light or sun_light values of the cell are modified.
     *
     * @param cell The current data in this cell.
     * @param newCell The future data we want to put there
     * @param cause The cause of this modification ( can be an Entity )
     * @throws IllegalBlockModificationException If we want to prevent it
     */
    @Throws(WorldException::class)
    open fun onModification(cell: ChunkCell, newCell: FutureCell, cause: WorldModificationCause?) {
        // Do nothing
    }

    /**
     * Called when an Entity's controller sends an Input while looking at this Cell
     *
     * @return True if the interaction was 'handled', and won't be passed to the next stage of the input pipeline
     */
    open fun handleInteraction(entity: Entity, cell: ChunkCell, input: Input): Boolean {
        return false
    }

    /** Must be in [0, 15] */
    open fun getEmittedLightLevel(cell: Cell): Int {
        // By default the light output is the one defined in the type, you can change it
        // depending on the provided data
        return emittedLightLevel
    }

    open fun getVoxelTexture(cell: Cell, side: VoxelSide): VoxelTexture {
        // By default we don't care about taskInstance, we give the same texture to everyone
        return voxelTextures[side.ordinal]
    }

    open fun getLightLevelModifier(cell: Cell, neighbor: Cell, side: VoxelSide): Int {
        return if (opaque) 15 else shadingLightLevel
    }

    open fun isFaceOpaque(cell: Cell, side: VoxelSide): Boolean {
        return opaque
    }

    fun getTranslatedCollisionBoxes(cell: Cell): Array<Box> {
        val boxes = getCollisionBoxes(cell)
        for (b in boxes)
            b.translate(cell.x.toDouble(), cell.y.toDouble(), cell.z.toDouble())
        return boxes
    }

    open fun getCollisionBoxes(cell: Cell): Array<Box> = arrayOf(Box.fromExtents(Vector3d(1.0)))

    /** Two voxels are of the same kind if they share the same declaration.  */
    open fun sameKind(that: Voxel): Boolean {
        return this == that
    }

    internal fun enumerateVariants_(itemStore: Content.ItemsDefinitions): List<ItemDefinition> {
        val variants = enumerateVariants(itemStore)

        val default = variants.getOrNull(0)?.let { Pair(it, 1) }
        lootLogic = makeBlockLootTableFromJson(definition["drops"] ?: Json.Value.Bool(true), itemStore.parent, default)

        return variants
    }

    protected open fun enumerateVariants(itemStore: Content.ItemsDefinitions): List<ItemDefinition> {
        val map = mutableMapOf<String, Json>(
                "voxel" to Json.Value.Text(name),
                "class" to Json.Value.Text(ItemVoxel::class.java.canonicalName!!)
        )

        val additionalItems = definition["itemProperties"].asDict?.elements
        if (additionalItems != null)
            map.putAll(additionalItems)

        val definition = ItemDefinition(itemStore, name, Json.Dict(map))

        return listOf(definition)
    }

    /** Returns the variant that best matches the cell */
    open fun getVariant(cell: Cell): ItemDefinition {
        return variants[0]
    }

    fun enumerateItemsForBuilding(): List<ItemVoxel> {
        return variants.map { it.newItem<Item>() }.filterIsInstance<ItemVoxel>()
    }

    open fun breakBlock(cell: WorldCell, player: IngamePlayer?, tool: MiningTool?) {
        if (tool != null) assert(player != null)

        val location = cell.location
        val world = location.world

        if (player != null) {
            val event = PlayerMineBlockEvent(player, cell, tool!!)
            world.gameInstance.pluginManager.fireEvent(event)
            if (event.isCancelled)
                return

            val shouldDropLoot = tool != TraitCreativeMode.CREATIVE_MODE_MINING_TOOL
            if (shouldDropLoot) {
                for ((item, amount) in getLoot(cell, tool)) {
                    val velocity = Vector3d(Math.random() * 0.125 - 0.0625, 0.1, Math.random() * 0.125 - 0.0625)
                    val lootLocation = Location(cell.location)
                    lootLocation.add(0.5, 0.5, 0.5)
                    EntityDroppedItem.spawn(item, amount, lootLocation, velocity)
                }
            }
        }

        cell.data.additionalData.clear()
        cell.data.extraData = 0
        cell.data.blocklightLevel = 0
        cell.data.sunlightLevel = 0
        cell.data.blockType = world.gameInstance.content.voxels.air

        //TODO spawnBlockDestructionParticles(location, world)
    }

    open fun tick(cell: MutableCell) {}

    /** Returns what's dropped when a cell using this voxel type is destroyed  */
    open fun getLoot(cell: Cell, tool: MiningTool): List<Pair<Item, Int>> {
        if (isAir())
            return emptyList()
        return lootLogic.spawn(tool)
    }

    override fun toString(): String {
        return "[Voxel name:$name]"
    }

    val store: Content.Voxels
        get() {
            return definition.store
        }
}
